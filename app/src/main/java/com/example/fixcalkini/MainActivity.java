package com.example.fixcalkini;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient; // Instancia de GoogleSignInClient
    private Button btnReport;
    private DrawerLayout drawerLayout;
    private ImageButton btnConfig;
    private boolean permitirSeleccion = false; // Control para habilitar selección en el mapa

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        // Inicializar GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configurar el botón "Crear un nuevo reporte"
        btnReport = findViewById(R.id.btnReportes);
        btnReport.setOnClickListener(v -> {
            permitirSeleccion = true;
            Toast.makeText(MainActivity.this, "Selecciona un punto en el mapa para reportar", Toast.LENGTH_LONG).show();
        });

        // Configurar el menú lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        btnConfig = findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.navigation_view)));

        NavigationView navigationView = findViewById(R.id.navigation_view);
        MenuItem cerrarSesionItem = navigationView.getMenu().findItem(R.id.nav_cerrar_sesion);
        if (cerrarSesionItem != null) {
            SpannableString textoRojo = new SpannableString("Cerrar sesión");
            textoRojo.setSpan(new ForegroundColorSpan(Color.RED), 0, textoRojo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            cerrarSesionItem.setTitle(textoRojo);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.nav_perfil) {
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_reportes) {
                intent = new Intent(MainActivity.this, ReportesActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_acerca_de) {
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_cerrar_sesion) {
                cerrarSesion(); // Método corregido para cerrar sesión correctamente
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng calkini = new LatLng(20.370884, -90.051370);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calkini, 15f));
        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(calkini).title("Calkiní"));

        mMap.setOnMapClickListener(latLng -> {
            if (permitirSeleccion) {
                mostrarDialogoReportar(latLng);
                permitirSeleccion = false;
            } else {
                Toast.makeText(MainActivity.this, "Presiona 'Crear un nuevo reporte' antes de seleccionar un punto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoReportar(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reportar, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportarActivity.class);
            intent.putExtra("latitud", latLng.latitude);
            intent.putExtra("longitud", latLng.longitude);
            startActivity(intent);
            dialog.dismiss();
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String correoRecuperado = ToolBox.obtenerCorreo(getApplicationContext());
    }

    private void cerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog, null));
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            ToolBox.setEstadoSesion(getApplicationContext(), false);
            ToolBox.guardarCorreo(getApplicationContext(), "");

            googleSignInClient.signOut().addOnCompleteListener(this, task -> {
                googleSignInClient.revokeAccess().addOnCompleteListener(this, revokeTask -> {
                    Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            });

        }, 3000);
    }
}
