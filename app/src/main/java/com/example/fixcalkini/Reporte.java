package com.example.fixcalkini;

public class Reporte {
    private String id;
    private String titulo;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private String estado;
    private String timestamp;

    public Reporte() {
        // Constructor vacío necesario para Firestore
    }

    public Reporte(String id, String titulo, String descripcion, Double latitud, Double longitud, String estado, String timestamp) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public String getEstado() {
        return estado;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
