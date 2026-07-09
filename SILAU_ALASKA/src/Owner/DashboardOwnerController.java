package Owner;

import Karyawan.Data;
import Karyawan.TransaksiItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardOwnerController {

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnRekap;
    @FXML
    private Button btnPromo;
    @FXML
    private Button btnPengeluaran;
    @FXML
    private Button btnPelanggan;
    @FXML
    private Button btnKeluar;

    @FXML
    private ScrollPane scrollDashboard;

    // Subviews
    @FXML
    private Parent rekapPendapatan;
    @FXML
    private RekapPendapatanController rekapPendapatanController;

    @FXML
    private Parent kelolaPromo;
    @FXML
    private KelolaPromoController kelolaPromoController;

    @FXML
    private Parent pengeluaran;
    @FXML
    private PengeluaranController pengeluaranController;

    @FXML
    private Parent dataPelanggan;
    @FXML
    private DataPelangganController dataPelangganController;

    // Header Info
    @FXML
    private Label lblNamaUser;
    @FXML
    private Label lblInisial;

    // Dashboard Cards
    @FXML
    private Label lblPendapatan;
    @FXML
    private Label lblPengeluaran;
    @FXML
    private Label lblPelangganCount;
    @FXML
    private Label lblPromoCount;

    // Chart
    @FXML
    private BarChart<String, Number> chartKeuangan;

    private static final String STYLE_AKTIF = "-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:13; " +
            "-fx-font-weight:bold; -fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    private static final String STYLE_NONAKTIF = "-fx-background-color:transparent; -fx-text-fill:#6b7280; -fx-font-size:13; "
            +
            "-fx-background-radius:20; -fx-padding:8 16; -fx-cursor:hand;";

    @FXML
    public void initialize() {
        setActive(btnDashboard);

        // Hook up data change listeners for reactive refreshing
        if (kelolaPromoController != null) {
            kelolaPromoController.setOnDataChanged(this::refreshTampilan);
        }
        if (pengeluaranController != null) {
            pengeluaranController.setOnDataChanged(this::refreshTampilan);
        }

        refreshTampilan();
    }

    private void refreshTampilan() {
        // Calculate Pendapatan (Sum of paid transaction totals)
        int totalPendapatan = 0;
        if (Data.getDaftarTransaksi() != null) {
            for (TransaksiItem item : Data.getDaftarTransaksi()) {
                if (item != null && "Sudah Bayar".equalsIgnoreCase(item.getStatusBayar())) {
                    try {
                        String totalStr = item.getTotal();
                        if (totalStr != null) {
                            String cleanTotal = totalStr.replaceAll("[^0-9]", "");
                            if (!cleanTotal.isEmpty()) {
                                totalPendapatan += Integer.parseInt(cleanTotal);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                }
            }
        }
        if (lblPendapatan != null) {
            lblPendapatan.setText(String.format("Rp %,d", totalPendapatan).replace(",", "."));
        }

        // Calculate Pengeluaran
        int totalPengeluaran = 0;
        if (Data.getDaftarPengeluaran() != null) {
            for (PengeluaranItem item : Data.getDaftarPengeluaran()) {
                if (item != null) {
                    totalPengeluaran += item.getJumlah();
                }
            }
        }
        if (lblPengeluaran != null) {
            lblPengeluaran.setText(String.format("Rp %,d", totalPengeluaran).replace(",", "."));
        }

        // Set counts
        if (lblPelangganCount != null && Data.getDaftarPelanggan() != null) {
            lblPelangganCount.setText(String.valueOf(Data.getDaftarPelanggan().size()));
        }
        if (lblPromoCount != null && Data.getDaftarPromo() != null) {
            lblPromoCount.setText(String.valueOf(Data.getDaftarPromo().size()));
        }

        // Populate BarChart (Pendapatan vs Pengeluaran comparison)
        if (chartKeuangan != null) {
            XYChart.Series<String, Number> seriesPendapatan = new XYChart.Series<>();
            seriesPendapatan.setName("Pendapatan");
            seriesPendapatan.getData().add(new XYChart.Data<>("Jan", 1200000));
            seriesPendapatan.getData().add(new XYChart.Data<>("Feb", 1500000));
            seriesPendapatan.getData().add(new XYChart.Data<>("Mar", 1800000));
            seriesPendapatan.getData().add(new XYChart.Data<>("Apr", 2200000));
            seriesPendapatan.getData().add(new XYChart.Data<>("Mei", totalPendapatan));

            XYChart.Series<String, Number> seriesPengeluaran = new XYChart.Series<>();
            seriesPengeluaran.setName("Pengeluaran");
            seriesPengeluaran.getData().add(new XYChart.Data<>("Jan", 400000));
            seriesPengeluaran.getData().add(new XYChart.Data<>("Feb", 600000));
            seriesPengeluaran.getData().add(new XYChart.Data<>("Mar", 500000));
            seriesPengeluaran.getData().add(new XYChart.Data<>("Apr", 700000));
            seriesPengeluaran.getData().add(new XYChart.Data<>("Mei", totalPengeluaran));

            chartKeuangan.getData().clear();
            chartKeuangan.getData().addAll(seriesPendapatan, seriesPengeluaran);

            javafx.application.Platform.runLater(() -> {
                for (XYChart.Data<String, Number> item : seriesPendapatan.getData()) {
                    if (item != null && item.getNode() != null) {
                        item.getNode().setStyle("-fx-bar-fill:#10b981;"); // Green
                    }
                }
                for (XYChart.Data<String, Number> item : seriesPengeluaran.getData()) {
                    if (item != null && item.getNode() != null) {
                        item.getNode().setStyle("-fx-bar-fill:#ef4444;"); // Red
                    }
                }
            });
        }

        // Refresh subcontrollers
        if (rekapPendapatanController != null) {
            rekapPendapatanController.refreshTampilan();
        }
        if (dataPelangganController != null) {
            dataPelangganController.refreshTampilan();
        }
    }

    public void setUserData(String nama, String role) {
        if (lblNamaUser != null) {
            lblNamaUser.setText(nama);
        }
        if (lblInisial != null && nama != null && !nama.isEmpty()) {
            lblInisial.setText(nama.substring(0, 1).toUpperCase());
        }
    }

    @FXML
    private void showDashboard() {
        setActive(btnDashboard);
        if (scrollDashboard != null) {
            scrollDashboard.setVisible(true);
            scrollDashboard.setManaged(true);
        }

        if (rekapPendapatan != null) {
            rekapPendapatan.setVisible(false);
            rekapPendapatan.setManaged(false);
        }
        if (kelolaPromo != null) {
            kelolaPromo.setVisible(false);
            kelolaPromo.setManaged(false);
        }
        if (pengeluaran != null) {
            pengeluaran.setVisible(false);
            pengeluaran.setManaged(false);
        }
        if (dataPelanggan != null) {
            dataPelanggan.setVisible(false);
            dataPelanggan.setManaged(false);
        }

        refreshTampilan();
    }

    @FXML
    private void showRekap() {
        setActive(btnRekap);
        if (scrollDashboard != null) {
            scrollDashboard.setVisible(false);
            scrollDashboard.setManaged(false);
        }

        if (rekapPendapatan != null) {
            rekapPendapatan.setVisible(true);
            rekapPendapatan.setManaged(true);
        }
        if (rekapPendapatanController != null) {
            rekapPendapatanController.refreshTampilan();
        }

        if (kelolaPromo != null) {
            kelolaPromo.setVisible(false);
            kelolaPromo.setManaged(false);
        }
        if (pengeluaran != null) {
            pengeluaran.setVisible(false);
            pengeluaran.setManaged(false);
        }
        if (dataPelanggan != null) {
            dataPelanggan.setVisible(false);
            dataPelanggan.setManaged(false);
        }
    }

    @FXML
    private void showPromo() {
        setActive(btnPromo);
        if (scrollDashboard != null) {
            scrollDashboard.setVisible(false);
            scrollDashboard.setManaged(false);
        }
        if (rekapPendapatan != null) {
            rekapPendapatan.setVisible(false);
            rekapPendapatan.setManaged(false);
        }

        if (kelolaPromo != null) {
            kelolaPromo.setVisible(true);
            kelolaPromo.setManaged(true);
        }

        if (pengeluaran != null) {
            pengeluaran.setVisible(false);
            pengeluaran.setManaged(false);
        }
        if (dataPelanggan != null) {
            dataPelanggan.setVisible(false);
            dataPelanggan.setManaged(false);
        }
    }

    @FXML
    private void showPengeluaran() {
        setActive(btnPengeluaran);
        if (scrollDashboard != null) {
            scrollDashboard.setVisible(false);
            scrollDashboard.setManaged(false);
        }
        if (rekapPendapatan != null) {
            rekapPendapatan.setVisible(false);
            rekapPendapatan.setManaged(false);
        }
        if (kelolaPromo != null) {
            kelolaPromo.setVisible(false);
            kelolaPromo.setManaged(false);
        }

        if (pengeluaran != null) {
            pengeluaran.setVisible(true);
            pengeluaran.setManaged(true);
        }

        if (dataPelanggan != null) {
            dataPelanggan.setVisible(false);
            dataPelanggan.setManaged(false);
        }
    }

    @FXML
    private void showPelanggan() {
        setActive(btnPelanggan);
        if (scrollDashboard != null) {
            scrollDashboard.setVisible(false);
            scrollDashboard.setManaged(false);
        }
        if (rekapPendapatan != null) {
            rekapPendapatan.setVisible(false);
            rekapPendapatan.setManaged(false);
        }
        if (kelolaPromo != null) {
            kelolaPromo.setVisible(false);
            kelolaPromo.setManaged(false);
        }
        if (pengeluaran != null) {
            pengeluaran.setVisible(false);
            pengeluaran.setManaged(false);
        }

        if (dataPelanggan != null) {
            dataPelanggan.setVisible(true);
            dataPelanggan.setManaged(true);
        }
        if (dataPelangganController != null) {
            dataPelangganController.refreshTampilan();
        }
    }

    private void setActive(Button aktif) {
        if (btnDashboard != null) {
            btnDashboard.setStyle(STYLE_NONAKTIF);
        }
        if (btnRekap != null) {
            btnRekap.setStyle(STYLE_NONAKTIF);
        }
        if (btnPromo != null) {
            btnPromo.setStyle(STYLE_NONAKTIF);
        }
        if (btnPengeluaran != null) {
            btnPengeluaran.setStyle(STYLE_NONAKTIF);
        }
        if (btnPelanggan != null) {
            btnPelanggan.setStyle(STYLE_NONAKTIF);
        }
        if (aktif != null) {
            aktif.setStyle(STYLE_AKTIF);
        }
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
