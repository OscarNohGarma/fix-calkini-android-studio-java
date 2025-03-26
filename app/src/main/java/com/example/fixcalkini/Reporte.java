package com.example.fixcalkini;

public class Reporte {
    private String titulo;
    private String descripcion;

    public Reporte() {
        // Constructor vacío necesario para Firestore
    }

    public Reporte(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
