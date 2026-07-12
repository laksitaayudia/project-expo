package Pelanggan;

import Karyawan.Data;
import Registrasi.PelangganRegister;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class EditProfilPelangganController {

    @FXML
    private Label lblNama;
    @FXML
    private Label lblInisialEdit;

    @FXML
    private TextField txtTelepon;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtAlamat;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPasswordBaru;
    @FXML
    private PasswordField txtKonfirmasiPassword;

    private String namaPelanggan = "";
    private PelangganRegister dataAsli;
    private Consumer<String> onSimpanBerhasil;

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
        lblNama.setText(nama);
        if (lblInisialEdit != null && nama != null && !nama.isEmpty()) {
            lblInisialEdit.setText(nama.substring(0, 1).toUpperCase());
        }

        dataAsli = Data.cariPelangganByNama(nama);
        if (dataAsli != null) {
            txtTelepon.setText(dataAsli.getTelepon());
            txtEmail.setText(dataAsli.getEmail());
            txtAlamat.setText(dataAsli.getAlamat());
            txtUsername.setText(dataAsli.getUsername());
        } else {
            txtUsername.setText(nama.toLowerCase().replace(" ", ""));
        }
    }

    public void setOnSimpanBerhasil(Consumer<String> callback) {
        this.onSimpanBerhasil = callback;
    }

    @FXML
    private void simpan(ActionEvent event) {
        String telepon = txtTelepon.getText();
        String email = txtEmail.getText();
        String alamat = txtAlamat.getText();
        String username = txtUsername.getText();
        String passwordBaru = txtPasswordBaru.getText();
        String konfirmasi = txtKonfirmasiPassword.getText();

        if (telepon.isEmpty() || email.isEmpty() || alamat.isEmpty() || username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Kolom Telepon, Email, Alamat, dan Username wajib diisi.");
            return;
        }

        if (!passwordBaru.isEmpty() || !konfirmasi.isEmpty()) {
            if (!passwordBaru.equals(konfirmasi)) {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Password Baru dan Konfirmasi Password tidak sama.");
                return;
            }
        }

        String passwordAkhir = passwordBaru.isEmpty()
                ? (dataAsli != null ? dataAsli.getPassword() : "")
                : passwordBaru;

        PelangganRegister updated = new PelangganRegister(namaPelanggan, telepon, email, alamat, username, passwordAkhir);

        if (dataAsli != null) {
            Data.editPelanggan(namaPelanggan, updated);
        } else {
            Data.tambahPelanggan(updated);
        }

        showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Profil berhasil diperbarui.");

        if (onSimpanBerhasil != null) {
            onSimpanBerhasil.accept(namaPelanggan);
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

    private void showAlert(Alert.AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}