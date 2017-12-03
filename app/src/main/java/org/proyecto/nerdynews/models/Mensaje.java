package org.proyecto.nerdynews.models;

/**
 * Created by Ana on 03/12/2017.
 */

public class Mensaje{

    private int id;
    private int autorId;
    private String mensaje;

    public Mensaje(int id, int autorId, String mensaje) {
        this.id = id;
        this.autorId = autorId;
        this.mensaje = mensaje;
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
}
