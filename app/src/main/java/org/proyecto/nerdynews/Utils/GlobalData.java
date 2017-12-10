package org.proyecto.nerdynews.Utils;

import android.app.Application;

import org.proyecto.nerdynews.models.Amigo;
import org.proyecto.nerdynews.models.HistorialMensaje;
import org.proyecto.nerdynews.models.Mensaje;

import java.util.ArrayList;

/**
 * Created by jmcastellano on 07/12/2017.
 */

public class GlobalData{

    private ArrayList<Amigo> misAmigos = null;
    private static GlobalData instance = null;
    private ArrayList<HistorialMensaje> listahistoriasmensaje = null;
    private int idmensaje = 10000;

    public GlobalData(){
        super();
    }

    public static GlobalData getInstance(){
        if(instance==null){
            instance = new GlobalData();
        }
        return instance;
    }

    public ArrayList<Amigo> getMisAmigos() {
        return misAmigos;
    }

    public void setMisAmigos(ArrayList<Amigo> misAmigos) {
        this.misAmigos = misAmigos;
    }

    public ArrayList<HistorialMensaje> getListahistoriasmensaje() {
        return listahistoriasmensaje;
    }

    public void setListahistoriasmensaje(ArrayList<HistorialMensaje> listahistoriasmensaje) {
        this.listahistoriasmensaje = listahistoriasmensaje;
    }

    public int getIdmensaje() {
        return idmensaje++;
    }

    public void setIdmensaje(int idmensaje) {
        this.idmensaje = idmensaje;
    }
}
