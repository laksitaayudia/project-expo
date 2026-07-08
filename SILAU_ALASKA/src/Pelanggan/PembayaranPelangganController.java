package Pelanggan;

import Karyawan.Data;
import Karyawan.PesananItem;
import Karyawan.TransaksiItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PembayaranPelangganController {

    @FXML private ComboBox<String> cbPesanan;
    @FXML private ComboBox<String> cbPromo;
    @FXML private ComboBox<String> cbMetode;

    @FXML private Label lblTotalAwal;
    @FXML private Label lblDiskon;
    @FXML private Label lblTotalBayar;

    @FXML private TableView<TransaksiItem> tabelTransaksi;
    @FXML private TableColumn<TransaksiItem, String> colId;
    @FXML private TableColumn<TransaksiItem, String> colPesanan;
    @FXML private TableColumn<TransaksiItem, String> colTotal;
    @FXML private TableColumn<TransaksiItem, String> colStatus;
    @FXML private TableColumn<TransaksiItem, String> colMetode;
    @FXML private TableColumn<TransaksiItem, String> colTanggal;

    private String namaPelanggan = "Budi Santoso";
    private int totalAwal = 0;
    private int diskon = 0;
    private int totalBayar = 0;

    private Runnable onDataChanged;

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
        refreshData();
    }

    @FXML
    public void initialize() {
        // Setup dropdown promo
        cbPromo.setItems(FXCollections.observableArrayList(
                "Tidak Ada Promo",
                "PROMOSILAU (Diskon 10%)",
                "EXPO20 (Diskon 20%)",
                "LUCKY30 (Diskon 30%)"
        ));
        cbPromo.setValue("Tidak Ada Promo");

        // Setup dropdown metode pembayaran
        cbMetode.setItems(FXCollections.observableArrayList(
                "Cash",
                "Transfer",
                "QRIS",
                "E-Wallet"
        ));

        // Listeners for auto-calculate payment details
        cbPesanan.setOnAction(e -> hitungPembayaran());
        cbPromo.setOnAction(e -> hitungPembayaran());

        setupTabel();
        refreshData();
    }

    public void refreshData() {
        // 1. Populate ComboBox Pesanan yang belum lunas (status transaksi "Belum Bayar" atau belum ada transaksi)
        cbPesanan.getItems().clear();
        for (PesananItem p : Data.getDaftarPesanan()) {
            if (p.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                // Check if this order is not paid yet
                boolean sudahBayar = false;
                for (TransaksiItem t : Data.getDaftarTransaksi()) {
                    if (t.getIdPesanan().equals("PSN00" + p.getId()) && "Sudah Bayar".equalsIgnoreCase(t.getStatusBayar())) {
                        sudahBayar = true;
                        break;
                    }
                }
                if (!sudahBayar) {
                    cbPesanan.getItems().add("PSN00" + p.getId() + " - " + p.getLayanan() + " (" + p.getBerat() + " kg)");
                }
            }
        }

        // 2. Populate Tabel Transaksi khusus milik pelanggan ini
        ObservableList<TransaksiItem> filteredTx = FXCollections.observableArrayList();
        for (TransaksiItem t : Data.getDaftarTransaksi()) {
            if (t.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                filteredTx.add(t);
            }
        }
        tabelTransaksi.setItems(filteredTx);
        tabelTransaksi.refresh();

        resetForm();
    }

    private void setupTabel() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colPesanan.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusBayar"));
        colMetode.setCellValueFactory(new PropertyValueFactory<>("metodeBayar"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalBayar"));

        colStatus.setCellFactory(col -> new TableCell<TransaksiItem, String>() {
            @Override protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                setText(value);
                if (value != null) {
                    if ("Sudah Bayar".equalsIgnoreCase(value)) {
                        setStyle("-fx-text-fill:#22c55e; -fx-font-weight:bold; -fx-font-size:12;");
                    } else {
                        setStyle("-fx-text-fill:#dc2626; -fx-font-weight:bold; -fx-font-size:12;");
                    }
                }
            }
        });
    }

    private void hitungPembayaran() {
        String pesananVal = cbPesanan.getValue();
        if (pesananVal == null) {
            totalAwal = 0;
            diskon = 0;
            totalBayar = 0;
            updateSummaryLabels();
            return;
        }

        // Extract ID Pesanan from e.g. "PSN002 - Reguler..."
        String idStr = pesananVal.split(" - ")[0].replace("PSN", "").replace("0", "");
        try {
            int idVal = Integer.parseInt(idStr);
            for (PesananItem p : Data.getDaftarPesanan()) {
                if (p.getId() == idVal) {
                    totalAwal = p.getBiaya();
                    break;
                }
            }
        } catch (NumberFormatException e) {
            totalAwal = 0;
        }

        // Calculate discount based on selected promo code
        double diskonPersen = 0.0;
        String promoVal = cbPromo.getValue();
        if (promoVal != null) {
            if (promoVal.contains("10%")) {
                diskonPersen = 0.10;
            } else if (promoVal.contains("20%")) {
                diskonPersen = 0.20;
            } else if (promoVal.contains("30%")) {
                diskonPersen = 0.30;
            }
        }

        diskon = (int) (totalAwal * diskonPersen);
        totalBayar = totalAwal - diskon;

        updateSummaryLabels();
    }

    private void updateSummaryLabels() {
        lblTotalAwal.setText("Rp " + String.format("%,d", totalAwal).replace(",", "."));
        lblDiskon.setText("-Rp " + String.format("%,d", diskon).replace(",", "."));
        lblTotalBayar.setText("Rp " + String.format("%,d", totalBayar).replace(",", "."));
    }

    private void resetForm() {
        cbPesanan.getSelectionModel().clearSelection();
        cbPromo.setValue("Tidak Ada Promo");
        cbMetode.getSelectionModel().clearSelection();
        totalAwal = 0;
        diskon = 0;
        totalBayar = 0;
        updateSummaryLabels();
    }

    @FXML
    private void prosesBayar() {
        String pesananVal = cbPesanan.getValue();
        String metodeVal = cbMetode.getValue();

        if (pesananVal == null || metodeVal == null) {
            showAlert("Pesanan dan Metode Pembayaran wajib dipilih.");
            return;
        }

        String idPesanan = pesananVal.split(" - ")[0];

        // Format Date to DD/MM/YYYY
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedTotal = "Rp" + String.format("%,d", totalBayar).replace(",", ".");

        // Check if transaction already exists in list (update status) or create new
        boolean foundTx = false;
        for (TransaksiItem t : Data.getDaftarTransaksi()) {
            if (t.getIdPesanan().equals(idPesanan)) {
                t.setStatusBayar("Sudah Bayar");
                t.setMetodeBayar(metodeVal);
                t.setTanggalBayar(formattedDate);
                t.setTotal(formattedTotal);
                foundTx = true;
                break;
            }
        }

        if (!foundTx) {
            String newIdTx = "TRX00" + (Data.getDaftarTransaksi().size() + 1);
            TransaksiItem newTx = new TransaksiItem(
                    newIdTx,
                    idPesanan,
                    namaPelanggan,
                    formattedTotal,
                    "Sudah Bayar",
                    metodeVal,
                    formattedDate
            );
            Data.tambahTransaksi(newTx);
        }

        // Show Success Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText("Pembayaran Berhasil!\nTotal Akhir (Setelah Potongan Diskon): " + formattedTotal);
        alert.showAndWait();

        refreshData();

        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
