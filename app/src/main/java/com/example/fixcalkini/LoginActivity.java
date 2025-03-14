package com.example.fixcalkini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private ImageView viewImg;
    private Button btnLogin; // Declarar aquí, pero inicializar en onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewImg = findViewById(R.id.idcarrite);
        btnLogin = findViewById(R.id.btn_inicio); // 🔹 Inicializar aquí

        Animation fromTop = AnimationUtils.loadAnimation(this, R.anim.anim_from_top);
        viewImg.startAnimation(fromTop);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Evita que el usuario regrese al Login
            }
        });
    }
}

