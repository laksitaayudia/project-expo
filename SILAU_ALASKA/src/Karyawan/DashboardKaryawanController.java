package Karyawan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardKaryawanController {

    @FXML private Button btnDashboard;
    @FXML private Button btnPesanan;
    @FXML private Button btnTransaksi;
    @FXML private Button btnKomplain;
    @FXML private Button btnKeluar;

    @FXML private Label lblNamaUser;
    @FXML private Label lblInisial;

    @FXML private Label lblMenunggu;
    @FXML private Label lblDicuci;
    @FXML private Label lblSelesai;
    @FXML private Label lblTotalSaya;

    @FXML private BarChart<String, Number> chartPesanan;
    @FXML private CategoryAxis chartXAxis;
    @FXML private NumberAxis chartYAxis;

    @FXML private TableView<PesananItem> tabelPesanan;
    @FXML private TableColumn<PesananItem, Integer> colId;
    @FXML private TableColumn<PesananItem, String> colPelanggan;
    @FXML private TableColumn<PesananItem, String> colLayanan;
    @FXML private TableColumn<PesananItem, Double> colBerat;
    @FXML private TableColumn<PesananItem, Integer> colBiaya;
    @FXML private TableColumn<PesananItem, String> colStatus;

    private static final String STYLE_AKTIF =
            "-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:13; " +
            "-fx-font-weight:bold; -fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    private static final String STYLE_NONAKTIF =
            "-fx-background-color:transparent; -fx-text-fill:#6b7280; -fx-font-size:13; " +
            "-fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    @FXML
    public void initialize() {
        setActive(btnDashboard);

        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colPelanggan.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("pelanggan"));
        colLayanan.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("layanan"));
        colBerat.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("berat"));
        colBiaya.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("biaya"));
        colStatus.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));

        muatDataContoh();
    }

  
    private void muatDataContoh() {

        ObservableList<PesananItem> data = FXCollections.observableArrayList(
                new PesananItem(1, "Budi Santoso", "Reguler — Pagi", 3.5, 35000, "MENUNGGU"),
                new PesananItem(2, "Siti Aminah", "Ekspres — Sore", 2.0, 50000, "DICUCI"),
                new PesananItem(3, "Andi Wijaya", "Reguler — Sore", 4.2, 42000, "MENUNGGU"),
                new PesananItem(4, "Rina Kartika", "Ekspres — Pagi", 1.8, 45000, "SELESAI")
        );

        tabelPesanan.setItems(data);

        long jumlahMenunggu = data.stream().filter(p -> p.getStatus().equals("MENUNGGU")).count();
        long jumlahDicuci = data.stream().filter(p -> p.getStatus().equals("DICUCI")).count();
        long jumlahSelesai = data.stream().filter(p -> p.getStatus().equals("SELESAI")).count();

        lblMenunggu.setText(String.valueOf(jumlahMenunggu));
        lblDicuci.setText(String.valueOf(jumlahDicuci));
        lblSelesai.setText(String.valueOf(jumlahSelesai));
        lblTotalSaya.setText(String.valueOf(data.size()));

        // Data contoh untuk grafik mingguan
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pesanan");
        series.getData().add(new XYChart.Data<>("Sen", 4));
        series.getData().add(new XYChart.Data<>("Sel", 6));
        series.getData().add(new XYChart.Data<>("Rab", 3));
        series.getData().add(new XYChart.Data<>("Kam", 8));
        series.getData().add(new XYChart.Data<>("Jum", 5));
        series.getData().add(new XYChart.Data<>("Sab", 7));
        series.getData().add(new XYChart.Data<>("Min", 2));

        chartPesanan.getData().clear();
        chartPesanan.getData().add(series);

        javafx.application.Platform.runLater(() -> {
            for (XYChart.Data<String, Number> item : series.getData()) {
                if (item.getNode() != null) {
                    item.getNode().setStyle("-fx-bar-fill:#6495ed;");
                }
            }
        });
    }

    public void setUserData(String nama, String role) {
        lblNamaUser.setText(nama);
        lblInisial.setText(nama.substring(0, 1).toUpperCase());
    }

    @FXML private void showDashboard() { setActive(btnDashboard); }
    @FXML private void showPesanan() { setActive(btnPesanan); }
    @FXML private void showTransaksi() { setActive(btnTransaksi); }
    @FXML private void showKomplain() { setActive(btnKomplain); }

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