package com.example.tubes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoImage = findViewById(R.id.logoImage);
        ImageView textImage = findViewById(R.id.textImage);

        animateLogo(textImage);
    }

    private void animateLogo(ImageView textImage) {
        ScaleAnimation scale = new ScaleAnimation(
                0.2f, 1.0f, 0.2f, 1.0f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(1000);
        scale.setFillAfter(true);

        AlphaAnimation fade = new AlphaAnimation(0f, 1f);
        fade.setDuration(800);
        fade.setFillAfter(true);

        // Logo
        logoImage.startAnimation(scale);
        logoImage.startAnimation(fade);
        logoImage.setAlpha(1f);

        // Tampil judul
        new Handler().postDelayed(() -> {
            textImage.setVisibility(View.VISIBLE);
            textImage.animate().alpha(1f).setDuration(500).start();
        }, 1300);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 2500);
    }
}
