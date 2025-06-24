package com.example.tubes;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AktivitasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengatur layout activity dari file activity_aktivitas.xml
        setContentView(R.layout.activity_aktivitas);

        // Kode Compose yang diberikan bersifat statis (hanya menampilkan UI).
        // Jika ada tombol yang perlu diklik, Anda akan menambahkan logika di sini.
        // Contoh:
        // FloatingActionButton fab = findViewById(R.id.fab_state_closed);
        // fab.setOnClickListener(view -> {
        //     // Aksi saat FAB diklik
        //     Snackbar.make(view, "Tombol FAB diklik!", Snackbar.LENGTH_LONG).show();
        // });
    }
}