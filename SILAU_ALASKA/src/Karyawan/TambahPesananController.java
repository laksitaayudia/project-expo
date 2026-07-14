package Karyawan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TambahPesananController {

    @FXML
    private TextField txtPelanggan;
    @FXML
    private ComboBox<String> cbLayanan;
    @FXML
    private DatePicker dpTanggal;
    @FXML
    private TextField txtBerat;
    @FXML
    private TextField txtBiaya;

    private Runnable onSimpanBerhasil;
    private PesananItem itemEdit;

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    @FXML
    public void initialize() {
        txtBerat.textProperty().addListener((observable, oldValue, newValue) -> hitungBiayaOtomatis());
        cbLayanan.valueProperty().addListener((observable, oldValue, newValue) -> hitungBiayaOtomatis());
    }

    private void hitungBiayaOtomatis() {
        String beratText = txtBerat.getText();
        String layananText = cbLayanan.getValue();

        if (beratText == null || beratText.isEmpty() || layananText == null) {
            txtBiaya.setText("0");
            return;
        }

        try {
            double berat = Double.parseDouble(beratText);
            if (berat < 0) {
                txtBiaya.setText("0");
                return;
            }

            int tarif = 10000;
            if (layananText.startsWith("Ekspres")) {
                tarif = 15000;
            }

            int totalBiaya = (int) (berat * tarif);
            txtBiaya.setText(String.valueOf(totalBiaya));
        } catch (NumberFormatException e) {
            txtBiaya.setText("0");
        }
    }

    public void isiUntukEdit(PesananItem item) {
        this.itemEdit = item;
        txtPelanggan.setText(item.getPelanggan());
        cbLayanan.setValue(item.getLayanan());
        txtBerat.setText(String.valueOf(item.getBerat()));
        txtBiaya.setText(String.valueOf(item.getBiaya()));

        if (item.getTanggal() != null && !item.getTanggal().isEmpty()) {
            try {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                        .ofPattern("dd/MM/yyyy");
                dpTanggal.setValue(java.time.LocalDate.parse(item.getTanggal(), formatter));
            } catch (Exception e) {

            }
        }
    }

    @FXML
    private void simpan(ActionEvent event) {

        String pelanggan = txtPelanggan.getText();
        String layanan = cbLayanan.getValue();
        java.time.LocalDate tanggal = dpTanggal.getValue();
        String beratText = txtBerat.getText();
        String biayaText = txtBiaya.getText();

        if (pelanggan == null || pelanggan.isEmpty() || layanan == null
                || tanggal == null || beratText == null || beratText.isEmpty()
                || biayaText == null || biayaText.isEmpty()) {
            showAlert("Semua kolom (termasuk Tanggal) wajib diisi.");
            return;
        }

        // Cek apakah pelanggan terdaftar
        boolean pelangganDitemukan = false;
        for (Registrasi.PelangganRegister p : Data.getDaftarPelanggan()) {
            if (p.getNama().equalsIgnoreCase(pelanggan)) {
                pelangganDitemukan = true;
                break;
            }
        }
        if (!pelangganDitemukan) {
            showAlert("Pelanggan belum terdaftar, harap daftar dulu!");
            return;
        }

        double berat;
        int biaya;

        try {
            berat = Double.parseDouble(beratText);
            biaya = Integer.parseInt(biayaText);
        } catch (NumberFormatException e) {
            showAlert("Berat dan Biaya harus berupa angka.");
            return;
        }

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String tanggalStr = tanggal.format(formatter);

        if (itemEdit != null) {
            PesananItem hasilEdit = new PesananItem(itemEdit.getId(), pelanggan, layanan, tanggalStr, berat, biaya, itemEdit.getStatus());
            Data.updatePesanan(hasilEdit);
        } else {
            int idBaru = Data.idBerikutnya();
            PesananItem pesananBaru = new PesananItem(idBaru, pelanggan, layanan, tanggalStr, berat, biaya, "MENUNGGU");
            Data.tambahPesanan(pesananBaru);
            
            // Generate Transaksi otomatis (Belum Bayar)
            int nextTrxId = Data.getDaftarTransaksi().stream()
                    .mapToInt(t -> {
                        try {
                            return Integer.parseInt(t.getIdTransaksi().replace("TRX", ""));
                        } catch (Exception e) { return 0; }
                    })
                    .max().orElse(0) + 1;
                    
            String newIdTx = String.format("TRX%03d", nextTrxId);
            String idPes = String.format("PSN%03d", idBaru);
            String formattedTotal = "Rp" + String.format("%,d", biaya).replace(",", ".");
            
            TransaksiItem newTx = new TransaksiItem(newIdTx, idPes, pelanggan, formattedTotal, "Belum Bayar", "-", "-");
            Data.tambahTransaksi(newTx);
        }

        if (onSimpanBerhasil != null) {
            onSimpanBerhasil.run();
        }

        tutup(event);
    }

    @FXML
    private void batal(ActionEvent event) {
        tutup(event);
    }

    private void tutup(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}