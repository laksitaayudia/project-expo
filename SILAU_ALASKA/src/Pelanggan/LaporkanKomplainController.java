package Pelanggan;

import Karyawan.Data;
import Karyawan.KomplainItem;
import Karyawan.PesananItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LaporkanKomplainController {

    @FXML private ComboBox<String> cbPesanan;
    @FXML private ComboBox<String> cbJenisKomplain;
    @FXML private TextArea txtDeskripsi;
    @FXML private VBox vboxRiwayat;

    private String namaPelanggan = "Budi Santoso";

    @FXML
    public void initialize() {
        cbJenisKomplain.setItems(FXCollections.observableArrayList(
                "Baju tertukar",
                "Baju rusak",
                "Baju hilang",
                "Cucian kurang bersih",
                "Lainnya"
        ));

        refreshData();
    }

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
        refreshData();
    }

    public void refreshData() {
        cbPesanan.getItems().clear();
        for (PesananItem p : Data.getDaftarPesanan()) {
            if (p.getPelanggan().equalsIgnoreCase(namaPelanggan)) {
                cbPesanan.getItems().add("PSN00" + p.getId() + " - " + p.getLayanan());
            }
        }
        if (cbPesanan.getItems().isEmpty()) {
            cbPesanan.getItems().add("Tidak ada pesanan aktif");
        }

        renderRiwayat();
    }

    private void renderRiwayat() {
        vboxRiwayat.getChildren().clear();

        for (KomplainItem k : Data.getDaftarKomplain()) {
            VBox card = new VBox(10);
            card.setPadding(new Insets(16));
            card.setStyle("-fx-background-color:white; -fx-background-radius:12; -fx-effect:dropshadow(gaussian,rgba(0,0,0,0.04),8,0,0,2); -fx-border-color:#f1f5f9; -fx-border-width:1; -fx-border-radius:12;");

            HBox header = new HBox();
            header.setAlignment(Pos.CENTER_LEFT);

            Label lblTitle = new Label(k.getJenisKomplain());
            lblTitle.setStyle("-fx-font-size:14; -fx-font-weight:bold; -fx-text-fill:#1f2937;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label lblStatus = new Label(k.getStatus());
            if ("Selesai".equalsIgnoreCase(k.getStatus())) {
                lblStatus.setStyle("-fx-background-color:#dcfce7; -fx-text-fill:#15803d; -fx-font-size:11; -fx-font-weight:bold; -fx-padding:4 10; -fx-background-radius:10;");
            } else {
                lblStatus.setStyle("-fx-background-color:#fef3c7; -fx-text-fill:#d97706; -fx-font-size:11; -fx-font-weight:bold; -fx-padding:4 10; -fx-background-radius:10;");
            }

            header.getChildren().addAll(lblTitle, spacer, lblStatus);

            Label lblDesc = new Label(k.getDeskripsi());
            lblDesc.setStyle("-fx-font-size:12; -fx-text-fill:#6b7280;");
            lblDesc.setWrapText(true);

            card.getChildren().addAll(header, lblDesc);

            if ("Selesai".equalsIgnoreCase(k.getStatus()) && k.getSolusi() != null && !k.getSolusi().trim().isEmpty() && !"-".equals(k.getSolusi())) {
                VBox solutionBox = new VBox();
                solutionBox.setPadding(new Insets(10, 12, 10, 12));
                solutionBox.setStyle("-fx-background-color:#f0fdf4; -fx-border-color:#bbf7d0; -fx-border-width:1; -fx-border-radius:8; -fx-background-radius:8;");

                Label lblSolusi = new Label("Solusi: " + k.getSolusi());
                lblSolusi.setStyle("-fx-font-size:12; -fx-text-fill:#166534; -fx-font-weight:bold;");
                lblSolusi.setWrapText(true);

                solutionBox.getChildren().add(lblSolusi);
                card.getChildren().add(solutionBox);
            }

            vboxRiwayat.getChildren().add(card);
        }
    }

    @FXML
    private void kirimKomplain() {
        String pesananVal = cbPesanan.getValue();
        String jenisVal = cbJenisKomplain.getValue();
        String deskripsi = txtDeskripsi.getText();

        if (pesananVal == null || pesananVal.startsWith("Tidak ada") || jenisVal == null || deskripsi == null || deskripsi.trim().isEmpty()) {
            showAlert("Semua kolom komplain wajib diisi.");
            return;
        }

        String idPesanan = pesananVal.split(" - ")[0];

        int newId = Data.idKomplainBerikutnya();
        KomplainItem newKomplain = new KomplainItem(newId, idPesanan, jenisVal, deskripsi, "Diproses", "-");
        Data.tambahKomplain(newKomplain);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText("Komplain berhasil dilaporkan. Kami akan segera memproses keluhan Anda.");
        alert.showAndWait();

        cbPesanan.getSelectionModel().clearSelection();
        cbJenisKomplain.getSelectionModel().clearSelection();
        txtDeskripsi.clear();

        renderRiwayat();
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
