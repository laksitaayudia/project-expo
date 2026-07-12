package Pelanggan;

import Karyawan.Data;
import Karyawan.KomplainItem;
import Karyawan.PesananItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class KomplainPelangganController {

    @FXML private TableView<KomplainItem> tabelKomplain;
    @FXML private TableColumn<KomplainItem, Integer> colId;
    @FXML private TableColumn<KomplainItem, String> colPesanan;
    @FXML private TableColumn<KomplainItem, String> colJenis;
    @FXML private TableColumn<KomplainItem, String> colDeskripsi;
    @FXML private TableColumn<KomplainItem, String> colStatus;
    @FXML private TableColumn<KomplainItem, String> colSolusi;
    @FXML private TableColumn<KomplainItem, Void> colAksi;

    private Runnable onDataChanged;
    private String namaPelanggan = "Budi Santoso";

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
        refreshData();
    }

    @FXML
    public void initialize() {
        setupKolom();
        setupKolomAksi();
        refreshData();
    }

    public void refreshData() {
        ObservableList<KomplainItem> filtered = FXCollections.observableArrayList();
        for (KomplainItem k : Data.getDaftarKomplain()) {
            String idStr = k.getIdPesanan().replace("PSN", "").replace("0", "");
            try {
                int idVal = Integer.parseInt(idStr);
                for (PesananItem p : Data.getDaftarPesanan()) {
                    if (p.getId() == idVal && p.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                        filtered.add(k);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                filtered.add(k);
            }
        }
        tabelKomplain.setItems(filtered);
    }

    private void setupKolom() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPesanan.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenisKomplain"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSolusi.setCellValueFactory(new PropertyValueFactory<>("solusi"));

        colId.setCellFactory(col -> new TableCell<KomplainItem, Integer>() {
            @Override protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "#" + value);
            }
        });

        colStatus.setCellFactory(col -> new TableCell<KomplainItem, String>() {
            @Override protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                setText(value);
                if (value != null) {
                    if ("Selesai".equalsIgnoreCase(value)) {
                        setStyle("-fx-text-fill:#22c55e; -fx-font-weight:bold; -fx-font-size:12;");
                    } else {
                        setStyle("-fx-text-fill:#f59e0b; -fx-font-weight:bold; -fx-font-size:12;");
                    }
                }
            }
        });
    }

    private void setupKolomAksi() {
        colAksi.setStyle("-fx-alignment: CENTER;");

        colAksi.setCellFactory(col -> new TableCell<KomplainItem, Void>() {

            private final javafx.scene.control.Button btnDetail = new javafx.scene.control.Button("Detail");

            {
                btnDetail.setStyle("-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 12;");

                btnDetail.setOnAction(event -> {
                    KomplainItem item = getTableView().getItems().get(getIndex());
                    bukaDetailKomplain(item);
                });
            }

            @Override
            protected void updateItem(Void value, boolean empty) {
                super.updateItem(value, empty);
                setGraphic(empty ? null : btnDetail);
            }
        });
    }

    private void bukaDetailKomplain(KomplainItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pelanggan/DetailKomplainPelanggan.fxml"));
            Parent root = loader.load();

            DetailKomplainPelangganController controller = loader.getController();
            controller.isiData(item);

            Stage dialog = new Stage();
            dialog.setTitle("Detail Komplain #" + item.getId());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void bukaTambahKomplain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pelanggan/TambahKomplainPelanggan.fxml"));
            Parent root = loader.load();

            TambahKomplainPelangganController controller = loader.getController();
            controller.setNamaPelanggan(namaPelanggan);
            controller.setOnSimpanBerhasil(() -> {
                refreshData();
                if (onDataChanged != null) {
                    onDataChanged.run();
                }
            });

            Stage dialog = new Stage();
            dialog.setTitle("Laporkan Komplain Baru");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}