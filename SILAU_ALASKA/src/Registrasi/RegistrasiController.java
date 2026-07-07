package Registrasi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class RegistrasiController {

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtTelepon;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtAlamat;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtKonfirmasi;

    @FXML
    private void daftar(ActionEvent event) {

        String nama = txtNama.getText();
        String telepon = txtTelepon.getText();
        String email = txtEmail.getText();
        String alamat = txtAlamat.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String konfirmasi = txtKonfirmasi.getText();

        if (nama.isEmpty() || telepon.isEmpty() || email.isEmpty() || alamat.isEmpty()
                || username.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Semua kolom wajib diisi.");
            return;
        }

        if (!password.equals(konfirmasi)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Password dan Konfirmasi Password tidak sama.");
            return;
        }

        // simpan data (nama, telepon, email, alamat, username, password) di sini.

        showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Pendaftaran berhasil! Silakan login.");

        kembaliKeLogin(event);
    }

    @FXML
    private void kembaliLogin(ActionEvent event) {
        kembaliKeLogin(event);
    }

    private void kembaliKeLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("SILAU");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

}