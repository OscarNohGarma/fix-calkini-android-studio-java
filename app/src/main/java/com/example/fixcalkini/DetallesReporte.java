package com.example.fixcalkini;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetallesReporte extends AppCompatActivity implements OnMapReadyCallback {

    String id, tipo, descripcion, estado;
    Boolean evaluacion;
    TextView txtTipo, txtDescripcion, txtEstado;
    ImageButton back;
    private MapView mapView;
    private GoogleMap gMap;
    private double latitud, longitud;
    Button btnAceptar, btnRechazar, btnArreglar;
    String text_correo;

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

        text_correo = ToolBox.obtenerCorreo(getApplicationContext());

        id = getIntent().getStringExtra("id");
        tipo = getIntent().getStringExtra("titulo");
        descripcion = getIntent().getStringExtra("descripcion");
        estado = getIntent().getStringExtra("estado");
        evaluacion = getIntent().getBooleanExtra("evaluacion", false);

        btnAceptar = findViewById(R.id.btn_aceptar);
        btnRechazar = findViewById(R.id.btn_rechazar);
        btnArreglar = findViewById(R.id.btn_marcar_arreglado);
        txtTipo = findViewById(R.id.tvTipo);
        txtDescripcion = findViewById(R.id.tv_descripcion);
        back = findViewById(R.id.btnBack);
        txtEstado = findViewById(R.id.txtEstado);

        mapView = findViewById(R.id.mapView);
        // Inicializar el MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        txtTipo.setText(tipo);
        txtDescripcion.setText(descripcion);
        String newEstado = "Estado del reporte: " + estado.toUpperCase();
        txtEstado.setText(newEstado);
        // Obtener las coordenadas del intent
        latitud = getIntent().getDoubleExtra("latitud", 0.0);
        longitud = getIntent().getDoubleExtra("longitud", 0.0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (evaluacion) {
            switch (estado) {
                case "pendiente":

                    btnAceptar.setVisibility(View.VISIBLE);
                    btnRechazar.setVisibility(View.VISIBLE);
                    break;
                case "aceptado":
                    btnArreglar.setVisibility(View.VISIBLE);
            }
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        btnAceptar.setOnClickListener(v -> {
            if (id != null) {
                DocumentReference reporteRef = db.collection("reportes").document(id);

                // Crear un mapa con los campos a actualizar
                Map<String, Object> actualizaciones = new HashMap<>();
                actualizaciones.put("estado", "aceptado");
                actualizaciones.put("revisor", text_correo);

                // Actualizar estado a "aceptado"
                reporteRef.update(actualizaciones)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DetallesReporte.this, "Reporte aceptado", Toast.LENGTH_SHORT).show();
                            // Enviar resultado a AdminMainActivity
                            setResult(RESULT_OK);
                            // Finalizar actividad para regresar
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(DetallesReporte.this, "Error al actualizar", Toast.LENGTH_SHORT).show());
            }
        });
        btnRechazar.setOnClickListener(v -> {
            if (id != null) {
                DocumentReference reporteRef = db.collection("reportes").document(id);
                
                // Actualizar estado a "aceptado"
                reporteRef.update("estado", "rechazado")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DetallesReporte.this, "Reporte rechazado", Toast.LENGTH_SHORT).show();
                            // Enviar resultado a AdminMainActivity
                            setResult(RESULT_OK);
                            // Finalizar actividad para regresar
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(DetallesReporte.this, "Error al actualizar", Toast.LENGTH_SHORT).show());
            }
        });
        btnArreglar.setOnClickListener(v -> {
            if (id != null) {
                DocumentReference reporteRef = db.collection("reportes").document(id);

                // Actualizar estado a "aceptado"
                reporteRef.update("estado", "arreglado")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DetallesReporte.this, "Reporte arreglado", Toast.LENGTH_SHORT).show();
                            // Enviar resultado a AdminMainActivity
                            setResult(RESULT_OK);
                            // Finalizar actividad para regresar
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(DetallesReporte.this, "Error al actualizar", Toast.LENGTH_SHORT).show());
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