package Pelanggan;

import Karyawan.Data;
import Karyawan.KomplainItem;
import Karyawan.PesananItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TambahKomplainPelangganController {

    @FXML private ComboBox<String> cbPesanan;
    @FXML private ComboBox<String> cbJenisKomplain;
    @FXML private TextArea txtDeskripsi;

    private Runnable onSimpanBerhasil;
    private String namaPelanggan = "Budi Santoso";

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
        populatePesanan();
    }

    @FXML
    public void initialize() {
        populatePesanan();
    }

    private void populatePesanan() {
        cbPesanan.getItems().clear();
        for (PesananItem p : Data.getDaftarPesanan()) {
            if (p.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                cbPesanan.getItems().add("PSN00" + p.getId() + " - " + p.getLayanan());
            }
        }
        if (cbPesanan.getItems().isEmpty()) {
            cbPesanan.getItems().add("Tidak ada pesanan aktif");
        }
    }

    @FXML
    private void simpan(ActionEvent event) {
        String pesananVal = cbPesanan.getValue();
        String jenisVal = cbJenisKomplain.getValue();
        String deskripsi = txtDeskripsi.getText();

        if (pesananVal == null || pesananVal.startsWith("Tidak ada") || jenisVal == null || deskripsi == null || deskripsi.trim().isEmpty()) {
            showAlert("Semua kolom wajib diisi.");
            return;
        }

        String idPesanan = pesananVal.split(" - ")[0];

        int newId = Data.idKomplainBerikutnya();
        KomplainItem newKomplain = new KomplainItem(newId, idPesanan, jenisVal, deskripsi, "Diproses", "-");
        Data.tambahKomplain(newKomplain);

        if (onSimpanBerhasil != null) {
            onSimpanBerhasil.run();
        }

        Alert sukses = new Alert(Alert.AlertType.INFORMATION);
        sukses.setTitle("Berhasil");
        sukses.setHeaderText(null);
        sukses.setContentText("Laporan komplain berhasil dikirim. Kami akan segera menindaklanjutinya.");
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
