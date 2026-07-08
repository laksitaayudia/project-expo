package Karyawan;

public class TransaksiItem {

    private String idTransaksi;
    private String idPesanan;
    private String pelanggan;
    private String total;
    private String statusBayar;
    private String metodeBayar;
    private String tanggalBayar;

    public TransaksiItem(String idTransaksi,
                         String idPesanan,
                         String pelanggan,
                         String total,
                         String statusBayar,
                         String metodeBayar,
                         String tanggalBayar) {

        this.idTransaksi = idTransaksi;
        this.idPesanan = idPesanan;
        this.pelanggan = pelanggan;
        this.total = total;
        this.statusBayar = statusBayar;
        this.metodeBayar = metodeBayar;
        this.tanggalBayar = tanggalBayar;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(String idPesanan) {
        this.idPesanan = idPesanan;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatusBayar() {
        return statusBayar;
    }

    public void setStatusBayar(String statusBayar) {
        this.statusBayar = statusBayar;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }

    public String getTanggalBayar() {
        return tanggalBayar;
    }

    public void setTanggalBayar(String tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }

}