package com.example.tubes;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengatur agar window berjalan di belakang status bar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_home_page);

        // Tidak ada logika UI yang kompleks (seperti onClick) di kode Compose,
        // jadi class Java ini cukup sederhana.
        // Jika ada tombol atau elemen interaktif, Anda akan menambahkan
        // findViewById dan setOnClickListener di sini.
        // Contoh:
        // Button joinButton = findViewById(R.id.join_button);
        // joinButton.setOnClickListener(v -> {
        //     // Aksi saat tombol diklik
        // });
        ImageView logoImage = findViewById(R.id.logoImageView);
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AktivitasActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

    }
}
