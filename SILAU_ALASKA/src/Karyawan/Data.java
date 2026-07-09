package Karyawan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Registrasi.PelangganRegister;
import Owner.PromoItem;
import Owner.PengeluaranItem;

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

    // ── Owner: Data Pelanggan Terdaftar ──
    private static final ObservableList<PelangganRegister> daftarPelanggan =
        FXCollections.observableArrayList(
            new PelangganRegister("Budi Santoso", "081234567890", "budi@gmail.com", "Jl. Mawar No. 12", "budi", "budi123"),
            new PelangganRegister("Siti Aminah", "085678901234", "siti@yahoo.com", "Jl. Melati No. 5", "siti", "siti123")
        );

    public static ObservableList<PelangganRegister> getDaftarPelanggan() {
        return daftarPelanggan;
    }

    public static void tambahPelanggan(PelangganRegister pelanggan) {
        daftarPelanggan.add(pelanggan);
    }

    // ── Owner: Kelola Promo ──
    private static final ObservableList<PromoItem> daftarPromo =
        FXCollections.observableArrayList(
            new PromoItem("SILAUCEPAT", 10, "Persentase", 30000, "Aktif"),
            new PromoItem("HEMATLAUNDRY", 5000, "Nominal", 25000, "Aktif")
        );

    public static ObservableList<PromoItem> getDaftarPromo() {
        return daftarPromo;
    }

    public static void tambahPromo(PromoItem promo) {
        daftarPromo.add(promo);
    }

    public static void hapusPromo(String kode) {
        daftarPromo.removeIf(p -> p.getKode().equalsIgnoreCase(kode));
    }

    public static void editPromo(String kodeAsli, PromoItem updated) {
        for (PromoItem p : daftarPromo) {
            if (p.getKode().equalsIgnoreCase(kodeAsli)) {
                p.setKode(updated.getKode());
                p.setDiskon(updated.getDiskon());
                p.setTipe(updated.getTipe());
                p.setMinBelanja(updated.getMinBelanja());
                p.setStatus(updated.getStatus());
                break;
            }
        }
    }

    // ── Owner: Pengeluaran ──
    private static final ObservableList<PengeluaranItem> daftarPengeluaran =
        FXCollections.observableArrayList(
            new PengeluaranItem(1, "2025-05-12", "Bahan Baku", 150000, "Pembelian deterjen"),
            new PengeluaranItem(2, "2025-05-14", "Operasional", 75000, "Perbaikan pipa air")
        );

    public static ObservableList<PengeluaranItem> getDaftarPengeluaran() {
        return daftarPengeluaran;
    }

    public static void tambahPengeluaran(PengeluaranItem pengeluaran) {
        daftarPengeluaran.add(pengeluaran);
    }

    public static void hapusPengeluaran(int id) {
        daftarPengeluaran.removeIf(p -> p.getId() == id);
    }

    public static int idPengeluaranBerikutnya() {
        return daftarPengeluaran.stream().mapToInt(PengeluaranItem::getId).max().orElse(0) + 1;
    }
}
