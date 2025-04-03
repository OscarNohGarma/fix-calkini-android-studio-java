package com.example.fixcalkini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReportesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReporteAdapter adapter;
    private List<Reporte> listaReportes = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View txtSinReportes; // Declarar como variable de clase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reportes);

        // Configurar botón de regreso
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerReportes);
        txtSinReportes = findViewById(R.id.txtSinReportes); // Guardar referencia

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReporteAdapter(listaReportes, this::verDetalles, false);
        recyclerView.setAdapter(adapter);

        // Cargar los reportes desde Firebase
        cargarReportes();
    }

    private void cargarReportes() {
        String usuarioCorreo = ToolBox.obtenerCorreo(getApplicationContext()); // Obtener el correo del usuario autenticado
        if (usuarioCorreo == null || usuarioCorreo.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró el usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference reportesRef = db.collection("reportes");

        // Filtrar reportes donde el propietario sea el usuario autenticado
        reportesRef.whereEqualTo("propietario", usuarioCorreo)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), "Error al cargar reportes", Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseReportes", "Error al obtener reportes", error);
                            return;
                        }

                        if (value == null) {
                            Log.e("FirebaseReportes", "No se encontraron reportes o hubo un error.");
                            return;
                        }

                        listaReportes.clear();
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId(); // Obtener el ID del documento
                            String titulo = document.getString("titulo");
                            String descripcion = document.getString("descripcion");
                            Double latitud = document.getDouble("latitud");
                            Double longitud = document.getDouble("longitud");
                            String estado = document.getString("estado");
                            String timestamp = document.getString("timestamp");
                            // Imprimir en la consola de Logcat
                            Log.d("FirebaseReportes", "ID: " + id + ", Título: " + titulo + ", Descripción: " + descripcion + " " + latitud + " " + longitud + " " + timestamp);

                            // Agregar a la lista solo con título y descripción
                            listaReportes.add(new Reporte(id, titulo, descripcion, latitud, longitud, estado, timestamp));
                        }

                        adapter.notifyDataSetChanged();

                        // Mostrar RecyclerView si hay reportes, o el mensaje de "No hay reportes"

                        if (listaReportes.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            txtSinReportes.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            txtSinReportes.setVisibility(View.GONE);
                        }
                    }
                });
    } // Método para mostrar la alerta con el ID del reporte

    private void verDetalles(@NonNull Reporte reporte) {

        Intent intent = new Intent(ReportesActivity.this, DetallesReporte.class);
        intent.putExtra("id", reporte.getId());
        intent.putExtra("titulo", reporte.getTitulo());
        intent.putExtra("descripcion", reporte.getDescripcion());
        intent.putExtra("latitud", reporte.getLatitud());
        intent.putExtra("longitud", reporte.getLongitud());
        intent.putExtra("estado", reporte.getEstado());
        startActivity(intent);
    }

}
