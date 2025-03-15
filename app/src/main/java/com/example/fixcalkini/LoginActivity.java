package com.example.fixcalkini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private ImageView viewImg;
    private LinearLayout layoutLogin, layoutRegistro;
    private Button btnLogin, btnRegistro;
    private TextView btnRegis, btnVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewImg = findViewById(R.id.idcarrite);
        layoutLogin = findViewById(R.id.layout_login);
        layoutRegistro = findViewById(R.id.layout_registro);
        btnLogin = findViewById(R.id.btn_inicio);
        btnRegistro = findViewById(R.id.btn_registro);
        btnRegis = findViewById(R.id.btn_regis);
        btnVolverLogin = findViewById(R.id.btn_volver_login);

        // Animaciones
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        Animation fromTop = AnimationUtils.loadAnimation(this, R.anim.anim_from_top);
        viewImg.startAnimation(fromTop);

        btnRegis.setOnClickListener(v -> {
            // Primero animamos la salida del login
            layoutLogin.startAnimation(slideOut);
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    layoutLogin.setVisibility(View.GONE);
                    layoutRegistro.setVisibility(View.VISIBLE);
                    layoutRegistro.startAnimation(slideIn);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });

        btnVolverLogin.setOnClickListener(v -> {
            // Primero animamos la salida del registro
            layoutRegistro.startAnimation(slideOutRight);
            slideOutRight.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    layoutRegistro.setVisibility(View.GONE);
                    layoutLogin.setVisibility(View.VISIBLE);
                    layoutLogin.startAnimation(slideInLeft);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });


        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

