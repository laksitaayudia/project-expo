package Karyawan;

public class PesananItem {

    private int id;
    private String pelanggan;
    private String layanan;
    private double berat;
    private int biaya;
    private String status;

    public PesananItem(int id, String pelanggan, String layanan, double berat, int biaya, String status) {
        this.id = id;
        this.pelanggan = pelanggan;
        this.layanan = layanan;
        this.berat = berat;
        this.biaya = biaya;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public int getBiaya() {
        return biaya;
    }

    public void setBiaya(int biaya) {
        this.biaya = biaya;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}