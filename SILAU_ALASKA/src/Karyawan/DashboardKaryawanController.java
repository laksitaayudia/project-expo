package Karyawan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardKaryawanController {

    @FXML private Button btnDashboard;
    @FXML private Button btnPesanan;
    @FXML private Button btnTransaksi;
    @FXML private Button btnKomplain;
    @FXML private Button btnKeluar;

    @FXML private Label lblNamaUser;
    @FXML private Label lblRoleUser;
    @FXML private Label lblInisial;
    @FXML private Label lblSelamatDatang;

    @FXML private Label lblMenunggu;
    @FXML private Label lblDicuci;
    @FXML private Label lblSelesai;
    @FXML private Label lblTotalSaya;

    @FXML private TableView<PesananItem> tabelPesanan;
    @FXML private TableColumn<PesananItem, Integer> colId;
    @FXML private TableColumn<PesananItem, String> colPelanggan;
    @FXML private TableColumn<PesananItem, String> colLayanan;
    @FXML private TableColumn<PesananItem, Double> colBerat;
    @FXML private TableColumn<PesananItem, Integer> colBiaya;
    @FXML private TableColumn<PesananItem, String> colStatus;

    private static final String STYLE_AKTIF =
            "-fx-background-color:white; -fx-text-fill:#2196F3; -fx-font-size:13; " +
            "-fx-font-weight:bold; -fx-padding:10 12; -fx-background-radius:8; -fx-cursor:hand;";

    private static final String STYLE_NONAKTIF =
            "-fx-background-color:transparent; -fx-text-fill:white; -fx-font-size:13; " +
            "-fx-padding:10 12; -fx-background-radius:8; -fx-cursor:hand;";

    @FXML
    public void initialize() {
        setActive(btnDashboard);
        setupTabel();
        loadDataDummy();
    }

    private void setupTabel() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPelanggan.setCellValueFactory(new PropertyValueFactory<>("pelanggan"));
        colLayanan.setCellValueFactory(new PropertyValueFactory<>("layanan"));
        colBerat.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colBiaya.setCellValueFactory(new PropertyValueFactory<>("biaya"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colId.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "#" + value);
            }
        });

        colBerat.setCellFactory(col -> new TableCell<PesananItem, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("%.1f kg", value));
            }
        });

        colBiaya.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("Rp %,d", value).replace(",", "."));
            }
        });

        colStatus.setCellFactory(col -> new TableCell<PesananItem, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(status);
                String warna;
                switch (status) {
                    case "SELESAI":
                        warna = "-fx-background-color:#d1fae5; -fx-text-fill:#059669;";
                        break;
                    case "DICUCI":
                        warna = "-fx-background-color:#dbeafe; -fx-text-fill:#2196F3;";
                        break;
                    case "MENUNGGU":
                        warna = "-fx-background-color:#fef3c7; -fx-text-fill:#d97706;";
                        break;
                    default:
                        warna = "-fx-background-color:#f1f5f9; -fx-text-fill:#64748b;";
                }
                setStyle(warna + " -fx-background-radius:12; -fx-font-size:11; -fx-font-weight:bold; " +
                          "-fx-padding:4 12; -fx-alignment:CENTER;");
            }
        });
    }

    private void loadDataDummy() {
        ObservableList<PesananItem> data = FXCollections.observableArrayList(
                new PesananItem(2, "Siti Rahayu", "Ekspres — Siang", 2.0, 50000, "SELESAI"),
                new PesananItem(3, "Eko Wahyono", "Reguler — Sore", 4.2, 42000, "DICUCI"),
                new PesananItem(4, "Budi Santoso", "Ekspres — Pagi", 1.8, 45000, "MENUNGGU"),
                new PesananItem(5, "Siti Rahayu", "Reguler — Siang", 2.7, 27000, "MENUNGGU")
        );
        tabelPesanan.setItems(data);
    }

    public void setUserData(String nama, String role) {
        lblNamaUser.setText(nama);
        lblRoleUser.setText(role);
        lblSelamatDatang.setText("Selamat Datang, " + nama);
        lblInisial.setText(nama.substring(0, 1).toUpperCase());
    }

    @FXML
    private void showDashboard() {
        setActive(btnDashboard);
    }

    @FXML
    private void showPesanan() {
        setActive(btnPesanan);
    }

    @FXML
    private void showTransaksi() {
        setActive(btnTransaksi);
    }

    @FXML
    private void showKomplain() {
        setActive(btnKomplain);
    }

    private void setActive(Button aktif) {
        btnDashboard.setStyle(STYLE_NONAKTIF);
        btnPesanan.setStyle(STYLE_NONAKTIF);
        btnTransaksi.setStyle(STYLE_NONAKTIF);
        btnKomplain.setStyle(STYLE_NONAKTIF);
        aktif.setStyle(STYLE_AKTIF);
    }

    @FXML
    private void keluar(ActionEvent event) {
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
}
