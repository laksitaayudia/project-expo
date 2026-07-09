package Owner;

public class PengeluaranItem {
    private int id;
    private String tanggal;
    private String kategori;
    private int jumlah;
    private String keterangan;

    public PengeluaranItem(int id, String tanggal, String kategori, int jumlah, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
