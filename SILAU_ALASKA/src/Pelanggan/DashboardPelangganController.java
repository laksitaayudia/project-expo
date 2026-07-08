package Pelanggan;

import Karyawan.Data;
import Karyawan.PesananItem;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.io.IOException;

public class DashboardPelangganController {

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnPesananSaya;
    @FXML
    private Button btnPembayaran;
    @FXML
    private Button btnKomplain;
    @FXML
    private Button btnKeluar;

    @FXML
    private ScrollPane scrollDashboard;

    @FXML
    private Parent pesananSaya;
    @FXML
    private PesananSayaController pesananSayaController;

    @FXML
    private Parent pembayaran;
    @FXML
    private PembayaranPelangganController pembayaranController;

    @FXML
    private Parent komplain;
    @FXML
    private KomplainPelangganController komplainController;

    @FXML
    private Label lblNamaUser;
    @FXML
    private Label lblInisial;

    @FXML
    private Label lblTotalSaya;
    @FXML
    private Label lblMenunggu;
    @FXML
    private Label lblDicuci;
    @FXML
    private Label lblSelesai;

    @FXML
    private TableView<PesananItem> tabelPesanan;
    @FXML
    private TableColumn<PesananItem, Integer> colId;
    @FXML
    private TableColumn<PesananItem, String> colTanggal;
    @FXML
    private TableColumn<PesananItem, String> colLayanan;
    @FXML
    private TableColumn<PesananItem, Double> colBerat;
    @FXML
    private TableColumn<PesananItem, Integer> colBiaya;
    @FXML
    private TableColumn<PesananItem, String> colStatus;

    @FXML
    private BarChart<String, Number> chartPesanan;

    private String namaPelanggan = "Budi Santoso";

    private static final String STYLE_AKTIF = "-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:13; " +
            "-fx-font-weight:bold; -fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    private static final String STYLE_NONAKTIF = "-fx-background-color:transparent; -fx-text-fill:#6b7280; -fx-font-size:13; "
            +
            "-fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    @FXML
    public void initialize() {
        setActive(btnDashboard);

        // Setup callbacks from nested pages
        if (pesananSayaController != null) {
            pesananSayaController.setOnDataChanged(this::refreshTampilan);
        }
        if (pembayaranController != null) {
            pembayaranController.setOnDataChanged(this::refreshTampilan);
        }
        if (komplainController != null) {
            komplainController.setOnDataChanged(this::refreshTampilan);
        }

        setupTabelDashboard();
        tabelPesanan.setItems(Data.getDaftarPesanan());

        refreshTampilan();
    }

    private void setupTabelDashboard() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTanggal.setCellValueFactory(cellData -> {
            PesananItem item = cellData.getValue();
            if (item != null) {
                return new javafx.beans.property.SimpleStringProperty(DataTanggal.getTanggal(item.getId()));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
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
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                setText(value);
                if (value != null) {
                    switch (value) {
                        case "MENUNGGU":
                            setStyle("-fx-text-fill:#f59e0b; -fx-font-size:12; -fx-font-weight:bold;");
                            break;
                        case "DICUCI":
                            setStyle("-fx-text-fill:#3b82f6; -fx-font-size:12; -fx-font-weight:bold;");
                            break;
                        case "SELESAI":
                            setStyle("-fx-text-fill:#22c55e; -fx-font-size:12; -fx-font-weight:bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill:black; -fx-font-size:12; -fx-font-weight:bold;");
                    }
                } else {
                    setStyle("-fx-text-fill:black; -fx-font-size:12; -fx-font-weight:bold;");
                }
            }
        });
    }

    private void refreshTampilan() {
        ObservableList<PesananItem> filtered = javafx.collections.FXCollections.observableArrayList();
        for (PesananItem p : Data.getDaftarPesanan()) {
            if (p.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                filtered.add(p);
            }
        }

        long jumlahMenunggu = filtered.stream().filter(p -> p.getStatus().equals("MENUNGGU")).count();
        long jumlahDicuci = filtered.stream().filter(p -> p.getStatus().equals("DICUCI")).count();
        long jumlahSelesai = filtered.stream().filter(p -> p.getStatus().equals("SELESAI")).count();

        lblTotalSaya.setText(String.valueOf(filtered.size()));
        lblMenunggu.setText(String.valueOf(jumlahMenunggu));
        lblDicuci.setText(String.valueOf(jumlahDicuci));
        lblSelesai.setText(String.valueOf(jumlahSelesai));

        tabelPesanan.setItems(filtered);
        tabelPesanan.refresh();

        // Populate BarChart (Sama dengan Karyawan)
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

        if (pesananSayaController != null) {
            pesananSayaController.refreshTampilan();
        }
        if (pembayaranController != null) {
            pembayaranController.refreshData();
        }
        if (komplainController != null) {
            komplainController.refreshData();
        }
    }

    public void setUserData(String nama, String role) {
        this.namaPelanggan = nama;
        lblNamaUser.setText(nama);
        lblInisial.setText(nama.substring(0, 1).toUpperCase());

        if (pesananSayaController != null) {
            pesananSayaController.setNamaPelanggan(nama);
        }
        if (pembayaranController != null) {
            pembayaranController.setNamaPelanggan(nama);
        }
        if (komplainController != null) {
            komplainController.setNamaPelanggan(nama);
        }

        refreshTampilan();
    }

    @FXML
    private void bukaTambahPesanan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pelanggan/TambahPesananPelanggan.fxml"));
            Parent root = loader.load();

            TambahPesananPelangganController controller = loader.getController();
            controller.setNamaPelanggan(namaPelanggan);
            controller.setOnSimpanBerhasil(this::refreshTampilan);

            Stage dialog = new Stage();
            dialog.setTitle("Buat Pesanan Baru");
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
        pesananSaya.setVisible(false);
        pesananSaya.setManaged(false);
        pembayaran.setVisible(false);
        pembayaran.setManaged(false);
        komplain.setVisible(false);
        komplain.setManaged(false);

        refreshTampilan();
    }

    @FXML
    private void showPesananSaya() {
        setActive(btnPesananSaya);
        scrollDashboard.setVisible(false);
        scrollDashboard.setManaged(false);
        pesananSaya.setVisible(true);
        pesananSaya.setManaged(true);
        pembayaran.setVisible(false);
        pembayaran.setManaged(false);
        komplain.setVisible(false);
        komplain.setManaged(false);
    }

    @FXML
    private void showPembayaran() {
        setActive(btnPembayaran);
        scrollDashboard.setVisible(false);
        scrollDashboard.setManaged(false);
        pesananSaya.setVisible(false);
        pesananSaya.setManaged(false);
        pembayaran.setVisible(true);
        pembayaran.setManaged(true);
        komplain.setVisible(false);
        komplain.setManaged(false);

        if (pembayaranController != null) {
            pembayaranController.refreshData();
        }
    }

    @FXML
    private void showKomplain() {
        setActive(btnKomplain);
        scrollDashboard.setVisible(false);
        scrollDashboard.setManaged(false);
        pesananSaya.setVisible(false);
        pesananSaya.setManaged(false);
        pembayaran.setVisible(false);
        pembayaran.setManaged(false);
        komplain.setVisible(true);
        komplain.setManaged(true);
    }

    private void setActive(Button aktif) {
        btnDashboard.setStyle(STYLE_NONAKTIF);
        btnPesananSaya.setStyle(STYLE_NONAKTIF);
        btnPembayaran.setStyle(STYLE_NONAKTIF);
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
