package Karyawan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {
    private static final ObservableList<PesananItem> daftarPesanan = FXCollections.observableArrayList(
        new PesananItem(1, "Budi Santoso", "Reguler — Pagi", 3.5, 35000, "MENUNGGU"),
        new PesananItem(2, "Siti Aminah", "Ekspres — Sore", 2.0, 50000, "DICUCI"),
        new PesananItem(3, "Andi Wijaya", "Reguler — Sore", 4.2, 42000, "MENUNGGU"),
        new PesananItem(4, "Rina Kartika", "Ekspres — Pagi", 1.8, 45000, "SELESAI"));

    public static ObservableList<PesananItem> getDaftarPesanan() {
        return daftarPesanan;
    }

    public static int idBerikutnya() {
        return daftarPesanan.stream().mapToInt(PesananItem::getId).max().orElse(0) + 1;
    }

    public static void tambahPesanan(PesananItem item) {
        daftarPesanan.add(item);
    }

    public static void hapusPesanan(int id) {
        daftarPesanan.removeIf(item -> item.getId() == id);
    }
}
