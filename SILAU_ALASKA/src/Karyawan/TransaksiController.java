package Karyawan;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TransaksiController implements Initializable {

    @FXML
    private TableView<TransaksiItem> tabelTransaksi;

    @FXML
    private TableColumn<TransaksiItem, String> colId;

    @FXML
    private TableColumn<TransaksiItem, String> colPesanan;

    @FXML
    private TableColumn<TransaksiItem, String> colPelanggan;

    @FXML
    private TableColumn<TransaksiItem, String> colTotal;

    @FXML
    private TableColumn<TransaksiItem, String> colStatus;

    @FXML
    private TableColumn<TransaksiItem, String> colMetode;

    @FXML
    private TableColumn<TransaksiItem, String> colTanggal;

    @FXML
    private TableColumn<TransaksiItem, Void> colAksi;

    private Runnable onDataChanged;

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colPesanan.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colPelanggan.setCellValueFactory(new PropertyValueFactory<>("pelanggan"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusBayar"));
        colMetode.setCellValueFactory(new PropertyValueFactory<>("metodeBayar"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalBayar"));
        tabelTransaksi.setItems(Data.getDaftarTransaksi());

        setupKolomAksi();
    }

    private void setupKolomAksi() {
        colAksi.setStyle("-fx-alignment: CENTER;");

        colAksi.setCellFactory(col -> new TableCell<TransaksiItem, Void>() {

            private final Button btnDetail = new Button("Detail");
            private final Button btnEdit = new Button("Edit");
            private final HBox container = new HBox(6, btnDetail, btnEdit);

            {
                container.setAlignment(Pos.CENTER);

                btnDetail.setStyle("-fx-background-color:#fef3c7; -fx-text-fill:#92400e; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnEdit.setStyle("-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnDetail.setOnAction(event -> {
                    TransaksiItem item = getTableView().getItems().get(getIndex());
                    detail(item);
                });

                btnEdit.setOnAction(event -> {
                    TransaksiItem item = getTableView().getItems().get(getIndex());
                    edit(item);
                });
            }

            @Override
            protected void updateItem(Void value, boolean empty) {
                super.updateItem(value, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void detail(TransaksiItem item) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("Detail");
        alert.setContentText(
                "ID : " + item.getIdTransaksi()
                + "\nPesanan : " + item.getIdPesanan()
                + "\nNama : " + item.getPelanggan()
                + "\nTotal : " + item.getTotal()
                + "\nStatus : " + item.getStatusBayar()
                + "\nMetode : " + item.getMetodeBayar()
                + "\nTanggal : " + item.getTanggalBayar()
        );
        alert.showAndWait();
    }

    private void edit(TransaksiItem item) {
        ChoiceDialog<String> dialogStatus = new ChoiceDialog<>(item.getStatusBayar(), "Sudah Bayar", "Belum Bayar");
        dialogStatus.setTitle("Ubah Status Pembayaran");
        dialogStatus.setHeaderText(null);
        dialogStatus.setContentText("Pilih status baru untuk transaksi " + item.getIdTransaksi() + ":");

        dialogStatus.showAndWait().ifPresent(statusBaru -> {

            if (statusBaru.equals("Belum Bayar")) {
                item.setStatusBayar("Belum Bayar");
                item.setMetodeBayar("-");
                item.setTanggalBayar("-");
                notifyDataChanged();
                return;
            }

            ChoiceDialog<String> dialogMetode = new ChoiceDialog<>("Cash", "Cash", "Transfer", "QRIS", "E-Wallet");
            dialogMetode.setTitle("Metode Pembayaran");
            dialogMetode.setHeaderText(null);
            dialogMetode.setContentText("Pilih metode pembayaran untuk transaksi " + item.getIdTransaksi() + ":");

            dialogMetode.showAndWait().ifPresent(metodeBaru -> {
                item.setStatusBayar("Sudah Bayar");
                item.setMetodeBayar(metodeBaru);
                item.setTanggalBayar(LocalDate.now().toString());
                notifyDataChanged();
            });
        });
    }

    @FXML
    private void bukaTambahTransaksi() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("TambahTransaksi.fxml"));

            Parent root = loader.load();

            TambahTransaksiController controller = loader.getController();
            controller.setOnSimpanBerhasil(this::notifyDataChanged);

            Stage stage = new Stage();
            stage.setTitle("Tambah Transaksi");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyDataChanged() {
        tabelTransaksi.refresh();
        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }
}