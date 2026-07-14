package Karyawan;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PesananItem {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty pelanggan;
    private final SimpleStringProperty layanan;
    private final SimpleStringProperty tanggal;
    private final SimpleDoubleProperty berat;
    private final SimpleIntegerProperty biaya;
    private final SimpleStringProperty status;

    public PesananItem(int id, String pelanggan, String layanan, double berat, int biaya, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.pelanggan = new SimpleStringProperty(pelanggan);
        this.layanan = new SimpleStringProperty(layanan);
        this.tanggal = new SimpleStringProperty("");
        this.berat = new SimpleDoubleProperty(berat);
        this.biaya = new SimpleIntegerProperty(biaya);
        this.status = new SimpleStringProperty(status);
    }

    public PesananItem(int id, String pelanggan, String layanan, String tanggal, double berat, int biaya, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.pelanggan = new SimpleStringProperty(pelanggan);
        this.layanan = new SimpleStringProperty(layanan);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.berat = new SimpleDoubleProperty(berat);
        this.biaya = new SimpleIntegerProperty(biaya);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() {
        return id.get();
    }

    public String getPelanggan() {
        return pelanggan.get();
    }

    public String getLayanan() {
        return layanan.get();
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public double getBerat() {
        return berat.get();
    }

    public int getBiaya() {
        return biaya.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan.set(pelanggan);
    }

    public void setLayanan(String layanan) {
        this.layanan.set(layanan);
    }

    public void setTanggal(String tanggal) {
        this.tanggal.set(tanggal);
    }

    public void setBerat(double berat) {
        this.berat.set(berat);
    }

    public void setBiaya(int biaya) {
        this.biaya.set(biaya);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty pelangganProperty() {
        return pelanggan;
    }

    public SimpleStringProperty layananProperty() {
        return layanan;
    }

    public SimpleStringProperty tanggalProperty() {
        return tanggal;
    }

    public SimpleDoubleProperty beratProperty() {
        return berat;
    }

    public SimpleIntegerProperty biayaProperty() {
        return biaya;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}