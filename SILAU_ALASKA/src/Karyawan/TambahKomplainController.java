package Karyawan;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class TambahKomplainController {

    @FXML private TextField txtIdPesanan;
    @FXML private ComboBox<String> cbJenis;
    @FXML private TextArea txtDeskripsi;

    private Runnable onSimpanBerhasil;

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    @FXML
    private void simpan(ActionEvent event) {

        String idPesanan = txtIdPesanan.getText();
        String jenis = cbJenis.getEditor().getText();
        String deskripsi = txtDeskripsi.getText();

        if (idPesanan == null || idPesanan.isEmpty()
                || jenis == null || jenis.isEmpty()
                || deskripsi == null || deskripsi.isEmpty()) {
            showAlert("Semua kolom wajib diisi.");
            return;
        }

        int idBaru = Data.idKomplainBerikutnya();
        KomplainItem komplainBaru = new KomplainItem(idBaru, idPesanan, jenis, deskripsi, "Diproses", "");
        Data.tambahKomplain(komplainBaru);

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
