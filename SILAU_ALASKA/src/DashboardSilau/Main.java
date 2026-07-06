package DashboardSilau;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Main {

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private void login() {

        String role = cbRole.getValue();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (role == null) {
            showAlert("Pilih jenis login!");
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username dan Password harus diisi!");
            return;
        }

        switch (role) {
            case "Pelanggan":
                System.out.println("Login Pelanggan");
                break;

            case "Karyawan":
                System.out.println("Login Karyawan");
                break;

            case "Owner":
                System.out.println("Login Owner");
                break;
        }
    }

    @FXML
    private void register() {
        System.out.println("Menu Register");
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
