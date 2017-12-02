package org.proyecto.nerdynews.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.MimeTypeFilter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import org.proyecto.nerdynews.Login.LoginActivity;
import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.amigos.ListadoAmigosActivity;
import org.proyecto.nerdynews.eventos.BusquedaEventosActivity;
import org.proyecto.nerdynews.eventos.ListadoEventosActivity;
import org.proyecto.nerdynews.intereses.ListadoFavoritosActivity;
import org.proyecto.nerdynews.intereses.ListadoInteresesActivity;
import org.proyecto.nerdynews.Perfil.PerfilActivity;

import java.util.Date;

/**
 * Created by jmcastellano on 13/11/2017.
 */

public class NavigationDrawerNavigate {

    public static boolean Navigate(MenuItem item, final Activity actividad){
        // Handle navigation view item clicks here
        DrawerLayout drawer = (DrawerLayout) actividad.findViewById(R.id.lidrawer_layout);
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.nav_amigos:
                drawer.closeDrawer(GravityCompat.START);
                if(!actividad.getClass().getSimpleName().equals(ListadoAmigosActivity.class.getSimpleName())) {
                    actividad.startActivity(new Intent(actividad, ListadoAmigosActivity.class), ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                }
                break;
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
            case R.id.nav_favoritos:
                drawer.closeDrawer(GravityCompat.START);
                if(!actividad.getClass().getSimpleName().equals(ListadoFavoritosActivity.class.getSimpleName())) {
                    actividad.startActivity(new Intent(actividad, ListadoFavoritosActivity.class), ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                }
                break;
            case R.id.nav_buscareventos:
                drawer.closeDrawer(GravityCompat.START);
                if(!actividad.getClass().getSimpleName().equals(BusquedaEventosActivity.class.getSimpleName())) {
                    actividad.startActivity(new Intent(actividad, BusquedaEventosActivity.class), ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                }
                break;
            case R.id.nav_micalendario:
                drawer.closeDrawer(GravityCompat.START);
                //hay que abrir el calendario de google asociado a la cuenta del cliente
                Date fecha = new Date();
                long startMillis = fecha.getTime();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                actividad.startActivity(intent);
                break;
            case R.id.nav_perfil:
                //startActivity(new Intent(this, PerfilActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                if(!actividad.getClass().getSimpleName().equals(ListadoFavoritosActivity.class.getSimpleName())) {
                    intent = new Intent(actividad, PerfilActivity.class);
                    intent.putExtra("registro", "N");
                    actividad.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                }
                break;
            case R.id.nav_cerrar_sesion:
                SharedPreferences pref = actividad.getSharedPreferences("nerdy", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("sesionIniciada", false);
                editor.commit();

                drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(actividad, LoginActivity.class);
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
            actividad.onBackPressed();
        }
    }
}
