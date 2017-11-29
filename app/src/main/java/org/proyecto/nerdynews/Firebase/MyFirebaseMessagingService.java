package org.proyecto.nerdynews.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.eventos.VisualizarEventoActivity;

import java.util.Random;

/**
 * Created by eloy on 18/11/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    // Metodo que recoge la notificación cuando la app esta abierta en primer plano
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "¡Mensaje recibido!");
        Bundle extras = new Bundle();
        extras.putString("Notificacion",remoteMessage.getData().get("Notificacion"));
        extras.putString("url",remoteMessage.getData().get("url"));
        displayNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),extras,getApplicationContext());
    }

    // Metodo que recoge la notificación cuando la app esta en segundo plano o cerrada
    @Override
    public void handleIntent(Intent intent) {
        //super.handleIntent(intent);
        Bundle extras = intent.getExtras();
        Log.e("EXTRAS INTENT FCM","----------------");
        for (String key : extras.keySet()) {
            Log.e("-----------", "KEY: " + key + "  ::: VALUE: " + extras.get(key));
        }
        displayNotification(intent.getStringExtra("gcm.notification.title"),intent.getStringExtra("gcm.notification.body"),extras,getApplicationContext());
        Log.e("Notificacion fcm","________________");
    }

    // Método que prepara y muestra la notificación
    public static void displayNotification(String titulo, String body, Bundle extra, Context ctx) {
        Intent intent = new Intent(ctx, VisualizarEventoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (extra != null)
            intent.putExtras(extra);

        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher_round))
                .setColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
                .setContentTitle(titulo)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body)
                        .setSummaryText(ctx.getString(R.string.app_name)))
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setLights(ContextCompat.getColor(ctx,R.color.colorPrimary), 1000,500);

        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

}
