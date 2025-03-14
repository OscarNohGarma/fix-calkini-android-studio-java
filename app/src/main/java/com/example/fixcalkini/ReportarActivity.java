package com.example.fixcalkini;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar);

        // Obtener las coordenadas enviadas
        double latitud = getIntent().getDoubleExtra("latitud", 0.0);
        double longitud = getIntent().getDoubleExtra("longitud", 0.0);

        // Mostrar las coordenadas en un TextView (puedes cambiarlo después)
        TextView txtUbicacion = findViewById(R.id.txtUbicacion);
        txtUbicacion.setText("Ubicación seleccionada:\nLat: " + latitud + "\nLng: " + longitud);
    }
}