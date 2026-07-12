package Pelanggan;

import Karyawan.Data;
import Owner.PromoItem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PromosPelangganController implements Initializable {

    @FXML private FlowPane flowPromo;
    @FXML private VBox emptyState;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshPromo();
    }

    public void refreshPromo() {
        flowPromo.getChildren().clear();

        List<PromoItem> promoAktif = Data.getDaftarPromo().stream()
                .filter(p -> "Aktif".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());

        if (promoAktif.isEmpty()) {
            emptyState.setVisible(true);
            emptyState.setManaged(true);
            flowPromo.setVisible(false);
            flowPromo.setManaged(false);
        } else {
            emptyState.setVisible(false);
            emptyState.setManaged(false);
            flowPromo.setVisible(true);
            flowPromo.setManaged(true);

            for (PromoItem promo : promoAktif) {
                flowPromo.getChildren().add(buatKartuPromo(promo));
            }
        }
    }

    private VBox buatKartuPromo(PromoItem promo) {
        Label lblKode = new Label(promo.getKode());
        lblKode.setStyle(
                "-fx-font-size:20; -fx-font-weight:bold; -fx-text-fill:white;" +
                "-fx-background-color:#6495ed; -fx-background-radius:8;" +
                "-fx-padding:10 18;");
        lblKode.setMaxWidth(Double.MAX_VALUE);
        lblKode.setAlignment(Pos.CENTER);

        String diskonTeks;
        if ("Persentase".equalsIgnoreCase(promo.getTipe())) {
            diskonTeks = promo.getDiskon() + "% OFF";
        } else {
            diskonTeks = "Hemat Rp " + String.format("%,d", promo.getDiskon()).replace(",", ".");
        }
        Label lblDiskon = new Label(diskonTeks);
        lblDiskon.setStyle("-fx-font-size:28; -fx-font-weight:bold; -fx-text-fill:#111827;");
        lblDiskon.setAlignment(Pos.CENTER);
        lblDiskon.setMaxWidth(Double.MAX_VALUE);

        Label lblTipe = new Label(promo.getTipe());
        lblTipe.setStyle(
                "-fx-font-size:11; -fx-font-weight:bold; -fx-text-fill:#6b7280;" +
                "-fx-background-color:#f3f4f6; -fx-background-radius:20; -fx-padding:3 10;");

        HBox boxTipe = new HBox(lblTipe);
        boxTipe.setAlignment(Pos.CENTER);

        Label dashed = new Label("- - - - - - - - - - - - - - - - -");
        dashed.setStyle("-fx-text-fill:#d1d5db; -fx-font-size:10;");
        dashed.setMaxWidth(Double.MAX_VALUE);
        dashed.setAlignment(Pos.CENTER);

        String minTeks = "Min. belanja: Rp " +
                String.format("%,d", promo.getMinBelanja()).replace(",", ".");
        Label lblMin = new Label(minTeks);
        lblMin.setStyle("-fx-font-size:12; -fx-text-fill:#6b7280;");
        lblMin.setMaxWidth(Double.MAX_VALUE);
        lblMin.setAlignment(Pos.CENTER);

        Label lblStatus = new Label("✓ " + promo.getStatus());
        lblStatus.setStyle(
                "-fx-font-size:12; -fx-font-weight:bold; -fx-text-fill:#16a34a;" +
                "-fx-background-color:#dcfce7; -fx-background-radius:20; -fx-padding:4 12;");

        HBox boxStatus = new HBox(lblStatus);
        boxStatus.setAlignment(Pos.CENTER);

        VBox kartu = new VBox(12, lblKode, lblDiskon, boxTipe, dashed, lblMin, boxStatus);
        kartu.setPrefWidth(260);
        kartu.setMaxWidth(260);
        kartu.setAlignment(Pos.CENTER);
        kartu.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),14,0,0,4);" +
                "-fx-padding:20;" +
                "-fx-cursor:hand;");

        kartu.setOnMouseEntered(e -> kartu.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-effect:dropshadow(gaussian,rgba(100,149,237,0.25),20,0,0,6);" +
                "-fx-padding:20;" +
                "-fx-cursor:hand;"));
        kartu.setOnMouseExited(e -> kartu.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),14,0,0,4);" +
                "-fx-padding:20;" +
                "-fx-cursor:hand;"));

        return kartu;
    }
}
