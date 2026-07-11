package Pelanggan;

import Karyawan.KomplainItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetailKomplainPelangganController {

    @FXML private Label lblId;
    @FXML private Label lblIdPesanan;
    @FXML private Label lblJenis;
    @FXML private Label lblDeskripsi;
    @FXML private Label lblSolusi;
    @FXML private Label lblStatus;
    @FXML private VBox boxSolusi;

    public void isiData(KomplainItem item) {
        lblId.setText("#" + item.getId());
        lblIdPesanan.setText(item.getIdPesanan());
        lblJenis.setText(item.getJenisKomplain());
        lblDeskripsi.setText(item.getDeskripsi());

        String status = item.getStatus() == null ? "" : item.getStatus();
        lblStatus.setText(status);
        lblStatus.setStyle(lblStatus.getStyle() + gayaBadge(status));

        String solusi = item.getSolusi();
        if (solusi == null || solusi.isEmpty()) {
            boxSolusi.setVisible(false);
            boxSolusi.setManaged(false);
        } else {
            lblSolusi.setText(solusi);
        }
    }

    private String gayaBadge(String status) {
        switch (status) {
            case "Selesai":
                return "-fx-background-color:#dcfce7; -fx-text-fill:#16a34a;";
            case "Ditolak":
                return "-fx-background-color:#fee2e2; -fx-text-fill:#dc2626;";
            default:
                return "-fx-background-color:#fef3c7; -fx-text-fill:#92400e;";
        }
    }

    @FXML
    private void tutup(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}