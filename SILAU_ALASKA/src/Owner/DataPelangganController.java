package Owner;

import Karyawan.Data;
import Registrasi.PelangganRegister;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DataPelangganController implements Initializable {

    @FXML
    private TableView<PelangganRegister> tabelPelanggan;

    @FXML
    private TableColumn<PelangganRegister, String> colNama;

    @FXML
    private TableColumn<PelangganRegister, String> colTelepon;

    @FXML
    private TableColumn<PelangganRegister, String> colEmail;

    @FXML
    private TableColumn<PelangganRegister, String> colAlamat;

    @FXML
    private TableColumn<PelangganRegister, String> colUsername;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        tabelPelanggan.setItems(Data.getDaftarPelanggan());
    }

    public void refreshTampilan() {
        tabelPelanggan.refresh();
    }
}
