package com.example.fixcalkini;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    Button btnReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        //Ir a Tus Reportes
        btnReport = findViewById(R.id.btnReportes);
        btnReport.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ReportesActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Coordenadas de Calkiní
        LatLng calkini = new LatLng(20.370884, -90.051370);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calkini, 15f));

        // Agregar un marcador en Calkiní
        googleMap.addMarker(new MarkerOptions().position(calkini).title("Calkiní"));
    }
}
