package com.example.proyectoicm;

public class dataServicio {
    private String servicio, descripcion;
    private int costo;

    public dataServicio(){    }

    public dataServicio(String servicio, String descripcion, int costo) {
        this.servicio = servicio;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public String getServicio() {
        return servicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCosto() {
        return costo;
    }
}
