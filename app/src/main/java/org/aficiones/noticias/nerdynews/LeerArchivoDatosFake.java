package org.aficiones.noticias.nerdynews;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by eloy on 7/11/17.
 */

public class LeerArchivoDatosFake {
    /** Lee el archivo JSON almacenado en la carpeta ASSETS del proyecto.
     * @param fichero Nombre del fichero que queremos leer
     * @param ctx Contegto de la aplicación, se lo pasaremos con "this" o "getApplicationContext()"
     */
    public static String loadJSONFromAsset(String fichero, Context ctx) {
        String json = null;
        try {
            InputStream is = ctx.getAssets().open(fichero);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
