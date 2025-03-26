package com.example.fixcalkini;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fixcalkini.admin.AdminMainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton btnBack;
    Button btnGuardar;
    EditText edit_nombre;
    TextView text_correo, text_registro,txtReportes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGuardar = findViewById(R.id.btnGuardar);
        btnBack = findViewById(R.id.btnBack);


        edit_nombre = findViewById(R.id.etNombre);
        text_correo = findViewById(R.id.txtEmail);
        text_registro = findViewById(R.id.txtRegistro);
        txtReportes = findViewById(R.id.txtReportes);
        text_correo.setText(ToolBox.obtenerCorreo(getApplicationContext()));
        txtReportes.setText(ToolBox.obtenerCantidadReportes(getApplicationContext()));
        db.collection("users").document(text_correo.getText().toString()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String nombre = documentSnapshot.getString("nombre");
                String fecha = documentSnapshot.getString("fecha_registro");

                edit_nombre.setText(nombre);
                text_registro.setText(fecha);
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

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarNombre()){
                    db.collection("users").document(text_correo.getText().toString())
                            .update("nombre", edit_nombre.getText().toString())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                            });
                }else {
                    Toast.makeText(getApplicationContext(), "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private boolean validarNombre() {
        return Reglas.validarNombre(edit_nombre.getText().toString()) == 1 ? true : false;

    }
}