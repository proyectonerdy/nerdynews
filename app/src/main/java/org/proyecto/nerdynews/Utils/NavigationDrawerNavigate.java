package org.proyecto.nerdynews.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import org.proyecto.nerdynews.Login.LoginActivity;
import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.eventos.ListadoEventosActivity;
import org.proyecto.nerdynews.intereses.ListadoInteresesActivity;

/**
 * Created by jmcastellano on 13/11/2017.
 */

public class NavigationDrawerNavigate {

    public static boolean Navigate(MenuItem item, final Activity actividad){
        // Handle navigation view item clicks here
        DrawerLayout drawer = (DrawerLayout) actividad.findViewById(R.id.lidrawer_layout);
        switch (item.getItemId()){
            case R.id.nav_eventos:
                drawer.closeDrawer(GravityCompat.START);
                if(!actividad.getClass().getSimpleName().equals(ListadoEventosActivity.class.getSimpleName())) {
                    actividad.startActivity(new Intent(actividad, ListadoEventosActivity.class), ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                }
                break;
            case R.id.nav_interes:
                drawer.closeDrawer(GravityCompat.START);
                if(!actividad.getClass().getSimpleName().equals(ListadoInteresesActivity.class.getSimpleName())) {
                    actividad.startActivity(new Intent(actividad, ListadoInteresesActivity.class), ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                }
                break;
            case R.id.nav_perfil:
                //startActivity(new Intent(this, PerfilActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                Toast.makeText(actividad,R.string.faltaimplementar,Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_cerrar_sesion:
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(actividad, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actividad.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        }
                        catch(Exception e){

                        }
                        actividad.finish();
                    }
                });
                t.start();
        }
        return true;
    }

    public static void OnBackPressed(Activity actividad){
        DrawerLayout drawer = (DrawerLayout) actividad.findViewById(R.id.lidrawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            actividad.getParent().onBackPressed();
        }
    }
}