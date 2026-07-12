package Karyawan;

import Registrasi.PelangganRegister;
import Owner.PromoItem;
import Owner.PengeluaranItem;
import Owner.KaryawanItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class AppStorage {

    private static final Path DATA_DIR = Paths.get("SILAU_ALASKA", "src", "data");

    private static final Path FILE_PESANAN     = DATA_DIR.resolve("pesanan.xml");
    private static final Path FILE_TRANSAKSI   = DATA_DIR.resolve("transaksi.xml");
    private static final Path FILE_KOMPLAIN    = DATA_DIR.resolve("komplain.xml");
    private static final Path FILE_PELANGGAN   = DATA_DIR.resolve("pelanggan.xml");
    private static final Path FILE_PROMO       = DATA_DIR.resolve("promo.xml");
    private static final Path FILE_PENGELUARAN = DATA_DIR.resolve("pengeluaran.xml");
    private static final Path FILE_KARYAWAN    = DATA_DIR.resolve("karyawan.xml");

    private static final XStream xstream;

    static {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal membuat direktori data: " + e.getMessage());
        }
        xstream = new XStream(new DomDriver("UTF-8"));
        xstream.addPermission(AnyTypePermission.ANY);
    }

    public static void simpanSemua() {
        simpanPesanan();
        simpanTransaksi();
        simpanKomplain();
        simpanPelanggan();
        simpanPromo();
        simpanPengeluaran();
        simpanKaryawan();
    }

    public static void simpanPesanan() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PESANAN, StandardCharsets.UTF_8)) {
            List<PesananItem> list = new ArrayList<>(Data.getDaftarPesanan());
            xstream.toXML(list, w);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan pesanan: " + e.getMessage());
        }
    }

    public static void simpanTransaksi() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_TRANSAKSI, StandardCharsets.UTF_8)) {
            List<TransaksiItem> list = new ArrayList<>(Data.getDaftarTransaksi());
            xstream.toXML(list, w);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan transaksi: " + e.getMessage());
        }
    }

    public static void simpanKomplain() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_KOMPLAIN, StandardCharsets.UTF_8)) {
            List<KomplainItem> list = new ArrayList<>(Data.getDaftarKomplain());
            xstream.toXML(list, w);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan komplain: " + e.getMessage());
        }
    }

    public static void simpanPelanggan() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PELANGGAN, StandardCharsets.UTF_8)) {
            List<PelangganRegister> list = new ArrayList<>(Data.getDaftarPelanggan());
            xstream.toXML(list, w);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan pelanggan: " + e.getMessage());
        }
    }

    public static void simpanPromo() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PROMO, StandardCharsets.UTF_8)) {
            List<PromoItem> list = new ArrayList<>(Data.getDaftarPromo());
            xstream.toXML(list, w);
        } catch (IOException e) {.
            System.err.println("[AppStorage] Gagal menyimpan promo: " + e.getMessage());
        }
    }

    public static void simpanPengeluaran() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_PENGELUARAN, StandardCharsets.UTF_8)) {
            List<PengeluaranItem> list = new ArrayList<>(Data.getDaftarPengeluaran());
            xstream.toXML(list, w);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan pengeluaran: " + e.getMessage());
        }
    }

    public static void simpanKaryawan() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE_KARYAWAN, StandardCharsets.UTF_8)) {
            List<KaryawanItem> list = new ArrayList<>(Data.getDaftarKaryawan());
            xstream.toXML(list, w);
        } catch (IOException e) {
            System.err.println("[AppStorage] Gagal menyimpan karyawan: " + e.getMessage());
        }
    }

    public static void muatSemua() {
        muatPesanan();
        muatTransaksi();
        muatKomplain();
        muatPelanggan();
        muatPromo();
        muatPengeluaran();
        muatKaryawan();
    }

    @SuppressWarnings("unchecked")
    public static void muatPesanan() {
        if (!Files.exists(FILE_PESANAN)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE_PESANAN, StandardCharsets.UTF_8)) {
            List<PesananItem> list = (List<PesananItem>) xstream.fromXML(r);
            Data.getDaftarPesanan().clear();
            Data.getDaftarPesanan().addAll(list);
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat pesanan: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void muatTransaksi() {
        if (!Files.exists(FILE_TRANSAKSI)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE_TRANSAKSI, StandardCharsets.UTF_8)) {
            List<TransaksiItem> list = (List<TransaksiItem>) xstream.fromXML(r);
            Data.getDaftarTransaksi().clear();
            Data.getDaftarTransaksi().addAll(list);
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat transaksi: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void muatKomplain() {
        if (!Files.exists(FILE_KOMPLAIN)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE_KOMPLAIN, StandardCharsets.UTF_8)) {
            List<KomplainItem> list = (List<KomplainItem>) xstream.fromXML(r);
            Data.getDaftarKomplain().clear();
            Data.getDaftarKomplain().addAll(list);
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat komplain: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void muatPelanggan() {
        if (!Files.exists(FILE_PELANGGAN)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE_PELANGGAN, StandardCharsets.UTF_8)) {
            List<PelangganRegister> list = (List<PelangganRegister>) xstream.fromXML(r);
            Data.getDaftarPelanggan().clear();
            Data.getDaftarPelanggan().addAll(list);
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat pelanggan: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void muatPromo() {
        if (!Files.exists(FILE_PROMO)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE_PROMO, StandardCharsets.UTF_8)) {
            List<PromoItem> list = (List<PromoItem>) xstream.fromXML(r);
            Data.getDaftarPromo().clear();
            Data.getDaftarPromo().addAll(list);
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat promo: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void muatPengeluaran() {
        if (!Files.exists(FILE_PENGELUARAN)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE_PENGELUARAN, StandardCharsets.UTF_8)) {
            List<PengeluaranItem> list = (List<PengeluaranItem>) xstream.fromXML(r);
            Data.getDaftarPengeluaran().clear();
            Data.getDaftarPengeluaran().addAll(list);
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat pengeluaran: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void muatKaryawan() {
        if (!Files.exists(FILE_KARYAWAN)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE_KARYAWAN, StandardCharsets.UTF_8)) {
            List<KaryawanItem> list = (List<KaryawanItem>) xstream.fromXML(r);
            Data.getDaftarKaryawan().clear();
            Data.getDaftarKaryawan().addAll(list);
        } catch (Exception e) {
            System.err.println("[AppStorage] Gagal memuat karyawan: " + e.getMessage());
        }
    }
}
