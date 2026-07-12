package Pelanggan;

import Karyawan.Data;
import Karyawan.PesananItem;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BuatPesananController {

    @FXML private Button btnReguler;
    @FXML private Label lblRegulerTitle;
    @FXML private Label lblRegulerSub;

    @FXML private Button btnEkspres;
    @FXML private Label lblEkspresTitle;
    @FXML private Label lblEkspresSub;

    @FXML private Button btnPagi;
    @FXML private Button btnSiang;
    @FXML private Button btnSore;

    @FXML private TextField txtBeratGram;
    @FXML private TextField txtCatatan;

    private String selectedLayanan = "Reguler";
    private String selectedWaktu = "Pagi"; 
    private String namaPelanggan = "Budi Santoso";

    private Runnable onSimpanBerhasil;

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    public void setNamaPelanggan(String nama) {
        this.namaPelanggan = nama;
    }

    @FXML
    public void initialize() {
        updateLayananStyles();
        updateWaktuStyles();
    }

    @FXML
    private void selectReguler() {
        selectedLayanan = "Reguler";
        updateLayananStyles();
    }

    @FXML
    private void selectEkspres() {
        selectedLayanan = "Ekspres";
        updateLayananStyles();
    }

    @FXML
    private void selectPagi() {
        selectedWaktu = "Pagi";
        updateWaktuStyles();
    }

    @FXML
    private void selectSiang() {
        selectedWaktu = "Siang";
        updateWaktuStyles();
    }

    @FXML
    private void selectSore() {
        selectedWaktu = "Sore";
        updateWaktuStyles();
    }

    private void updateLayananStyles() {
        if ("Reguler".equals(selectedLayanan)) {
            btnReguler.setStyle("-fx-background-color:#f8fafc; -fx-border-color:#1e5af6; -fx-border-width:2; -fx-border-radius:10; -fx-background-radius:10; -fx-cursor:hand;");
            lblRegulerTitle.setStyle("-fx-font-size:14; -fx-font-weight:bold; -fx-text-fill:#1e5af6;");
            lblRegulerSub.setStyle("-fx-font-size:11; -fx-text-fill:#1e5af6;");

            btnEkspres.setStyle("-fx-background-color:white; -fx-border-color:#e2e8f0; -fx-border-width:1; -fx-border-radius:10; -fx-background-radius:10; -fx-cursor:hand;");
            lblEkspresTitle.setStyle("-fx-font-size:14; -fx-font-weight:bold; -fx-text-fill:#475569;");
            lblEkspresSub.setStyle("-fx-font-size:11; -fx-text-fill:#64748b;");
        } else {
            btnReguler.setStyle("-fx-background-color:white; -fx-border-color:#e2e8f0; -fx-border-width:1; -fx-border-radius:10; -fx-background-radius:10; -fx-cursor:hand;");
            lblRegulerTitle.setStyle("-fx-font-size:14; -fx-font-weight:bold; -fx-text-fill:#475569;");
            lblRegulerSub.setStyle("-fx-font-size:11; -fx-text-fill:#64748b;");

            btnEkspres.setStyle("-fx-background-color:#f8fafc; -fx-border-color:#1e5af6; -fx-border-width:2; -fx-border-radius:10; -fx-background-radius:10; -fx-cursor:hand;");
            lblEkspresTitle.setStyle("-fx-font-size:14; -fx-font-weight:bold; -fx-text-fill:#1e5af6;");
            lblEkspresSub.setStyle("-fx-font-size:11; -fx-text-fill:#1e5af6;");
        }
    }

    private void updateWaktuStyles() {
        String active = "-fx-background-color:#f8fafc; -fx-border-color:#1e5af6; -fx-border-width:2; -fx-border-radius:8; -fx-background-radius:8; -fx-font-weight:bold; -fx-text-fill:#1e5af6; -fx-cursor:hand;";
        String inactive = "-fx-background-color:white; -fx-border-color:#e2e8f0; -fx-border-width:1; -fx-border-radius:8; -fx-background-radius:8; -fx-text-fill:#475569; -fx-cursor:hand;";

        btnPagi.setStyle("Pagi".equals(selectedWaktu) ? active : inactive);
        btnSiang.setStyle("Siang".equals(selectedWaktu) ? active : inactive);
        btnSore.setStyle("Sore".equals(selectedWaktu) ? active : inactive);
    }

    @FXML
    private void submitPesanan() {
        String beratText = txtBeratGram.getText();
        if (beratText == null || beratText.trim().isEmpty()) {
            showAlert("Berat cucian wajib diisi.");
            return;
        }

        double beratGram;
        try {
            beratGram = Double.parseDouble(beratText);
        } catch (NumberFormatException e) {
            showAlert("Berat cucian harus berupa angka.");
            return;
        }

        if (beratGram <= 0) {
            showAlert("Berat cucian harus lebih dari 0.");
            return;
        }

        double beratKg = beratGram / 1000.0;
        int ratePerKg = "Reguler".equals(selectedLayanan) ? 10000 : 15000;
        int biaya = (int) (beratKg * ratePerKg);

        String namaLayanan = selectedLayanan + " — " + selectedWaktu;

        int idBaru = Data.idBerikutnya();
        PesananItem pesananBaru = new PesananItem(idBaru, namaPelanggan, namaLayanan, beratKg, biaya, "MENUNGGU");
        Data.tambahPesanan(pesananBaru);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText("Pesanan berhasil dibuat!\nLayanan: " + namaLayanan + "\nBerat: " + beratKg + " kg\nBiaya: Rp " + String.format("%,d", biaya).replace(",", "."));
        alert.showAndWait();

        txtBeratGram.clear();
        txtCatatan.clear();

        if (onSimpanBerhasil != null) {
            onSimpanBerhasil.run();
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
