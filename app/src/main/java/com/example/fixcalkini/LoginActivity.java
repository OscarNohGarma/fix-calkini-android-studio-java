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
            layoutRegistro.setVisibility(View.VISIBLE); // Hacemos visible el registro antes de iniciar animación
            layoutRegistro.startAnimation(slideIn); // Se desliza dentro

            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    layoutLogin.setVisibility(View.GONE); // Ocultamos hasta que termine la animación
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            layoutLogin.startAnimation(slideOut); // Se desliza fuera
        });

        btnVolverLogin.setOnClickListener(v -> {
            layoutLogin.setVisibility(View.VISIBLE); // Hacemos visible el login antes de iniciar animación
            layoutLogin.startAnimation(slideInLeft); // Se desliza dentro

            slideOutRight.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    layoutRegistro.setVisibility(View.GONE); // Ocultamos hasta que termine la animación
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            layoutRegistro.startAnimation(slideOutRight); // Se desliza fuera
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

