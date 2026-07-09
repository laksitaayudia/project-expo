package Registrasi;

public class PelangganRegister {
    private String nama;
    private String telepon;
    private String email;
    private String alamat;
    private String username;
    private String password;

    public PelangganRegister(String nama, String telepon, String email, String alamat, String username, String password) {
        this.nama = nama;
        this.telepon = telepon;
        this.email = email;
        this.alamat = alamat;
        this.username = username;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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
}
