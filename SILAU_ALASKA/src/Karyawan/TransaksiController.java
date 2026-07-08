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
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusBayar"));
        colMetode.setCellValueFactory(new PropertyValueFactory<>("metodeBayar"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalBayar"));
        tabelTransaksi.setItems(Data.getDaftarTransaksi());
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