package Owner;

import Karyawan.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DaftarKaryawanController implements Initializable {

    @FXML private TextField txtNama;
    @FXML private TextField txtTelepon;
    @FXML private ComboBox<String> cbJabatan;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbStatus;

    @FXML private TableView<KaryawanItem> tabelKaryawan;
    @FXML private TableColumn<KaryawanItem, String> colNama;
    @FXML private TableColumn<KaryawanItem, String> colTelepon;
    @FXML private TableColumn<KaryawanItem, String> colJabatan;
    @FXML private TableColumn<KaryawanItem, String> colUsername;
    @FXML private TableColumn<KaryawanItem, String> colStatus;
    @FXML private TableColumn<KaryawanItem, Void> colAksi;

    @FXML private Label lblFormTitle;
    @FXML private Button btnSimpan;
    @FXML private Button btnBatal;

    private String usernameEditing = null;

    private Runnable onDataChanged;

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        colJabatan.setCellValueFactory(new PropertyValueFactory<>("jabatan"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        setupKolomAksi();

        tabelKaryawan.setItems(Data.getDaftarKaryawan());

        cbJabatan.setValue("Kasir");
        cbStatus.setValue("Aktif");

        if (btnBatal != null) {
            btnBatal.setVisible(false);
            btnBatal.setManaged(false);
        }
    }

    private void setupKolomAksi() {
        colAksi.setStyle("-fx-alignment: CENTER;");
        colAksi.setCellFactory(col -> new TableCell<KaryawanItem, Void>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox container = new HBox(6, btnEdit, btnHapus);

            {
                container.setAlignment(Pos.CENTER);

                btnEdit.setStyle(
                        "-fx-background-color:#dbeafe; -fx-text-fill:#1d4ed8; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10; -fx-font-weight:bold;");

                btnHapus.setStyle(
                        "-fx-background-color:#fee2e2; -fx-text-fill:#dc2626; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10; -fx-font-weight:bold;");

                btnEdit.setOnAction(event -> {
                    KaryawanItem item = getTableRow() != null ? getTableRow().getItem() : null;
                    if (item != null) muatFormEdit(item);
                });

                btnHapus.setOnAction(event -> {
                    KaryawanItem item = getTableRow() != null ? getTableRow().getItem() : null;
                    if (item != null) {
                        if (usernameEditing != null && usernameEditing.equalsIgnoreCase(item.getUsername())) {
                            resetForm();
                        }
                        Data.hapusKaryawan(item.getUsername());
                        notifyDataChanged();
                    }
                });
            }

            @Override
            protected void updateItem(Void value, boolean empty) {
                super.updateItem(value, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void muatFormEdit(KaryawanItem item) {
        usernameEditing = item.getUsername();
        txtNama.setText(item.getNama());
        txtTelepon.setText(item.getTelepon());
        cbJabatan.setValue(item.getJabatan());
        txtUsername.setText(item.getUsername());
        txtPassword.setText(item.getPassword());
        cbStatus.setValue(item.getStatus());

        if (lblFormTitle != null) lblFormTitle.setText("Edit Data Karyawan");
        if (btnSimpan != null) btnSimpan.setText("Update Karyawan");
        if (btnBatal != null) {
            btnBatal.setVisible(true);
            btnBatal.setManaged(true);
        }
    }

    @FXML
    private void batalEdit() {
        resetForm();
    }

    @FXML
    private void tambahKaryawan(ActionEvent event) {
        String nama = txtNama.getText().trim();
        String telepon = txtTelepon.getText().trim();
        String jabatan = cbJabatan.getValue();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String status = cbStatus.getValue();

        if (nama.isEmpty() || telepon.isEmpty() || username.isEmpty() || password.isEmpty()
                || jabatan == null || status == null) {
            showAlert("Semua kolom input wajib diisi!");
            return;
        }

        boolean ada = Data.getDaftarKaryawan().stream()
                .anyMatch(k -> k.getUsername().equalsIgnoreCase(username));
        if (ada && (usernameEditing == null || !username.equalsIgnoreCase(usernameEditing))) {
            showAlert("Username '" + username + "' sudah digunakan!");
            return;
        }

        KaryawanItem baru = new KaryawanItem(nama, telepon, jabatan, username, password, status);

        if (usernameEditing != null) {
            Data.editKaryawan(usernameEditing, baru);
        } else {
            Data.tambahKaryawan(baru);
        }

        resetForm();
        notifyDataChanged();
    }

    private void resetForm() {
        usernameEditing = null;
        txtNama.clear();
        txtTelepon.clear();
        txtUsername.clear();
        txtPassword.clear();
        cbJabatan.setValue("Kasir");
        cbStatus.setValue("Aktif");

        if (lblFormTitle != null) lblFormTitle.setText("Daftarkan Karyawan Baru");
        if (btnSimpan != null) btnSimpan.setText("Daftarkan Karyawan");
        if (btnBatal != null) {
            btnBatal.setVisible(false);
            btnBatal.setManaged(false);
        }
    }

    public void refreshTampilan() {
        tabelKaryawan.refresh();
    }

    private void notifyDataChanged() {
        tabelKaryawan.refresh();
        if (onDataChanged != null) {
            onDataChanged.run();
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
