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
        refreshData();
    }

    public void refreshData() {
        // Filter complaints where the corresponding order belongs to this customer
        ObservableList<KomplainItem> filtered = FXCollections.observableArrayList();
        for (KomplainItem k : Data.getDaftarKomplain()) {
            // Find order
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
                // Fallback: if name lookup matches or for demo, add all
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
