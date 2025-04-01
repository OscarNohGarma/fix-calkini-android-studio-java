package com.example.fixcalkini.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageButton btnBack;

    TextView etNombre, txtEmail, txtReportes,txtRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_profile);


        btnBack = findViewById(R.id.btnBack);


        etNombre = findViewById(R.id.etNombre);
        txtEmail = findViewById(R.id.txtEmail);
        txtRegistro = findViewById(R.id.txtRegistro);
        txtReportes = findViewById(R.id.txtReportes);

        txtEmail.setText(ToolBox.obtenerCorreo(getApplicationContext()));

        // Obtener el número de reportes
        CollectionReference reportesRef = db.collection("reportes");
        reportesRef.whereEqualTo("estado", "aceptado")
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
        db.collection("users").document(txtEmail.getText().toString()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String nombre = documentSnapshot.getString("nombre");
                String fecha = documentSnapshot.getString("fecha_registro");

                etNombre.setText(nombre);
                txtRegistro.setText(fecha);
            } else {
                Toast.makeText(getApplicationContext(), "No se encontró el usuario en la base de datos", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error al obtener el nombre", Toast.LENGTH_SHORT).show();

        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}