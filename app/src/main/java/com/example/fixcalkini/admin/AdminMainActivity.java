package com.example.fixcalkini.admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import com.example.fixcalkini.AboutActivity;
import com.example.fixcalkini.LoginActivity;
import com.example.fixcalkini.ProfileActivity;
import com.example.fixcalkini.R;


import com.example.fixcalkini.ReportesActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

public class AdminMainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btnReportesRecientes;
    private DrawerLayout drawerLayout;
    private ImageButton btnConfig;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_main);
        EdgeToEdge.enable(this);
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
                intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Coordenadas de Calkiní
        LatLng calkini = new LatLng(20.370884, -90.051370);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calkini, 15f));

        // Marcador del centro de Calkiní
        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                .position(calkini)
                .title("Calkiní")
                .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(
                        com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE))
        );

        // Marcadores de reportes NO públicos (rojo)
        LatLng reporteNoPublico1 = new LatLng(20.371500, -90.052000);
        LatLng reporteNoPublico2 = new LatLng(20.369800, -90.050500);

        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                .position(reporteNoPublico1)
                .title("Reporte pendiente 1")
                .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(
                        com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED))
        );

        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                .position(reporteNoPublico2)
                .title("Reporte pendiente 2")
                .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(
                        com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED))
        );

        // Marcadores de reportes PÚBLICOS (verde)
        LatLng reportePublico1 = new LatLng(20.372000, -90.051500);
        LatLng reportePublico2 = new LatLng(20.370200, -90.049800);

        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                .position(reportePublico1)
                .title("Reporte público 1")
                .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(
                        com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN))
        );

        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
                .position(reportePublico2)
                .title("Reporte público 2")
                .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(
                        com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN))
        );
    }

    // Mostrar diálogo de reportar un problema

}
