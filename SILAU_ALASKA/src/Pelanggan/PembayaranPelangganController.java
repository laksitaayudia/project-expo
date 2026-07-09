package Pelanggan;

import Karyawan.Data;
import Karyawan.PesananItem;
import Karyawan.TransaksiItem;
import Owner.PromoItem;
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

    @FXML
    private ComboBox<String> cbPesanan;
    @FXML
    private ComboBox<String> cbPromo;
    @FXML
    private ComboBox<String> cbMetode;

    @FXML
    private Label lblTotalAwal;
    @FXML
    private Label lblDiskon;
    @FXML
    private Label lblTotalBayar;

    @FXML
    private TableView<TransaksiItem> tabelTransaksi;
    @FXML
    private TableColumn<TransaksiItem, String> colId;
    @FXML
    private TableColumn<TransaksiItem, String> colPesanan;
    @FXML
    private TableColumn<TransaksiItem, String> colTotal;
    @FXML
    private TableColumn<TransaksiItem, String> colStatus;
    @FXML
    private TableColumn<TransaksiItem, String> colMetode;
    @FXML
    private TableColumn<TransaksiItem, String> colTanggal;

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
        // Setup dropdown metode pembayaran
        cbMetode.setItems(FXCollections.observableArrayList(
                "Cash",
                "Transfer",
                "QRIS",
                "E-Wallet"));

        // Listeners for auto-calculate payment details
        cbPesanan.setOnAction(e -> hitungPembayaran());
        cbPromo.setOnAction(e -> hitungPembayaran());

        setupTabel();
        refreshData();
    }

    // Hanya promo dengan status "Aktif" yang ditampilkan
    private void refreshCbPromo() {
        String sebelumnya = cbPromo.getValue();

        ObservableList<String> listPromo = FXCollections.observableArrayList();
        listPromo.add("Tidak Ada Promo");

        for (PromoItem promo : Data.getDaftarPromo()) {
            if ("Aktif".equalsIgnoreCase(promo.getStatus())) {
                listPromo.add(promo.getKode());
            }
        }

        cbPromo.setItems(listPromo);

        // Pertahankan pilihan sebelumnya jika masih ada, atau reset ke "Tidak Ada
        // Promo"
        if (sebelumnya != null && listPromo.contains(sebelumnya)) {
            cbPromo.setValue(sebelumnya);
        } else {
            cbPromo.setValue("Tidak Ada Promo");
        }
    }

    public void refreshData() {
        // 1. Refresh ComboBox Promo dari data Owner
        refreshCbPromo();

        // 2. Populate ComboBox Pesanan yang belum lunas
        cbPesanan.getItems().clear();
        for (PesananItem p : Data.getDaftarPesanan()) {
            if (p.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                boolean sudahBayar = false;
                for (TransaksiItem t : Data.getDaftarTransaksi()) {
                    if (t.getIdPesanan().equals("PSN00" + p.getId())
                            && "Sudah Bayar".equalsIgnoreCase(t.getStatusBayar())) {
                        sudahBayar = true;
                        break;
                    }
                }
                if (!sudahBayar) {
                    cbPesanan.getItems()
                            .add("PSN00" + p.getId() + " - " + p.getLayanan() + " (" + p.getBerat() + " kg)");
                }
            }
        }

        // 3. Populate Tabel Transaksi khusus milik pelanggan ini
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
            @Override
            protected void updateItem(String value, boolean empty) {
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

        // Extract biaya dari PesananItem
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

        // Hitung diskon dari PromoItem yang dipilih (bukan hardcoded)
        diskon = 0;
        String promoLabel = cbPromo.getValue();
        if (promoLabel != null && !promoLabel.equals("Tidak Ada Promo")) {
            // Ambil kode promo dari label (kata pertama sebelum spasi)
            String kodePromo = promoLabel.split(" ")[0];
            for (PromoItem promo : Data.getDaftarPromo()) {
                if (promo.getKode().equalsIgnoreCase(kodePromo)
                        && "Aktif".equalsIgnoreCase(promo.getStatus())) {
                    // Validasi minimal belanja
                    if (totalAwal >= promo.getMinBelanja()) {
                        if ("Persentase".equalsIgnoreCase(promo.getTipe())) {
                            diskon = (int) (totalAwal * promo.getDiskon() / 100.0);
                        } else {
                            // Nominal: potongan tetap, tidak boleh melebihi totalAwal
                            diskon = Math.min(promo.getDiskon(), totalAwal);
                        }
                    } else {
                        diskon = 0; // Belanja belum memenuhi syarat minimum
                    }
                    break;
                }
            }
        }

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

        // Validasi ulang minimal belanja jika ada promo dipilih
        String promoLabel = cbPromo.getValue();
        if (promoLabel != null && !promoLabel.equals("Tidak Ada Promo")) {
            String kodePromo = promoLabel.split(" ")[0];
            for (PromoItem promo : Data.getDaftarPromo()) {
                if (promo.getKode().equalsIgnoreCase(kodePromo)) {
                    if (totalAwal < promo.getMinBelanja()) {
                        showAlert("Promo \"" + kodePromo + "\" membutuhkan minimal belanja Rp"
                                + String.format("%,d", promo.getMinBelanja()).replace(",", ".")
                                + ". Diskon tidak diterapkan.");
                    }
                    break;
                }
            }
        }

        String idPesanan = pesananVal.split(" - ")[0];
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedTotal = "Rp" + String.format("%,d", totalBayar).replace(",", ".");

        // Update atau buat transaksi baru
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
                    formattedDate);
            Data.tambahTransaksi(newTx);
        }

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
