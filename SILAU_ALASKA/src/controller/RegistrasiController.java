package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrasiController {

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtKonfirmasi;

    @FXML
    private void daftar(ActionEvent event) {

        String nama = txtNama.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String konfirmasi = txtKonfirmasi.getText();

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Semua kolom wajib diisi.");
            return;
        }

        if (!password.equals(konfirmasi)) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Password dan Konfirmasi Password tidak sama.");
            return;
        }

        // simpan data (nama, username, password) di sini.

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
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
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