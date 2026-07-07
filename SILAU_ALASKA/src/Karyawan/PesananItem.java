package Karyawan;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PesananItem {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty pelanggan;
    private final SimpleStringProperty layanan;
    private final SimpleDoubleProperty berat;
    private final SimpleIntegerProperty biaya;
    private final SimpleStringProperty status;

    public PesananItem(int id, String pelanggan, String layanan, double berat, int biaya, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.pelanggan = new SimpleStringProperty(pelanggan);
        this.layanan = new SimpleStringProperty(layanan);
        this.berat = new SimpleDoubleProperty(berat);
        this.biaya = new SimpleIntegerProperty(biaya);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() { return id.get(); }
    public String getPelanggan() { return pelanggan.get(); }
    public String getLayanan() { return layanan.get(); }
    public double getBerat() { return berat.get(); }
    public int getBiaya() { return biaya.get(); }
    public String getStatus() { return status.get(); }
}