package com.example.fixcalkini;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ReportarActivity extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner spinnerTipoReporte;
    private TextView txtUbicacion;
    private MapView mapView;
    private GoogleMap gMap;
    private ImageButton btn_back;
    private Button btn_enviar_reporte;
    private double latitud, longitud;

    private EditText et_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinnerTipoReporte = findViewById(R.id.spinner_tipo_reporte);
        mapView = findViewById(R.id.mapView);
        // Inicializar el MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btn_back = findViewById(R.id.btnBack);
        btn_enviar_reporte = findViewById(R.id.btn_enviar_reporte);

        et_descripcion = findViewById(R.id.et_descripcion);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_enviar_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportar();
            }
        });

        // Obtener las coordenadas del intent
        latitud = getIntent().getDoubleExtra("latitud", 0.0);
        longitud = getIntent().getDoubleExtra("longitud", 0.0);

        // Configurar el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipo_reporte, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTipoReporte.setAdapter(adapter);


    }

    private void reportar() {
        // Obtener el tipo de reporte seleccionado en el spinner
        String tituloReporte = spinnerTipoReporte.getSelectedItem().toString();
        long timestamp = System.currentTimeMillis(); // Valor obtenido de Firebase
        String fechaLegible = convertirTimestamp(timestamp);
        // Metodo Hash para guardar los datos
        HashMap<String, Object> reporteMash = new HashMap<>();
        reporteMash.put("propietario", ToolBox.obtenerCorreo(getApplicationContext()));
        reporteMash.put("titulo", tituloReporte);
        reporteMash.put("descripcion", et_descripcion.getText().toString());
        reporteMash.put("latitud", latitud);
        reporteMash.put("longitud", longitud);
        reporteMash.put("timestamp", fechaLegible); // Guardar fecha y hora del reporte
        reporteMash.put("estado", "pendiente"); // Por defecto el reporte se encuentra pendiente


        // Subir el reporte con un ID aleatorio generado automáticamente por Firestore
        db.collection("reportes").add(reporteMash).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Reporte enviado con éxito", Toast.LENGTH_SHORT).show();
                finish(); // Cerrar la actividad después de enviar el reporte
            } else {
                Toast.makeText(getApplicationContext(), "Error al enviar el reporte", Toast.LENGTH_SHORT).show();
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

    private String convertirTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    // Métodos del ciclo de vida para el MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
