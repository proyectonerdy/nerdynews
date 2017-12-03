package org.proyecto.nerdynews.models;

/**
 * Created by Ana on 03/12/2017.
 */

public class Mensaje{

    private int id;
    private int autorId;
    private String mensaje;
    private String fecha;

    public Mensaje(int id, int autorId, String mensaje, String fecha) {
        this.id = id;
        this.autorId = autorId;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
