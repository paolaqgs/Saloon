package com.example.proyectoicm;

public class dataClient {
    private String id;

    private String cliente, servicio, costo, formapago;
    private long fecha, hora;

    public dataClient() {
    }

    public dataClient(String cliente, String servicio, String costo, String formapago, long fecha, long hora) {
        this.cliente = cliente;
        this.servicio = servicio;
        this.costo = costo;
        this.formapago = formapago;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getCliente() {
        return cliente;
    }

    public String getServicio() {
        return servicio;
    }

    public String getCosto() {
        return costo;
    }

    public String getFormapago() {
        return formapago;
    }

    public long getFecha() {
        return fecha;
    }

    public long getHora() {
        return hora;
    }
}
