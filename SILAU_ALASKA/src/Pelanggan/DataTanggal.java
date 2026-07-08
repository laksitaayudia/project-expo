package Pelanggan;

import java.util.HashMap;
import java.util.Map;

public class DataTanggal {
    private static final Map<Integer, String> mapTanggal = new HashMap<>();

    static {
        // Dummy data matching default laundry orders in Karyawan.Data
        mapTanggal.put(1, "12/05/2025");
        mapTanggal.put(2, "12/05/2025");
        mapTanggal.put(3, "13/05/2025");
        mapTanggal.put(4, "14/05/2025");
    }

    public static void setTanggal(int id, String tanggal) {
        mapTanggal.put(id, tanggal);
    }

    public static String getTanggal(int id) {
        return mapTanggal.getOrDefault(id, java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
