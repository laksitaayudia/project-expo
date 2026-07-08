package Karyawan;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableCell;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        colPesanan.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colPelanggan.setCellValueFactory(new PropertyValueFactory<>("pelanggan"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colMetode.setCellValueFactory(new PropertyValueFactory<>("metodeBayar"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalBayar"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusBayar"));
        tabelTransaksi.setItems(Data.getDaftarTransaksi());
        setupKolomAksi();

        colStatus.setCellFactory(column -> new TableCell<TransaksiItem, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                if (item.equalsIgnoreCase("Sudah Bayar")) {
                    setText("● Sudah Bayar");
                    setStyle(
                            "-fx-background-color:#DCFCE7;" +
                            "-fx-text-fill:#15803D;" +
                            "-fx-font-weight:bold;" +
                            "-fx-alignment:CENTER;" +
                            "-fx-background-radius:20;"
                    );
                } else {
                    setText("● Belum Bayar");
                    setStyle(
                            "-fx-background-color:#FEE2E2;" +
                            "-fx-text-fill:#DC2626;" +
                            "-fx-font-weight:bold;" +
                            "-fx-alignment:CENTER;" +
                            "-fx-background-radius:20;"
                    );
                }
            }
        });
    }

    private void setupKolomAksi() {
        colAksi.setStyle("-fx-alignment: CENTER;");

        colAksi.setCellFactory(col -> new TableCell<TransaksiItem, Void>() {

            private final Button btnStatus = new Button("Status");
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox container = new HBox(6, btnStatus, btnEdit, btnHapus);

            {
                container.setAlignment(Pos.CENTER);

                btnStatus.setStyle("-fx-background-color:#fef3c7; -fx-text-fill:#92400e; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnEdit.setStyle("-fx-background-color:#6495ed; -fx-text-fill:white; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnHapus.setStyle("-fx-background-color:#fee2e2; -fx-text-fill:#dc2626; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10;");

                btnStatus.setOnAction(event -> {
                    TransaksiItem item = getTableView().getItems().get(getIndex());
                    detail(item);
                });

                btnEdit.setOnAction(event -> {
                    TransaksiItem item = getTableView().getItems().get(getIndex());
                    edit(item);
                });

                btnHapus.setOnAction(event -> {
                    TransaksiItem item = getTableView().getItems().get(getIndex());
                    hapus(item);
                });
            }

            @Override
            protected void updateItem(Void value, boolean empty) {
                super.updateItem(value, empty);
                setGraphic(empty ? null : container);
            }
    });
}
          

            private void detail(TransaksiItem item){
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

            private void edit(TransaksiItem item){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Edit");
            alert.setContentText("Fitur Edit akan kita buat pada tahap berikutnya.");
            alert.showAndWait();

            }

            private void hapus(TransaksiItem item){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Hapus Transaksi");
            alert.setContentText("Yakin ingin menghapus transaksi ini?");
            alert.showAndWait().ifPresent(btn -> {

                if(btn == ButtonType.OK){
            Data.getDaftarTransaksi().remove(item);

            }
    });
}

    @FXML
    private void bukaTambahTransaksi() {
       try {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("TambahTransaksi.fxml"));

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Tambah Transaksi");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        tabelTransaksi.refresh();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}