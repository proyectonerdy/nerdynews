package org.proyecto.nerdynews.Utils;

import android.app.Application;

import org.proyecto.nerdynews.models.Amigo;

import java.util.ArrayList;

/**
 * Created by jmcastellano on 07/12/2017.
 */

public class GlobalData{

    private ArrayList<Amigo> misAmigos = null;
    private static GlobalData instance = null;

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
}
