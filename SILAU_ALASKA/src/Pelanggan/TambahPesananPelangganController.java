package Pelanggan;

import Karyawan.Data;
import Karyawan.PesananItem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TambahPesananPelangganController {

    @FXML private TextField txtPelanggan;
    @FXML private ComboBox<String> cbLayanan;
    @FXML private DatePicker dpTanggal;
    @FXML private TextField txtBerat;
    @FXML private TextField txtBiaya;

    private Runnable onSimpanBerhasil;
    private String namaPelanggan = "Pelanggan";

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
        if (txtPelanggan != null) {
            txtPelanggan.setText(nama);
        }
    }

    @FXML
    public void initialize() {
        txtPelanggan.setText(namaPelanggan);

        // Auto-update price when weight or service changes
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

            int tarif = 10000; // Default Reguler
            if (layananText.startsWith("Ekspres")) {
                tarif = 15000;
            }

            int totalBiaya = (int) (berat * tarif);
            txtBiaya.setText(String.valueOf(totalBiaya));
        } catch (NumberFormatException e) {
            txtBiaya.setText("0");
        }
    }

    @FXML
    private void simpan(ActionEvent event) {

        String pelanggan = txtPelanggan.getText();
        String layanan = cbLayanan.getValue();
        String beratText = txtBerat.getText();
        String biayaText = txtBiaya.getText();

        if (pelanggan == null || pelanggan.isEmpty() || layanan == null
                || beratText == null || beratText.isEmpty()
                || biayaText == null || biayaText.isEmpty()) {
            showAlert("Semua kolom wajib diisi.");
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
        
        java.time.LocalDate dateVal = dpTanggal.getValue();
        String tanggalText = "";
        if (dateVal != null) {
            tanggalText = dateVal.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            tanggalText = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        int idBaru = Data.idBerikutnya();
        PesananItem pesananBaru = new PesananItem(idBaru, pelanggan, layanan, berat, biaya, "MENUNGGU");
        DataTanggal.setTanggal(idBaru, tanggalText);
        Data.tambahPesanan(pesananBaru);

        if (onSimpanBerhasil != null) {
            onSimpanBerhasil.run();
        }

        Alert sukses = new Alert(Alert.AlertType.INFORMATION);
        sukses.setTitle("Berhasil");
        sukses.setHeaderText(null);
        sukses.setContentText("Pesanan berhasil dibuat! Pesanan Anda sedang diproses.");
        sukses.showAndWait();

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