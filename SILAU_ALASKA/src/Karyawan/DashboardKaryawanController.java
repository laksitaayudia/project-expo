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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML private Parent transaksi;
    @FXML private TransaksiController transaksiController;

    @FXML private Label lblNamaUser;
    @FXML private Label lblInisial;

    @FXML private Label lblMenunggu;
    @FXML private Label lblDicuci;
    @FXML private Label lblSelesai;
    @FXML private Label lblTotalSaya;

    @FXML private Label lblSudahBayar;
    @FXML private Label lblBelumBayar;

    @FXML private BarChart<String, Number> chartPesanan;

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

        pesananController.setOnDataChanged(this::refreshTampilan);
        transaksiController.setOnDataChanged(this::refreshTampilan);

        setupTabelDashboard();
        tabelPesanan.setItems(Data.getDaftarPesanan());

        refreshTampilan();
    }

    private void setupTabelDashboard() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPelanggan.setCellValueFactory(new PropertyValueFactory<>("pelanggan"));
        colLayanan.setCellValueFactory(new PropertyValueFactory<>("layanan"));
        colBerat.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colBiaya.setCellValueFactory(new PropertyValueFactory<>("biaya"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colId.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "#" + value);
            }
        });

        colBerat.setCellFactory(col -> new TableCell<PesananItem, Double>() {
            @Override protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("%.1f kg", value));
            }
        });

        colBiaya.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("Rp %,d", value).replace(",", "."));
            }
        });

        colStatus.setCellFactory(col -> new TableCell<PesananItem, String>() {
            @Override protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                setText(value);
                setStyle("-fx-text-fill:black; -fx-font-size:12; -fx-font-weight:bold;");
            }
        });
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

        long jumlahSudahBayar = Data.getDaftarTransaksi().stream()
                .filter(t -> t.getStatusBayar().equalsIgnoreCase("Sudah Bayar")).count();
        long jumlahBelumBayar = Data.getDaftarTransaksi().stream()
                .filter(t -> t.getStatusBayar().equalsIgnoreCase("Belum Bayar")).count();

        lblSudahBayar.setText(String.valueOf(jumlahSudahBayar));
        lblBelumBayar.setText(String.valueOf(jumlahBelumBayar));

        tabelPesanan.refresh();

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
        transaksi.setVisible(false);
        transaksi.setManaged(false);
    }

    @FXML
    private void showPesanan() {
        setActive(btnPesanan);
        scrollDashboard.setVisible(false);
        scrollDashboard.setManaged(false);
        pesanan.setVisible(true);
        pesanan.setManaged(true);
        transaksi.setVisible(false);
        transaksi.setManaged(false);
    }

    @FXML
    private void showTransaksi() {
        setActive(btnTransaksi);
        scrollDashboard.setVisible(false);
        scrollDashboard.setManaged(false);
        pesanan.setVisible(false);
        pesanan.setManaged(false);
        transaksi.setVisible(true);
        transaksi.setManaged(true);
    }

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