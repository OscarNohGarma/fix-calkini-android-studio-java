package com.example.fixcalkini;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private ImageView viewImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        viewImg = findViewById(R.id.loge);

        Animation fromZoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fromZoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        Animation over = AnimationUtils.loadAnimation(this, R.anim.fromover);

        viewImg.startAnimation(fromZoomIn);

        // Retrasar el cambio de Activity sin bloquear el hilo principal
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }, 2000); // 2 segundos
    }
}
