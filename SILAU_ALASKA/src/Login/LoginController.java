package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    // Data login sementara 
    private static final String USER_PELANGGAN = "pelanggan";
    private static final String PASS_PELANGGAN = "pelanggan123";

    private static final String USER_KARYAWAN = "karyawan";
    private static final String PASS_KARYAWAN = "karyawan123";

    private static final String USER_OWNER = "owner";
    private static final String PASS_OWNER = "owner123";

    @FXML
    private void login() {

        String role = cbRole.getValue();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (role == null) {
            showAlert("Silakan pilih jenis login.");
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username dan Password tidak boleh kosong.");
            return;
        }

        String userAsli = "";
        String passAsli = "";

        switch (role) {

            case "Pelanggan":
                userAsli = USER_PELANGGAN;
                passAsli = PASS_PELANGGAN;
                break;

            case "Karyawan":
                userAsli = USER_KARYAWAN;
                passAsli = PASS_KARYAWAN;
                break;

            case "Owner":
                userAsli = USER_OWNER;
                passAsli = PASS_OWNER;
                break;
        }

        boolean usernameBenar = username.equals(userAsli);
        boolean passwordBenar = password.equals(passAsli);

        if (usernameBenar && passwordBenar) {
            showSukses("Login sebagai " + role + " berhasil!");
            System.out.println("Login " + role + " berhasil");
            // pindah ke scene/halaman dashboard sesuai role di sini

        } else if (!usernameBenar && !passwordBenar) {
            showAlert("Username dan Password salah.");

        } else if (!usernameBenar) {
            showAlert("Username salah.");

        } else {
            showAlert("Password salah.");
        }
    }

    private void showSukses(String pesan) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();

    }

    @FXML
    private void register(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Registrasi.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SILAU - Registrasi");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Gagal memuat halaman registrasi.");
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