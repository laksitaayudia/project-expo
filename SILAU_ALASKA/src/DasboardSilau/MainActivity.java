package DasboardSilau;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etNama, etBerat;
    Button btnHitung, btnReset;
    TextView tvHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNama = findViewById(R.id.etNama);
        etBerat = findViewById(R.id.etBerat);
        btnHitung = findViewById(R.id.btnHitung);
        btnReset = findViewById(R.id.btnReset);
        tvHasil = findViewById(R.id.tvHasil);

        btnHitung.setOnClickListener(v -> {

            String nama = etNama.getText().toString().trim();
            String beratText = etBerat.getText().toString().trim();

            if (nama.isEmpty()) {
                etNama.setError("Nama tidak boleh kosong");
                return;
            }

            if (beratText.isEmpty()) {
                etBerat.setError("Berat cucian tidak boleh kosong");
                return;
            }

            double berat = Double.parseDouble(beratText);
            int hargaPerKg = 5000;

            double total = berat * hargaPerKg;

            tvHasil.setText(
                    "Nama Pelanggan : " + nama +
                    "\nBerat Cucian : " + berat + " kg" +
                    "\nHarga per Kg : Rp " + hargaPerKg +
                    "\nTotal Bayar : Rp " + total
            );
        });

        btnReset.setOnClickListener(v -> {
            etNama.setText("");
            etBerat.setText("");
            tvHasil.setText("");
            etNama.requestFocus();
        });
    }
}