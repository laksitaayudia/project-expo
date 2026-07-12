package Owner;

public class KaryawanItem {
    private String nama;
    private String telepon;
    private String jabatan;
    private String username;
    private String password;
    private String status;

    public KaryawanItem(String nama, String telepon, String jabatan, String username, String password, String status) {
        this.nama = nama;
        this.telepon = telepon;
        this.jabatan = jabatan;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
