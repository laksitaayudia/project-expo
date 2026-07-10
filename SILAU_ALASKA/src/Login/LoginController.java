package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import Owner.DashboardOwnerController;

public class LoginController {

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private static final String USER_PELANGGAN = "p";
    private static final String PASS_PELANGGAN = "p123";

    private static final String USER_KARYAWAN = "k";
    private static final String PASS_KARYAWAN = "k123";

    private static final String USER_OWNER = "o";
    private static final String PASS_OWNER = "o123";

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
            pindahKeDashboard(role, username);

        } else if (!usernameBenar && !passwordBenar) {
            showAlert("Username dan Password salah.");

        } else if (!usernameBenar) {
            showAlert("Username salah.");

        } else {
            showAlert("Password salah.");
        }
    }

    private void pindahKeDashboard(String role, String username) {

        String fxmlPath = "";

        switch (role) {
            case "Pelanggan":
                fxmlPath = "/Pelanggan/DashboardPelanggan.fxml";
                break;
            case "Karyawan":
                fxmlPath = "/Karyawan/DashboardKaryawan.fxml";
                break;
            case "Owner":
                fxmlPath = "/Owner/DashboardOwner.fxml";
                break;
        }

        try {
            java.net.URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new java.io.FileNotFoundException("File FXML tidak ditemukan di classpath: " + fxmlPath);
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Kalau role-nya Karyawan, kirim data nama+role ke controllernya
            if (role.equals("Karyawan")) {
                Karyawan.DashboardKaryawanController controller = loader.getController();
                if (controller != null) {
                    controller.setUserData(username, role);
                }
            } else if (role.equals("Pelanggan")) {
                Pelanggan.DashboardPelangganController controller = loader.getController();
                if (controller != null) {
                    controller.setUserData(username, role);
                }
            } else if (role.equals("Owner")) {
                DashboardOwnerController controller = loader.getController();
                if (controller != null) {
                    controller.setUserData(username, role);
                }
            }

            Stage stage = (Stage) cbRole.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("SILAU - Dashboard " + role);

        } catch (Exception e) {
            e.printStackTrace();
            
            // Cari root cause terdalam
            Throwable rootCause = e;
            while (rootCause.getCause() != null) {
                rootCause = rootCause.getCause();
            }
            
            String errorMsg = "Gagal memuat halaman dashboard.\n\n"
                    + "Detail Error: " + e.toString() + "\n\n"
                    + "Penyebab Utama (Root Cause): " + rootCause.toString();
                    
            showAlert(errorMsg);
        }
    }

    @FXML
    private void register(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registrasi/Registrasi.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
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