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

    private static final ObservableList<TransaksiItem> daftarTransaksi =
                FXCollections.observableArrayList(

                new TransaksiItem(
                        "TRX001",
                        "PSN001",
                        "Budi Santoso",
                        "Rp35.000",
                        "Sudah Bayar",
                        "Cash",
                        "12/05/2025"),

                new TransaksiItem(
                        "TRX002",
                        "PSN002",
                        "Siti Aminah",
                        "Rp50.000",
                        "Belum Bayar",
                        "-",
                        "-"),

                new TransaksiItem(
                        "TRX003",
                        "PSN003",
                        "Andi Wijaya",
                        "Rp42.000",
                        "Sudah Bayar",
                        "Transfer",
                        "13/05/2025"),

                new TransaksiItem(
                        "TRX004",
                        "PSN004",
                        "Rina Kartika",
                        "Rp45.000",
                        "Sudah Bayar",
                        "QRIS",
                        "14/05/2025")
        );

    public static ObservableList<TransaksiItem> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    public static void tambahTransaksi(TransaksiItem transaksi) {
        daftarTransaksi.add(transaksi);
    }

    public static void hapusTransaksi(String idTransaksi) {
        daftarTransaksi.removeIf(
                item -> item.getIdTransaksi().equals(idTransaksi));
    }

    // ── Komplain ──

    private static final ObservableList<KomplainItem> daftarKomplain =
            FXCollections.observableArrayList(

            new KomplainItem(
                    1,
                    "PSN001",
                    "Baju tertukar",
                    "Kemeja biru tidak ada di dalam paket",
                    "Selesai",
                    "Kemeja ditemukan dan dikembalikan")
    );

    public static ObservableList<KomplainItem> getDaftarKomplain() {
        return daftarKomplain;
    }

    public static void tambahKomplain(KomplainItem item) {
        daftarKomplain.add(item);
    }

    public static int idKomplainBerikutnya() {
        return daftarKomplain.stream()
                .mapToInt(KomplainItem::getId)
                .max().orElse(0) + 1;
    }
}
