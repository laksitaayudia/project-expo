package Karyawan;

public class KomplainItem {

    private int id;
    private String idPesanan;
    private String jenisKomplain;
    private String deskripsi;
    private String status;
    private String solusi;

    public KomplainItem(int id,
                        String idPesanan,
                        String jenisKomplain,
                        String deskripsi,
                        String status,
                        String solusi) {

        this.id = id;
        this.idPesanan = idPesanan;
        this.jenisKomplain = jenisKomplain;
        this.deskripsi = deskripsi;
        this.status = status;
        this.solusi = solusi;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdPesanan() { return idPesanan; }
    public void setIdPesanan(String idPesanan) { this.idPesanan = idPesanan; }

    public String getJenisKomplain() { return jenisKomplain; }
    public void setJenisKomplain(String jenisKomplain) { this.jenisKomplain = jenisKomplain; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSolusi() { return solusi; }
    public void setSolusi(String solusi) { this.solusi = solusi; }
}