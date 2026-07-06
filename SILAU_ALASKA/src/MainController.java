import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MainController {

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
            showAlert("Silakan pilih jenis login.");
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username dan Password tidak boleh kosong.");
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText("Menu Registrasi Pelanggan");
        alert.showAndWait();

    }

    private void showAlert(String pesan) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();

    }

}