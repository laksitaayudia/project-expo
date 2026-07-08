package Karyawan;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TambahTransaksiController {

    @FXML
    private ComboBox<String> cbPesanan;

    @FXML
    private TextField txtPelanggan;

    @FXML
    private TextField txtTotal;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private ComboBox<String> cbMetode;

    @FXML
    private DatePicker dpTanggal;

    private Runnable onSimpanBerhasil;

    public void setOnSimpanBerhasil(Runnable callback) {
        this.onSimpanBerhasil = callback;
    }

    @FXML
    public void initialize(){

        cbStatus.setItems(FXCollections.observableArrayList(
                "Sudah Bayar",
                "Belum Bayar"));

        cbMetode.setItems(FXCollections.observableArrayList(
                "Cash",
                "Transfer",
                "QRIS",
                "E-Wallet"));

        for(PesananItem p : Data.getDaftarPesanan()){
            cbPesanan.getItems().add("PSN00" + p.getId());
        }

        cbPesanan.setOnAction(e -> isiData());
        cbStatus.setOnAction(e -> updateStatus());
    }

    private void isiData(){
        int index = cbPesanan.getSelectionModel().getSelectedIndex();
        if(index >= 0){

            PesananItem p = Data.getDaftarPesanan().get(index);
            txtPelanggan.setText(p.getPelanggan());
            txtTotal.setText("Rp" + (int)p.getBiaya());
        }
    }

    private void updateStatus(){
        if(cbStatus.getValue()==null)
            return;

        if(cbStatus.getValue().equals("Belum Bayar")){

            cbMetode.setDisable(true);
            dpTanggal.setDisable(true);
            cbMetode.setValue("-");
            dpTanggal.setValue(null);

        }else{

            cbMetode.setDisable(false);
            dpTanggal.setDisable(false);
            dpTanggal.setValue(LocalDate.now());
        }
    }

    @FXML
    private void simpan(){

        if(cbPesanan.getValue() == null || cbStatus.getValue() == null){
            Alert peringatan = new Alert(Alert.AlertType.WARNING);
            peringatan.setHeaderText(null);
            peringatan.setContentText("Pesanan dan Status Pembayaran wajib diisi.");
            peringatan.showAndWait();
            return;
        }

        String id="TRX00"+(Data.getDaftarTransaksi().size()+1);
        Data.tambahTransaksi(
                new TransaksiItem(
                        id,

                        cbPesanan.getValue(),
                        txtPelanggan.getText(),
                        txtTotal.getText(),
                        cbStatus.getValue(),
                        cbStatus.getValue().equals("Belum Bayar") ? "-" : cbMetode.getValue(),
                        cbStatus.getValue().equals("Belum Bayar") ? "-" : dpTanggal.getValue().toString()
            )
        );

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Transaksi berhasil ditambahkan.");
        alert.showAndWait();

        if(onSimpanBerhasil != null){
            onSimpanBerhasil.run();
        }

        tutup();
    }

    @FXML
    private void batal(){
        tutup();
    }

    private void tutup(){
        Stage stage = (Stage) cbPesanan.getScene().getWindow();
        stage.close();
    }
}