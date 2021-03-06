package org.aficiones.noticias.nerdynews.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.aficiones.noticias.nerdynews.Login.LoginActivity;
import org.aficiones.noticias.nerdynews.mensajes.*;
import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.amigos.ListadoAmigosActivity;
import org.aficiones.noticias.nerdynews.eventos.BusquedaEventosActivity;
import org.aficiones.noticias.nerdynews.eventos.ListadoEventosActivity;
import org.aficiones.noticias.nerdynews.intereses.ListadoFavoritosActivity;
import org.aficiones.noticias.nerdynews.intereses.ListadoInteresesActivity;
import org.aficiones.noticias.nerdynews.Perfil.PerfilActivity;

import java.util.Date;

/**
 * Created by jmcastellano on 13/11/2017.
 */

public class NavigationDrawerNavigate {

    private IInAppBillingService serviceBilling;
    private ServiceConnection serviceConnection;

    public static boolean Navigate(MenuItem item, final Activity actividad,InApp inApp){
        // Handle navigation view item clicks here
        DrawerLayout drawer = (DrawerLayout) actividad.findViewById(R.id.lidrawer_layout);
        Intent intent = null;
        SharedPreferences pref = null;
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
                //hay que comprobar si es Premium
                if(inApp.checkPurchasedInAppProducts(actividad)){
                    Date fecha = new Date();
                    long startMillis = fecha.getTime();
                    Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    intent = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    actividad.startActivity(intent);
                }
                else{
                    //no es premium
                    Toast.makeText(actividad,actividad.getString(R.string.debespremium),Toast.LENGTH_LONG).show();
                }
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
            case R.id.nav_compartir:
                String texto = "Lista de eventos , intereses y amigos al alcance de tu mano. \nCompartido https://goo.gl/Ug7eXC";
                drawer.closeDrawer(GravityCompat.START);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, texto);
                actividad.startActivity(Intent.createChooser(i, "Selecciona aplicación"));
                break;
            case R.id.nav_mensajes:
                //startActivity(new Intent(this, PerfilActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                if(!actividad.getClass().getSimpleName().equals(ListadoMensajesActivity.class.getSimpleName())) {
                    actividad.startActivity(new Intent(actividad, ListadoMensajesActivity.class), ActivityOptions.makeSceneTransitionAnimation(actividad).toBundle());
                }
                break;
            case R.id.nav_premium:
                //compramos producto
                inApp.comprarProducto(actividad);
                break;

            case R.id.nav_cerrar_sesion:
                pref = actividad.getSharedPreferences("nerdy", Context.MODE_PRIVATE);
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

    public static boolean isOpened(Activity actividad){
        DrawerLayout drawer = (DrawerLayout) actividad.findViewById(R.id.lidrawer_layout);
        if (drawer!=null && drawer.isDrawerOpen(GravityCompat.START)) {
            return true;
        }
        return false;
    }

    public static void OnBackPressed(Activity actividad){
        DrawerLayout drawer = (DrawerLayout) actividad.findViewById(R.id.lidrawer_layout);
        if (drawer!=null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public static void hideItem(NavigationView navigationView)
    {
        if(navigationView!=null) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_premium).setVisible(false);
        }
    }


}
