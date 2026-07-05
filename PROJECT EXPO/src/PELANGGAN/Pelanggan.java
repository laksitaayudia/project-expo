import javax.swing.JOptionPane;

public class Pelanggan {

    private String cbRole;
    private String txtUsername;
    private String txtPassword;

    // setters to simulate injected fields
    public void setCbRole(String role){ this.cbRole = role; }
    public void setTxtUsername(String u){ this.txtUsername = u; }
    public void setTxtPassword(String p){ this.txtPassword = p; }

    private void showAlert(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public void login() {
        String role = cbRole;
        String username = txtUsername == null ? "" : txtUsername;
        String password = txtPassword == null ? "" : txtPassword;

        if(role == null){
            showAlert("Pilih jenis login!");
            return;
        }

        if(username.isEmpty() || password.isEmpty()){
            showAlert("Username dan Password harus diisi!");
            return;
        }

        switch(role){
            case "Pelanggan":
                // buka Dashboard Pelanggan
                break;
            case "Karyawan":
                // buka Dashboard Karyawan
                break;
            case "Owner":
                // buka Dashboard Owner
                break;
            default:
                showAlert("Role tidak dikenali");
        }
    }
}
