package com.example.fixcalkini;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcalkini.admin.AdminMainActivity;

import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private List<Reporte> listaReportes;
    private OnItemClickListener listener;
    private boolean mostrarBotonUbicacion; // Nuevo parámetro

    // Interfaz para manejar los clics
    public interface OnItemClickListener {
        void onItemClick(Reporte reporte);
    }


    public ReporteAdapter(List<Reporte> listaReportes, OnItemClickListener listener, boolean mostrarBotonUbicacion) {
        this.listaReportes = listaReportes;
        this.listener = listener;
        this.mostrarBotonUbicacion = mostrarBotonUbicacion;
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReporteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        Reporte reporte = listaReportes.get(position);
        holder.txtTitulo.setText(reporte.getTitulo());
        holder.txtDescripcion.setText(reporte.getDescripcion());

        // Mostrar u ocultar el botón de ubicación según la actividad
        if (mostrarBotonUbicacion) {
            holder.btnVerEnMapa.setVisibility(View.VISIBLE);
            holder.btnVerEnMapa.setOnClickListener(v -> {
                // Llamar al mapa pasando la latitud y longitud del reporte
                Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
                intent.putExtra("latitud", reporte.getLatitud());
                intent.putExtra("longitud", reporte.getLongitud());
                v.getContext().startActivity(intent);
            });
        } else {
            holder.btnVerEnMapa.setVisibility(View.GONE);
        }


        // Asignar evento de clic
        holder.itemView.setOnClickListener(v -> listener.onItemClick(reporte));
    }

    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion;
        ImageButton btnVerEnMapa;

        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloReporte);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionReporte);
            btnVerEnMapa = itemView.findViewById(R.id.btnVerEnMapa);
        }

    }
}
