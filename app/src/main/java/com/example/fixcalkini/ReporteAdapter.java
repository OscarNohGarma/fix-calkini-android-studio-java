package com.example.fixcalkini;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private List<Reporte> listaReportes;

    public ReporteAdapter(List<Reporte> listaReportes) {
        this.listaReportes = listaReportes;
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
    }

    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion;

        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloReporte);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionReporte);
        }
    }
}
