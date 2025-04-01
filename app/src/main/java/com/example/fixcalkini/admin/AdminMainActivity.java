package com.example.fixcalkini.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fixcalkini.AboutActivity;
import com.example.fixcalkini.DetallesReporte;
import com.example.fixcalkini.LoginActivity;
import com.example.fixcalkini.R;
import com.example.fixcalkini.ToolBox;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminMainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GoogleSignInClient googleSignInClient; // Instancia de GoogleSignInClient
    private Button btnReportesRecientes;
    private DrawerLayout drawerLayout;
    private ImageButton btnConfig;


    private final ActivityResultLauncher<Intent> detallesLauncher = registerForActivityResult(
            new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    actualizarMapa(); // Recarga los reportes cuando se regrese de DetallesReporte
                }
            }
    );

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_main);
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
                intent = new Intent(AdminMainActivity.this, AdminProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_reportes_pendientes) {
                intent = new Intent(AdminMainActivity.this, AdminReportesPendientesActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_reportes_aceptados) {
                intent = new Intent(AdminMainActivity.this, AdminReportesAceptadosActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_acerca_de) {
                intent = new Intent(AdminMainActivity.this, AboutActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_cerrar_sesion) {
                cerrarSesion();
            }
            drawerLayout.closeDrawers();
            return true;
        });
        btnReportesRecientes = findViewById(R.id.btnReportesRecientes);
        btnReportesRecientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminReportesRecientesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String correoRecuperado = ToolBox.obtenerCorreo(getApplicationContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Coordenadas de Calkiní
        LatLng calkini = new LatLng(20.370884, -90.051370);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calkini, 15f));

        // Referencia a la base de datos Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener los reportes desde Firestore
        actualizarMapa();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(AdminMainActivity.this));
        mMap.setOnInfoWindowClickListener(marker -> {
            String idReporte = (String) marker.getTag();

            if (idReporte != null) {
                // Obtener Firestore
                DocumentReference docRef = db.collection("reportes").document(idReporte);

                // Obtener los datos antes de abrir la nueva pantalla
                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Extraer los datos del reporte
                        String titulo = documentSnapshot.getString("titulo");
                        String descripcion = documentSnapshot.getString("descripcion");
                        String estado = documentSnapshot.getString("estado");
                        double latitud = documentSnapshot.getDouble("latitud");
                        double longitud = documentSnapshot.getDouble("longitud");

                        // Enviar los datos a la nueva actividad
                        Intent intent = new Intent(AdminMainActivity.this, DetallesReporte.class);
                        intent.putExtra("id", idReporte);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("estado", estado);
                        intent.putExtra("latitud", latitud);
                        intent.putExtra("longitud", longitud);
                        intent.putExtra("evaluacion", true);
                        detallesLauncher.launch(intent); // Lanzar la actividad
                    } else {
                        Toast.makeText(AdminMainActivity.this, "Reporte no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(AdminMainActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                });
            }
        });
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
                    Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            });

        }, 3000);
    }

    private void actualizarMapa() {
        if (mMap != null) {
            mMap.clear(); // Borra todos los marcadores
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("reportes").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        double latitud = document.getDouble("latitud");
                        double longitud = document.getDouble("longitud");
                        String titulo = document.getString("titulo");
                        String estado = document.getString("estado");
                        String idReporte = document.getId();

                        if ("rechazado".equalsIgnoreCase(estado)) continue;
                        if ("arreglado".equalsIgnoreCase(estado)) continue;

                        float colorMarcador = estado.equalsIgnoreCase("pendiente") ?
                                com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED :
                                estado.equalsIgnoreCase("aceptado") ?
                                        com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN :
                                        com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

                        LatLng ubicacion = new LatLng(latitud, longitud);
                        Marker marker = mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                                .position(ubicacion)
                                .title(titulo)
                                .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(colorMarcador))
                        );

                        if (marker != null) {
                            marker.setTag(idReporte);
                        }
                    }
                } else {
                    Toast.makeText(AdminMainActivity.this, "Error al actualizar reportes", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View window;
    private final LayoutInflater inflater;

    CustomInfoWindowAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        window = inflater.inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null; // Usa getInfoContents en su lugar
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView tvTitulo = window.findViewById(R.id.tvTitulo);
        tvTitulo.setText(marker.getTitle());
        return window;
    }
}

