package Owner;

import Karyawan.Data;
import Karyawan.TransaksiItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class RekapPendapatanController implements Initializable {

    @FXML
    private Label lblTotalPendapatan;

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
        hitungTotalPendapatan();
    }

    public void refreshTampilan() {
        tabelTransaksi.refresh();
        hitungTotalPendapatan();
    }

    private void hitungTotalPendapatan() {
        int total = 0;
        for (TransaksiItem item : Data.getDaftarTransaksi()) {
            if ("Sudah Bayar".equalsIgnoreCase(item.getStatusBayar())) {
                try {
                    String cleanTotal = item.getTotal().replaceAll("[^0-9]", "");
                    if (!cleanTotal.isEmpty()) {
                        total += Integer.parseInt(cleanTotal);
                    }
                } catch (NumberFormatException e) {
                    // Ignore parsing error
                }
            }
        }
        lblTotalPendapatan.setText(String.format("Rp %,d", total).replace(",", "."));
    }
}
