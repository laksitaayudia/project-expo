package Karyawan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PesananController {

    @FXML private TableView<PesananItem> tabelPesananFull;
    @FXML private TableColumn<PesananItem, Integer> colIdF;
    @FXML private TableColumn<PesananItem, String> colPelangganF;
    @FXML private TableColumn<PesananItem, String> colLayananF;
    @FXML private TableColumn<PesananItem, Double> colBeratF;
    @FXML private TableColumn<PesananItem, Integer> colBiayaF;
    @FXML private TableColumn<PesananItem, String> colStatusF;
    @FXML private TableColumn<PesananItem, Void> colAksiF;

    private Runnable onDataChanged;

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    @FXML
    public void initialize() {
        setupKolom();
        setupKolomAksi();
        tabelPesananFull.setItems(Data.getDaftarPesanan());
    }

    public void refreshTampilan() {
        tabelPesananFull.refresh();
    }

    private void setupKolom() {
        colIdF.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPelangganF.setCellValueFactory(new PropertyValueFactory<>("pelanggan"));
        colLayananF.setCellValueFactory(new PropertyValueFactory<>("layanan"));
        colBeratF.setCellValueFactory(new PropertyValueFactory<>("berat"));
        colBiayaF.setCellValueFactory(new PropertyValueFactory<>("biaya"));
        colStatusF.setCellValueFactory(new PropertyValueFactory<>("status"));

        colIdF.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "#" + value);
            }
        });

        colBeratF.setCellFactory(col -> new TableCell<PesananItem, Double>() {
            @Override protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("%.1f kg", value));
            }
        });

        colBiayaF.setCellFactory(col -> new TableCell<PesananItem, Integer>() {
            @Override protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("Rp %,d", value).replace(",", "."));
            }
        });

        colStatusF.setCellFactory(col -> new TableCell<PesananItem, String>() {
            @Override protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                setText(value);
                setStyle("-fx-text-fill:black; -fx-font-size:12; -fx-font-weight:bold;");
            }
        });
    }

    private void setupKolomAksi() {
        colAksiF.setStyle("-fx-alignment: CENTER;");

        colAksiF.setCellFactory(col -> new TableCell<PesananItem, Void>() {

            private final Button btnStatus = new Button("Status");
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox container = new HBox(6, btnStatus, btnEdit, btnHapus);

            {
                container.setAlignment(javafx.geometry.Pos.CENTER);

                btnStatus.setStyle("-fx-background-color:#fef3c7; -fx-text-fill:#92400e; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnEdit.setStyle("-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnHapus.setStyle("-fx-background-color:#fee2e2; -fx-text-fill:#dc2626; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnStatus.setOnAction(event -> {
                    PesananItem item = getTableView().getItems().get(getIndex());
                    ubahStatus(item);
                });

                btnEdit.setOnAction(event -> {
                    PesananItem item = getTableView().getItems().get(getIndex());
                    bukaEditPesanan(item);
                });

                btnHapus.setOnAction(event -> {
                    PesananItem item = getTableView().getItems().get(getIndex());
                    konfirmasiHapus(item);
                });
            }

            @Override
            protected void updateItem(Void value, boolean empty) {
                super.updateItem(value, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    @FXML
    private void bukaTambahPesanan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Karyawan/TambahPesanan.fxml"));
            Parent root = loader.load();

            TambahPesananController controller = loader.getController();
            controller.setOnSimpanBerhasil(this::notifyDataChanged);

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

    private void bukaEditPesanan(PesananItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Karyawan/TambahPesanan.fxml"));
            Parent root = loader.load();

            TambahPesananController controller = loader.getController();
            controller.isiUntukEdit(item);
            controller.setOnSimpanBerhasil(this::notifyDataChanged);

            Stage dialog = new Stage();
            dialog.setTitle("Edit Pesanan");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void konfirmasiHapus(PesananItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText(null);
        alert.setContentText("Yakin ingin menghapus pesanan #" + item.getId() + " (" + item.getPelanggan() + ")?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Data.hapusPesanan(item.getId());
                notifyDataChanged();
            }
        });
    }

    private void ubahStatus(PesananItem item) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(item.getStatus(), "MENUNGGU", "DICUCI", "SELESAI");
        dialog.setTitle("Ubah Status Pesanan");
        dialog.setHeaderText(null);
        dialog.setContentText("Pilih status baru untuk pesanan #" + item.getId() + ":");

        dialog.showAndWait().ifPresent(statusBaru -> {
            item.setStatus(statusBaru);
            notifyDataChanged();
        });
    }

    private void notifyDataChanged() {
        tabelPesananFull.refresh();
        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }
}