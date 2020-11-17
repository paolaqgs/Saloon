package com.example.proyectoicm;

public class dataClient {
    private String id;

    private String cliente, servicio, formapago;
    private int costo;
    private long fecha, hora;

    public dataClient() {
    }

    public dataClient(String cliente, String servicio, String formapago, int costo, long fecha, long hora) {
        this.cliente = cliente;
        this.servicio = servicio;
        this.formapago = formapago;
        this.costo = costo;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getCliente() {
        return cliente;
    }

    public String getServicio() {
        return servicio;
    }

    public String getFormapago() {
        return formapago;
    }

    public int getCosto() {
        return costo;
    }

    public long getFecha() {
        return fecha;
    }

    public long getHora() {
        return hora;
    }
}
