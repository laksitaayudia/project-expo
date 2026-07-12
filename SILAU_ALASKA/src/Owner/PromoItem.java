package Owner;

public class PromoItem {
    private String kode;
    private int diskon;
    private String tipe;
    private int minBelanja;
    private String status;

    public PromoItem(String kode, int diskon, String tipe, int minBelanja, String status) {
        this.kode = kode;
        this.diskon = diskon;
        this.tipe = tipe;
        this.minBelanja = minBelanja;
        this.status = status;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public int getDiskon() {
        return diskon;
    }

    public void setDiskon(int diskon) {
        this.diskon = diskon;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getMinBelanja() {
        return minBelanja;
    }

    public void setMinBelanja(int minBelanja) {
        this.minBelanja = minBelanja;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
