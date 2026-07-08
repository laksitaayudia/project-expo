package Pelanggan;

import Karyawan.Data;
import Karyawan.PesananItem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PesananSayaController {

    @FXML
    private TableView<PesananItem> tabelPesananSaya;
    @FXML
    private TableColumn<PesananItem, Integer> colIdF;
    @FXML
    private TableColumn<PesananItem, String> colTanggalF;
    @FXML
    private TableColumn<PesananItem, String> colLayananF;
    @FXML
    private TableColumn<PesananItem, Double> colBeratF;
    @FXML
    private TableColumn<PesananItem, Integer> colBiayaF;
    @FXML
    private TableColumn<PesananItem, String> colStatusF;

    private Runnable onDataChanged;
    private String namaPelanggan = "Pelanggan";

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
    }

    @FXML
    public void initialize() {
        setupKolom();
        refreshTampilan();
    }

    public void refreshTampilan() {
        javafx.collections.ObservableList<PesananItem> filtered = javafx.collections.FXCollections.observableArrayList();
        for (PesananItem p : Data.getDaftarPesanan()) {
            if (p.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                filtered.add(p);
            }
        }
        tabelPesananSaya.setItems(filtered);
        tabelPesananSaya.refresh();
    }

    private void setupKolom() {
        colIdF.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTanggalF.setCellValueFactory(cellData -> {
            PesananItem item = cellData.getValue();
            if (item != null) {
                return new javafx.beans.property.SimpleStringProperty(DataTanggal.getTanggal(item.getId()));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        colLayananF.setCellValueFactory(new PropertyValueFactory<>("layanan"));
        colBeratF.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colBiayaF.setCellValueFactory(new PropertyValueFactory<>("biaya"));
        colStatusF.setCellValueFactory(new PropertyValueFactory<>("status"));

        colIdF.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "#" + value);
            }
        });

        colBeratF.setCellFactory(col -> new TableCell<PesananItem, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("%.1f kg", value));
            }
        });

        colBiayaF.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("Rp %,d", value).replace(",", "."));
            }
        });

        colStatusF.setCellFactory(col -> new TableCell<PesananItem, String>() {
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

    @FXML
    private void bukaTambahPesanan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pelanggan/TambahPesananPelanggan.fxml"));
            Parent root = loader.load();

            TambahPesananPelangganController controller = loader.getController();
            controller.setNamaPelanggan(namaPelanggan);
            controller.setOnSimpanBerhasil(this::notifyDataChanged);

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

    private void notifyDataChanged() {
        tabelPesananSaya.refresh();
        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }
}
