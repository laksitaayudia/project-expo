package Karyawan;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class TanggapiKomplainController {

    @FXML private Label lblIdPesanan;
    @FXML private Label lblJenis;
    @FXML private Label lblDeskripsi;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextArea txtSolusi;

    private Runnable onSimpanBerhasil;
    private KomplainItem itemKomplain;

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    public void isiData(KomplainItem item) {
        this.itemKomplain = item;

        lblIdPesanan.setText("Pesanan: " + item.getIdPesanan());
        lblJenis.setText("Jenis: " + item.getJenisKomplain());
        lblDeskripsi.setText(item.getDeskripsi());

        cbStatus.setValue(item.getStatus());
        txtSolusi.setText(item.getSolusi());
    }

    @FXML
    private void simpan(ActionEvent event) {

        String statusBaru = cbStatus.getValue();
        String solusiBaru = txtSolusi.getText();

        if (statusBaru == null || statusBaru.isEmpty()) {
            showAlert("Silakan pilih status komplain.");
            return;
        }

        if (statusBaru.equals("Selesai") && (solusiBaru == null || solusiBaru.isEmpty())) {
            showAlert("Isi solusi/tanggapan sebelum menandai komplain sebagai Selesai.");
            return;
        }

        Data.perbaruiKomplain(itemKomplain.getId(), statusBaru, solusiBaru == null ? "" : solusiBaru);

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