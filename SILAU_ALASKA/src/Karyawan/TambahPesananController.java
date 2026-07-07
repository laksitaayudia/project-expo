package Karyawan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TambahPesananController {

    @FXML private TextField txtPelanggan;
    @FXML private ComboBox<String> cbLayanan;
    @FXML private TextField txtBerat;
    @FXML private TextField txtBiaya;

    private Runnable onSimpanBerhasil;
    private PesananItem itemEdit;

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    public void isiUntukEdit(PesananItem item) {
        this.itemEdit = item;
        txtPelanggan.setText(item.getPelanggan());
        cbLayanan.setValue(item.getLayanan());
        txtBerat.setText(String.valueOf(item.getBerat()));
        txtBiaya.setText(String.valueOf(item.getBiaya()));
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

        if (itemEdit != null) {
            Data.hapusPesanan(itemEdit.getId());
            PesananItem hasilEdit = new PesananItem(itemEdit.getId(), pelanggan, layanan, berat, biaya, itemEdit.getStatus());
            Data.tambahPesanan(hasilEdit);
        } else {
            int idBaru = Data.idBerikutnya();
            PesananItem pesananBaru = new PesananItem(idBaru, pelanggan, layanan, berat, biaya, "MENUNGGU");
            Data.tambahPesanan(pesananBaru);
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