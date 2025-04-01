package com.example.fixcalkini.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcalkini.R;
import com.example.fixcalkini.Reporte;
import com.example.fixcalkini.ReporteAdapter;
import com.example.fixcalkini.ToolBox;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminReportesAceptadosActivity extends AppCompatActivity {
    ImageButton btnBack;
    private RecyclerView recyclerView;
    private ReporteAdapter adapter;
    private List<Reporte> listaReportes = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View txtSinReportes; // Declarar como variable de clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_reportes_aceptados);
        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerReportes);
        txtSinReportes = findViewById(R.id.txtSinReportes); // Guardar referencia

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReporteAdapter(listaReportes, this::verDetalles);
        recyclerView.setAdapter(adapter);

        // Cargar los reportes desde Firebase
        cargarReportes();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void cargarReportes() {
        String usuarioCorreo = ToolBox.obtenerCorreo(getApplicationContext()); // Obtener el correo del usuario autenticado
        if (usuarioCorreo == null || usuarioCorreo.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró el usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference reportesRef = db.collection("reportes");

        // Filtrar reportes donde el propietario sea el usuario autenticado
        reportesRef.whereEqualTo("estado", "arreglado")
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
                            String descripcion = document.getString("propietario");
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

    }
}
