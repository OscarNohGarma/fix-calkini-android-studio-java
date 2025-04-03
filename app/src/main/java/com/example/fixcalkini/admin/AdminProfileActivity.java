package com.example.fixcalkini.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fixcalkini.R;
import com.example.fixcalkini.ToolBox;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfileActivity extends AppCompatActivity {


    TextView text_nombre, text_correo, text_registro, txtReportes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        text_nombre = findViewById(R.id.txtNombre);
        text_correo = findViewById(R.id.txtEmail);
        text_registro = findViewById(R.id.txtRegistro);
        txtReportes = findViewById(R.id.txtReportes);
        text_correo.setText(ToolBox.obtenerCorreo(getApplicationContext()));

        // Obtener el número de reportes
        CollectionReference reportesRef = db.collection("reportes");
        reportesRef.whereEqualTo("revisor", ToolBox.obtenerCorreo(getApplicationContext()))
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (value != null) {
                        txtReportes.setText(String.valueOf(value.size())); // Directamente obtenemos la cantidad
                    } else {
                        txtReportes.setText("0"); // Si no hay datos, mostramos 0
                    }
                });

        db.collection("users").document(text_correo.getText().toString()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String nombre = documentSnapshot.getString("nombre");
                String fecha = documentSnapshot.getString("fecha_registro");

                text_nombre.setText(nombre);
                text_registro.setText(fecha);
            } else {
                Toast.makeText(getApplicationContext(), "No se encontró el usuario en la base de datos", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error al obtener el nombre", Toast.LENGTH_SHORT).show();

        });

    }
}