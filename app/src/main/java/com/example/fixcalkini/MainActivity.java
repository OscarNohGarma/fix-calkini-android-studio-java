package com.example.fixcalkini;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btnReport;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        // Obtener el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Botón para ver los reportes
        btnReport = findViewById(R.id.btnReportes);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReportesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Coordenadas de Calkiní
        LatLng calkini = new LatLng(20.370884, -90.051370);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calkini, 15f));

        // Agregar un marcador en Calkiní
        mMap.addMarker(new MarkerOptions().position(calkini).title("Calkiní"));

        // Detectar clics en el mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mostrarDialogoReportar(latLng);
            }
        });
    }

    private void mostrarDialogoReportar(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reportar un problema");
        builder.setMessage("¿Quieres reportar un problema en este punto?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Enviar las coordenadas a la nueva actividad
                Intent intent = new Intent(MainActivity.this, ReportarActivity.class);
                intent.putExtra("latitud", latLng.latitude);
                intent.putExtra("longitud", latLng.longitude);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}