package Owner;

import Karyawan.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class KelolaPromoController implements Initializable {

    @FXML private TextField txtKode;
    @FXML private TextField txtDiskon;
    @FXML private ComboBox<String> cbTipe;
    @FXML private TextField txtMinBelanja;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TableView<PromoItem> tabelPromo;
    @FXML private TableColumn<PromoItem, String> colKode;
    @FXML private TableColumn<PromoItem, Integer> colDiskon;
    @FXML private TableColumn<PromoItem, String> colTipe;
    @FXML private TableColumn<PromoItem, Integer> colMinBelanja;
    @FXML private TableColumn<PromoItem, String> colStatus;
    @FXML private TableColumn<PromoItem, Void> colAksi;
    @FXML private Label lblFormTitle;
    @FXML private Button btnSimpan;
    @FXML private Button btnBatal;

    private String kodeEditing = null;

    private Runnable onDataChanged;

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKode.setCellValueFactory(new PropertyValueFactory<>("kode"));
        colDiskon.setCellValueFactory(new PropertyValueFactory<>("diskon"));
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        colMinBelanja.setCellValueFactory(new PropertyValueFactory<>("minBelanja"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        setupFormatKolom();
        setupKolomAksi();

        tabelPromo.setItems(Data.getDaftarPromo());

        cbTipe.setValue("Persentase");
        cbStatus.setValue("Aktif");

        if (btnBatal != null) {
            btnBatal.setVisible(false);
            btnBatal.setManaged(false);
        }
    }


    private void setupFormatKolom() {
        colDiskon.setCellFactory(col -> new TableCell<PromoItem, Integer>() {
            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    PromoItem promo = getTableRow() != null ? getTableRow().getItem() : null;
                    if (promo != null) {
                        if ("Persentase".equalsIgnoreCase(promo.getTipe())) {
                            setText(value + "%");
                        } else {
                            setText(String.format("Rp %,d", value).replace(",", "."));
                        }
                    } else {
                        setText(value.toString());
                    }
                }
            }
        });

        colMinBelanja.setCellFactory(col -> new TableCell<PromoItem, Integer>() {
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
        colAksi.setCellFactory(col -> new TableCell<PromoItem, Void>() {
            private final Button btnEdit  = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");
            private final HBox container  = new HBox(6, btnEdit, btnHapus);

            {
                container.setAlignment(Pos.CENTER);

                btnEdit.setStyle(
                        "-fx-background-color:#dbeafe; -fx-text-fill:#1d4ed8; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10; -fx-font-weight:bold;");

                btnHapus.setStyle(
                        "-fx-background-color:#fee2e2; -fx-text-fill:#dc2626; -fx-font-size:11; " +
                        "-fx-background-radius:6; -fx-cursor:hand; -fx-padding:4 10; -fx-font-weight:bold;");

                btnEdit.setOnAction(event -> {
                    PromoItem item = getTableRow() != null ? getTableRow().getItem() : null;
                    if (item != null) muatFormEdit(item);
                });

                btnHapus.setOnAction(event -> {
                    PromoItem item = getTableRow() != null ? getTableRow().getItem() : null;
                    if (item != null) {
                        if (kodeEditing != null && kodeEditing.equalsIgnoreCase(item.getKode())) {
                            resetForm();
                        }
                        Data.hapusPromo(item.getKode());
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

    private void muatFormEdit(PromoItem item) {
        kodeEditing = item.getKode();
        txtKode.setText(item.getKode());
        txtDiskon.setText(String.valueOf(item.getDiskon()));
        cbTipe.setValue(item.getTipe());
        txtMinBelanja.setText(String.valueOf(item.getMinBelanja()));
        cbStatus.setValue(item.getStatus());

        if (lblFormTitle != null) lblFormTitle.setText("Edit Promo");
        if (btnSimpan  != null)  btnSimpan.setText("Update Promo");
        if (btnBatal   != null) {
            btnBatal.setVisible(true);
            btnBatal.setManaged(true);
        }
    }

    // ── Batal Edit ──

    @FXML
    private void batalEdit() {
        resetForm();
    }

    // ── Simpan / Update Promo ──

    @FXML
    private void tambahPromo(ActionEvent event) {
        String kode        = txtKode.getText().trim().toUpperCase();
        String diskonStr   = txtDiskon.getText().trim();
        String tipe        = cbTipe.getValue();
        String minStr      = txtMinBelanja.getText().trim();
        String status      = cbStatus.getValue();

        if (kode.isEmpty() || diskonStr.isEmpty() || minStr.isEmpty() || tipe == null || status == null) {
            showAlert("Semua kolom input wajib diisi!");
            return;
        }

        // Cek duplikasi: boleh sama jika ini mode edit dan kodenya tidak berubah
        boolean ada = Data.getDaftarPromo().stream().anyMatch(p -> p.getKode().equalsIgnoreCase(kode));
        if (ada && (kodeEditing == null || !kode.equalsIgnoreCase(kodeEditing))) {
            showAlert("Kode promo '" + kode + "' sudah digunakan!");
            return;
        }

        try {
            int diskon    = Integer.parseInt(diskonStr);
            int minBelanja = Integer.parseInt(minStr);

            if (diskon <= 0 || minBelanja < 0) {
                showAlert("Diskon harus positif dan Minimal Belanja tidak boleh negatif!");
                return;
            }
            if ("Persentase".equalsIgnoreCase(tipe) && diskon > 100) {
                showAlert("Diskon persentase tidak boleh lebih dari 100%!");
                return;
            }

            PromoItem baru = new PromoItem(kode, diskon, tipe, minBelanja, status);

            if (kodeEditing != null) {
                Data.editPromo(kodeEditing, baru);
            } else {
                Data.tambahPromo(baru);
            }

            resetForm();
            notifyDataChanged();

        } catch (NumberFormatException e) {
            showAlert("Diskon dan Minimal Belanja harus berupa angka integer!");
        }
    }

    // ── Reset form ke mode Tambah ──

    private void resetForm() {
        kodeEditing = null;
        txtKode.clear();
        txtDiskon.clear();
        txtMinBelanja.clear();
        cbTipe.setValue("Persentase");
        cbStatus.setValue("Aktif");

        if (lblFormTitle != null) lblFormTitle.setText("Tambah Promo Baru");
        if (btnSimpan   != null)  btnSimpan.setText("Simpan Promo");
        if (btnBatal    != null) {
            btnBatal.setVisible(false);
            btnBatal.setManaged(false);
        }
    }

    // ── Notify & Alert helpers ──

    private void notifyDataChanged() {
        tabelPromo.refresh();
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
