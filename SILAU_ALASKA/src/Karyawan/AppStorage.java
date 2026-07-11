package Karyawan;

import Registrasi.PelangganRegister;
import Owner.PromoItem;
import Owner.PengeluaranItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class AppStorage {

    private static final String SEP = "||";
    private static final String SEP_REGEX = "\\|\\|";

    private static final Path DATA_DIR = Paths.get(System.getProperty("user.home"), "silau_data");

    private static final Path FILE_PESANAN     = DATA_DIR.resolve("pesanan.dat");
    private static final Path FILE_TRANSAKSI   = DATA_DIR.resolve("transaksi.dat");
    private static final Path FILE_KOMPLAIN    = DATA_DIR.resolve("komplain.dat");
    private static final Path FILE_PELANGGAN   = DATA_DIR.resolve("pelanggan.dat");
    private static final Path FILE_PROMO       = DATA_DIR.resolve("promo.dat");
    private static final Path FILE_PENGELUARAN = DATA_DIR.resolve("pengeluaran.dat");

    static {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal membuat direktori data: " + e.getMessage());
        }
    }

    public static void simpanSemua() {
        simpanPesanan();
        simpanTransaksi();
        simpanKomplain();
        simpanPelanggan();
        simpanPromo();
        simpanPengeluaran();
    }

    public static void simpanPesanan() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PESANAN, StandardCharsets.UTF_8)) {
            for (PesananItem p : Data.getDaftarPesanan()) {
                w.write(esc(String.valueOf(p.getId()))    + SEP +
                        esc(p.getPelanggan())              + SEP +
                        esc(p.getLayanan())                + SEP +
                        esc(p.getTanggal())                + SEP +
                        esc(String.valueOf(p.getBerat()))  + SEP +
                        esc(String.valueOf(p.getBiaya()))  + SEP +
                        esc(p.getStatus()));
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan pesanan: " + e.getMessage());
        }
    }

    public static void simpanTransaksi() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_TRANSAKSI, StandardCharsets.UTF_8)) {
            for (TransaksiItem t : Data.getDaftarTransaksi()) {
                w.write(esc(t.getIdTransaksi())  + SEP +
                        esc(t.getIdPesanan())    + SEP +
                        esc(t.getPelanggan())    + SEP +
                        esc(t.getTotal())        + SEP +
                        esc(t.getStatusBayar())  + SEP +
                        esc(t.getMetodeBayar())  + SEP +
                        esc(t.getTanggalBayar()));
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan transaksi: " + e.getMessage());
        }
    }

    public static void simpanKomplain() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_KOMPLAIN, StandardCharsets.UTF_8)) {
            for (KomplainItem k : Data.getDaftarKomplain()) {
                w.write(esc(String.valueOf(k.getId())) + SEP +
                        esc(k.getIdPesanan())           + SEP +
                        esc(k.getJenisKomplain())       + SEP +
                        esc(k.getDeskripsi())           + SEP +
                        esc(k.getStatus())              + SEP +
                        esc(k.getSolusi()));
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan komplain: " + e.getMessage());
        }
    }

    public static void simpanPelanggan() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PELANGGAN, StandardCharsets.UTF_8)) {
            for (PelangganRegister p : Data.getDaftarPelanggan()) {
                w.write(esc(p.getNama())     + SEP +
                        esc(p.getTelepon())  + SEP +
                        esc(p.getEmail())    + SEP +
                        esc(p.getAlamat())   + SEP +
                        esc(p.getUsername()) + SEP +
                        esc(p.getPassword()));
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan pelanggan: " + e.getMessage());
        }
    }

    public static void simpanPromo() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PROMO, StandardCharsets.UTF_8)) {
            for (PromoItem p : Data.getDaftarPromo()) {
                w.write(esc(p.getKode())                      + SEP +
                        esc(String.valueOf(p.getDiskon()))     + SEP +
                        esc(p.getTipe())                      + SEP +
                        esc(String.valueOf(p.getMinBelanja())) + SEP +
                        esc(p.getStatus()));
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan promo: " + e.getMessage());
        }
    }

    public static void simpanPengeluaran() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PENGELUARAN, StandardCharsets.UTF_8)) {
            for (PengeluaranItem p : Data.getDaftarPengeluaran()) {
                w.write(esc(String.valueOf(p.getId()))     + SEP +
                        esc(p.getTanggal())                + SEP +
                        esc(p.getKategori())               + SEP +
                        esc(String.valueOf(p.getJumlah())) + SEP +
                        esc(p.getKeterangan()));
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan pengeluaran: " + e.getMessage());
        }
    }

    public static void muatSemua() {
        muatPesanan();
        muatTransaksi();
        muatKomplain();
        muatPelanggan();
        muatPromo();
        muatPengeluaran();
    }

    public static void muatPesanan() {
        if (!Files.exists(FILE_PESANAN)) return;
        try {
            List<String> lines = Files.readAllLines(FILE_PESANAN, StandardCharsets.UTF_8);
            Data.getDaftarPesanan().clear();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP_REGEX, -1);
                if (f.length < 7) continue;
                Data.getDaftarPesanan().add(new PesananItem(
                        Integer.parseInt(f[0].trim()),
                        f[1], f[2], f[3],
                        Double.parseDouble(f[4].trim()),
                        Integer.parseInt(f[5].trim()),
                        f[6]
                ));
            }
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat pesanan: " + e.getMessage());
        }
    }

    public static void muatTransaksi() {
        if (!Files.exists(FILE_TRANSAKSI)) return;
        try {
            List<String> lines = Files.readAllLines(FILE_TRANSAKSI, StandardCharsets.UTF_8);
            Data.getDaftarTransaksi().clear();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP_REGEX, -1);
                if (f.length < 7) continue;
                Data.getDaftarTransaksi().add(new TransaksiItem(
                        f[0], f[1], f[2], f[3], f[4], f[5], f[6]
                ));
            }
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat transaksi: " + e.getMessage());
        }
    }

    public static void muatKomplain() {
        if (!Files.exists(FILE_KOMPLAIN)) return;
        try {
            List<String> lines = Files.readAllLines(FILE_KOMPLAIN, StandardCharsets.UTF_8);
            Data.getDaftarKomplain().clear();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP_REGEX, -1);
                if (f.length < 6) continue;
                Data.getDaftarKomplain().add(new KomplainItem(
                        Integer.parseInt(f[0].trim()),
                        f[1], f[2], f[3], f[4], f[5]
                ));
            }
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat komplain: " + e.getMessage());
        }
    }

    public static void muatPelanggan() {
        if (!Files.exists(FILE_PELANGGAN)) return;
        try {
            List<String> lines = Files.readAllLines(FILE_PELANGGAN, StandardCharsets.UTF_8);
            Data.getDaftarPelanggan().clear();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP_REGEX, -1);
                if (f.length < 6) continue;
                Data.getDaftarPelanggan().add(new PelangganRegister(
                        f[0], f[1], f[2], f[3], f[4], f[5]
                ));
            }
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat pelanggan: " + e.getMessage());
        }
    }

    public static void muatPromo() {
        if (!Files.exists(FILE_PROMO)) return;
        try {
            List<String> lines = Files.readAllLines(FILE_PROMO, StandardCharsets.UTF_8);
            Data.getDaftarPromo().clear();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP_REGEX, -1);
                if (f.length < 5) continue;
                Data.getDaftarPromo().add(new PromoItem(
                        f[0],
                        Integer.parseInt(f[1].trim()),
                        f[2],
                        Integer.parseInt(f[3].trim()),
                        f[4]
                ));
            }
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat promo: " + e.getMessage());
        }
    }

    public static void muatPengeluaran() {
        if (!Files.exists(FILE_PENGELUARAN)) return;
        try {
            List<String> lines = Files.readAllLines(FILE_PENGELUARAN, StandardCharsets.UTF_8);
            Data.getDaftarPengeluaran().clear();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(SEP_REGEX, -1);
                if (f.length < 5) continue;
                Data.getDaftarPengeluaran().add(new PengeluaranItem(
                        Integer.parseInt(f[0].trim()),
                        f[1], f[2],
                        Integer.parseInt(f[3].trim()),
                        f[4]
                ));
            }
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat pengeluaran: " + e.getMessage());
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("||", "__PIPE__");
    }
}
