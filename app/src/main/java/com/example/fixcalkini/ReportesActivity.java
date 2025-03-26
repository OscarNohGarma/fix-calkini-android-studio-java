package com.example.fixcalkini;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReporteAdapter(listaReportes);
        recyclerView.setAdapter(adapter);

        // Cargar los reportes desde Firebase
        cargarReportes();
    }

    private void cargarReportes() {
        String usuarioCorreo = ToolBox.obtenerCorreo(getApplicationContext()); // Obtener el correo del usuario autenticado

        CollectionReference reportesRef = db.collection("reportes");

        // Filtrar reportes donde el propietario sea el usuario autenticado
        reportesRef.whereEqualTo("propietario", usuarioCorreo)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        listaReportes.clear();
                        for (QueryDocumentSnapshot document : value) {
                            String titulo = document.getString("titulo");
                            String descripcion = document.getString("descripcion");

                            // Agregar a la lista solo con título y descripción
                            listaReportes.add(new Reporte(titulo, descripcion));
                        }

                        adapter.notifyDataSetChanged();

                        // Guardar la cantidad de reportes en SharedPreferences
                        ToolBox.guardarCantidadReportes(getApplicationContext(), listaReportes.size());

                        // Mostrar RecyclerView si hay reportes, o el mensaje de "No hay reportes"
                        findViewById(R.id.recyclerReportes).setVisibility(listaReportes.isEmpty() ? View.GONE : View.VISIBLE);
                        findViewById(R.id.txtSinReportes).setVisibility(listaReportes.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                });
    }

}
