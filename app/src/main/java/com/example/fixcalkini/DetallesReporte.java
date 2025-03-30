package com.example.fixcalkini;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetallesReporte extends AppCompatActivity implements OnMapReadyCallback {

    String tipo, descripcion;
    TextView txtTipo, txtDescripcion;
    ImageButton back;
    private MapView mapView;
    private GoogleMap gMap;
    private double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_reporte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tipo = getIntent().getStringExtra("titulo");
        descripcion = getIntent().getStringExtra("descripcion");
        txtTipo = findViewById(R.id.tvTipo);
        txtDescripcion = findViewById(R.id.tv_descripcion);
        back = findViewById(R.id.btnBack);
        mapView = findViewById(R.id.mapView);
        // Inicializar el MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        txtTipo.setText(tipo);
        txtDescripcion.setText(descripcion);
        // Obtener las coordenadas del intent
        latitud = getIntent().getDoubleExtra("latitud", 0.0);
        longitud = getIntent().getDoubleExtra("longitud", 0.0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng ubicacion = new LatLng(latitud, longitud);

        // Agregar marcador y centrar la cámara
        gMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación seleccionada"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16f));
        gMap.getUiSettings().setAllGesturesEnabled(false); // Deshabilita interacciones
    }


}