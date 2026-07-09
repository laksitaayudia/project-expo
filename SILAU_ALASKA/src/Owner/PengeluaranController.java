package Owner;

import Karyawan.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PengeluaranController implements Initializable {

    @FXML
    private ComboBox<String> cbKategori;

    @FXML
    private TextField txtJumlah;

    @FXML
    private TextField txtKeterangan;

    @FXML
    private TableView<PengeluaranItem> tabelPengeluaran;

    @FXML
    private TableColumn<PengeluaranItem, Integer> colId;

    @FXML
    private TableColumn<PengeluaranItem, String> colTanggal;

    @FXML
    private TableColumn<PengeluaranItem, String> colKategori;

    @FXML
    private TableColumn<PengeluaranItem, Integer> colJumlah;

    @FXML
    private TableColumn<PengeluaranItem, String> colKeterangan;

    @FXML
    private TableColumn<PengeluaranItem, Void> colAksi;

    private Runnable onDataChanged;

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));

        setupFormatKolom();
        setupKolomAksi();

        tabelPengeluaran.setItems(Data.getDaftarPengeluaran());

        cbKategori.setValue("Bahan Baku");
    }

    private void setupFormatKolom() {
        colId.setCellFactory(col -> new TableCell<PengeluaranItem, Integer>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "#" + value);
            }
        });

        colJumlah.setCellFactory(col -> new TableCell<PengeluaranItem, Integer>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %,d", value).replace(",", "."));
                }
            }
        });
    }

    private void setupKolomAksi() {
        colAksi.setStyle("-fx-alignment: CENTER;");
        colAksi.setCellFactory(col -> new TableCell<PengeluaranItem, Void>() {
            private final Button btnHapus = new Button("Hapus");
            private final HBox container = new HBox(btnHapus);

            {
                container.setAlignment(Pos.CENTER);
                btnHapus.setStyle("-fx-background-color:#fee2e2; -fx-text-fill:#dc2626; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10; -fx-font-weight:bold;");
                btnHapus.setOnAction(event -> {
                    PengeluaranItem item = null;
                    if (getTableRow() != null) {
                        item = getTableRow().getItem();
                    }
                    if (item != null) {
                        Data.hapusPengeluaran(item.getId());
                        notifyDataChanged();
                    }
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
    private void tambahPengeluaran(ActionEvent event) {
        String kategori = cbKategori.getValue();
        String jumlahStr = txtJumlah.getText().trim();
        String keterangan = txtKeterangan.getText().trim();

        if (kategori == null || jumlahStr.isEmpty() || keterangan.isEmpty()) {
            showAlert("Semua kolom input wajib diisi!");
            return;
        }

        try {
            int jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) {
                showAlert("Jumlah pengeluaran harus lebih besar dari 0!");
                return;
            }

            int id = Data.idPengeluaranBerikutnya();
            String tanggal = LocalDate.now().toString();

            PengeluaranItem baru = new PengeluaranItem(id, tanggal, kategori, jumlah, keterangan);
            Data.tambahPengeluaran(baru);

            txtJumlah.clear();
            txtKeterangan.clear();
            cbKategori.setValue("Bahan Baku");

            notifyDataChanged();

        } catch (NumberFormatException e) {
            showAlert("Jumlah pengeluaran harus berupa angka!");
        }
    }

    private void notifyDataChanged() {
        tabelPengeluaran.refresh();
        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
