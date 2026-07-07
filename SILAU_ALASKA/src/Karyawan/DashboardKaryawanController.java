package Karyawan;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardKaryawanController {

    @FXML private Button btnDashboard;
    @FXML private Button btnPesanan;
    @FXML private Button btnTransaksi;
    @FXML private Button btnKomplain;
    @FXML private Button btnKeluar;

    @FXML private ScrollPane scrollDashboard;

    @FXML private Parent pesanan;
    @FXML private PesananController pesananController;

    @FXML private Label lblNamaUser;
    @FXML private Label lblInisial;

    @FXML private Label lblMenunggu;
    @FXML private Label lblDicuci;
    @FXML private Label lblSelesai;
    @FXML private Label lblTotalSaya;

    @FXML private BarChart<String, Number> chartPesanan;

    private static final String STYLE_AKTIF =
            "-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:13; " +
            "-fx-font-weight:bold; -fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    private static final String STYLE_NONAKTIF =
            "-fx-background-color:transparent; -fx-text-fill:#6b7280; -fx-font-size:13; " +
            "-fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    @FXML
    public void initialize() {
        setActive(btnDashboard);

        pesananController.setOnDataChanged(this::refreshTampilan);

        refreshTampilan();
    }

    private void refreshTampilan() {
        ObservableList<PesananItem> data = Data.getDaftarPesanan();

        long jumlahMenunggu = data.stream().filter(p -> p.getStatus().equals("MENUNGGU")).count();
        long jumlahDicuci = data.stream().filter(p -> p.getStatus().equals("DICUCI")).count();
        long jumlahSelesai = data.stream().filter(p -> p.getStatus().equals("SELESAI")).count();

        lblMenunggu.setText(String.valueOf(jumlahMenunggu));
        lblDicuci.setText(String.valueOf(jumlahDicuci));
        lblSelesai.setText(String.valueOf(jumlahSelesai));
        lblTotalSaya.setText(String.valueOf(data.size()));

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

    @FXML
    private void bukaTambahPesanan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Karyawan/TambahPesanan.fxml"));
            Parent root = loader.load();

            TambahPesananController controller = loader.getController();
            controller.setOnSimpanBerhasil(() -> {
                refreshTampilan();
                pesananController.refreshTampilan();
            });

            Stage dialog = new Stage();
            dialog.setTitle("Tambah Pesanan");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard() {
        setActive(btnDashboard);
        scrollDashboard.setVisible(true);
        scrollDashboard.setManaged(true);
        pesanan.setVisible(false);
        pesanan.setManaged(false);
    }

    @FXML
    private void showPesanan() {
        setActive(btnPesanan);
        scrollDashboard.setVisible(false);
        scrollDashboard.setManaged(false);
        pesanan.setVisible(true);
        pesanan.setManaged(true);
    }

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