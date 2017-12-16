package org.aficiones.noticias.nerdynews.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.eventos.VisualizarEventoActivity;

/**
 * Created by jmcastellano on 01/12/2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appwWidgetManager, int[] widgetIds){
        for(int widgetId: widgetIds){
            actualizaWidget(context,widgetId);
        }
    }

    public static void actualizaWidget(Context context,int widgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intent = new Intent(context, VisualizarEventoActivity.class);
        intent.putExtra("TITULO",context.getString(R.string.titulowidget));
        intent.putExtra("TEXTO",context.getString(R.string.resumenwidget));
        intent.putExtra("FECHA",context.getString(R.string.fechawidget));
        intent.putExtra("LUGAR",context.getString(R.string.lugawidget));
        intent.putExtra("DIBUJO",context.getString(R.string.imageUrl));
        intent.putExtra("COORDSGPS",context.getString(R.string.coordGPS));
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.layout_padre_widget, pendingIntent);
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId,remoteViews);
    }
}
