package Karyawan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class KomplainController {

    @FXML private TableView<KomplainItem> tabelKomplain;
    @FXML private TableColumn<KomplainItem, Integer> colIdK;
    @FXML private TableColumn<KomplainItem, String> colIdPesananK;
    @FXML private TableColumn<KomplainItem, String> colJenisK;
    @FXML private TableColumn<KomplainItem, String> colDeskripsiK;
    @FXML private TableColumn<KomplainItem, String> colStatusK;
    @FXML private TableColumn<KomplainItem, Void> colAksiK;

    private Runnable onDataChanged;

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    @FXML
    public void initialize() {
        setupKolom();
        setupKolomAksi();
        tabelKomplain.setItems(Data.getDaftarKomplain());
    }

    public void refreshTampilan() {
        tabelKomplain.refresh();
    }

    private void setupKolom() {
        colIdK.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdPesananK.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colJenisK.setCellValueFactory(new PropertyValueFactory<>("jenisKomplain"));
        colDeskripsiK.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        colStatusK.setCellValueFactory(new PropertyValueFactory<>("status"));

        colIdK.setCellFactory(col -> new TableCell<KomplainItem, Integer>() {
            @Override protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "#" + value);
            }
        });

        colStatusK.setCellFactory(col -> new TableCell<KomplainItem, String>() {
            private final Label badge = new Label();

            @Override protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                    return;
                }
                badge.setText(value);
                badge.setStyle(gayaBadge(value));
                setGraphic(badge);
            }
        });
    }

    private String gayaBadge(String status) {
        String dasar = "-fx-font-size:11; -fx-font-weight:bold; -fx-background-radius:12; -fx-padding:4 12;";

        if (status == null) {
            return dasar + "-fx-background-color:#f1f5f9; -fx-text-fill:#374151;";
        }

        switch (status) {
            case "Selesai":
                return dasar + "-fx-background-color:#dcfce7; -fx-text-fill:#16a34a;";
            case "Ditolak":
                return dasar + "-fx-background-color:#fee2e2; -fx-text-fill:#dc2626;";
            default:
                return dasar + "-fx-background-color:#fef3c7; -fx-text-fill:#92400e;";
        }
    }

    private void setupKolomAksi() {
        colAksiK.setStyle("-fx-alignment: CENTER;");

        colAksiK.setCellFactory(col -> new TableCell<KomplainItem, Void>() {

            private final javafx.scene.control.Button btnTanggapi = new javafx.scene.control.Button("Tanggapi");

            {
                btnTanggapi.setStyle("-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 12;");

                btnTanggapi.setOnAction(event -> {
                    KomplainItem item = getTableView().getItems().get(getIndex());
                    bukaTanggapiKomplain(item);
                });
            }

            @Override
            protected void updateItem(Void value, boolean empty) {
                super.updateItem(value, empty);
                setGraphic(empty ? null : btnTanggapi);
            }
        });
    }

    @FXML
    private void bukaTambahKomplain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Karyawan/TambahKomplain.fxml"));
            Parent root = loader.load();

            TambahKomplainController controller = loader.getController();
            controller.setOnSimpanBerhasil(this::notifyDataChanged);

            Stage dialog = new Stage();
            dialog.setTitle("Catat Komplain Baru");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bukaTanggapiKomplain(KomplainItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Karyawan/TanggapiKomplain.fxml"));
            Parent root = loader.load();

            TanggapiKomplainController controller = loader.getController();
            controller.isiData(item);
            controller.setOnSimpanBerhasil(this::notifyDataChanged);

            Stage dialog = new Stage();
            dialog.setTitle("Tanggapi Komplain #" + item.getId());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyDataChanged() {
        tabelKomplain.refresh();
        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }
}