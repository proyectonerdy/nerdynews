package org.proyecto.nerdynews.models;

import java.util.ArrayList;

/**
 * Created by Ana on 03/12/2017.
 */

public class HistorialMensaje {
    private int id;
    private int amigoId;
    private ArrayList<Mensaje> historial;

    public HistorialMensaje(int id, int amigoId, ArrayList<Mensaje> historial) {
        this.id = id;
        this.amigoId = amigoId;
        this.historial = historial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmigoId() {
        return amigoId;
    }

    public void setAmigoId(int amigoId) {
        this.amigoId = amigoId;
    }

    public ArrayList<Mensaje> getHistorial() {
        return historial;
    }

    public void setHistorial(ArrayList<Mensaje> historial) {
        this.historial = historial;
    }
}
