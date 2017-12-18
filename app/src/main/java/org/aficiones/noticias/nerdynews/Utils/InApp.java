package org.aficiones.noticias.nerdynews.Utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.aficiones.noticias.nerdynews.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jmcastellano on 17/12/2017.
 */

public class InApp {

    private IInAppBillingService serviceBilling;
    private ServiceConnection serviceConnection;
    private final String ID_ARTICULO = "org.aficiones.noticias.nerdynews.premium";
    private final int INAPP_BILLING = 1;
    private String developerPayLoad = null;

    public void serviceConectInAppBilling(Activity actividad) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceBilling = null;
            }
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceBilling= IInAppBillingService.Stub.asInterface(service);
            }
        };
        Intent serviceIntent = new Intent( "com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        actividad.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void comprarProducto(Activity actividad) {
        developerPayLoad = actividad.getString(R.string.payLoad);
        if (serviceBilling != null) {
            Bundle buyIntentBundle = null;
            try {
                buyIntentBundle = serviceBilling.getBuyIntent(3, actividad.getPackageName(), ID_ARTICULO, "inapp", developerPayLoad);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            try {
                if (pendingIntent != null) {
                    actividad.startIntentSenderForResult(pendingIntent.getIntentSender(), INAPP_BILLING, new Intent(), 0, 0, 0);
                }
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(actividad, "Servicio de compras no disponible", Toast.LENGTH_LONG).show();
        }
    }

    public void comprobarCompra(int requestCode, int resultCode, Intent data, Activity actividad) {
        switch (requestCode) {
            case INAPP_BILLING: {
                int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
                String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
                String dataSignature = data.getStringExtra( "INAPP_DATA_SIGNATURE");
                if (resultCode == actividad.RESULT_OK) {
                    try {
                        JSONObject jo = new JSONObject(purchaseData);
                        String sku = jo.getString("productId");
                        String developerPayload = jo.getString("developerPayload");
                        String purchaseToken = jo.getString("purchaseToken");
                        if (sku.equals(ID_ARTICULO)) {
                            Toast.makeText(actividad,actividad.getString(R.string.compracompletada), Toast.LENGTH_LONG).show();
                            //tenemos que guardar en las preferencias dle usuario que es premium
                            SharedPreferences pref = actividad.getSharedPreferences("nerdy", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("premium", true);
                            editor.commit();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean checkPurchasedInAppProducts(Activity actividad) {
        Bundle ownedItemsInApp = null;
        if (serviceBilling != null) {
            try {
                ownedItemsInApp = serviceBilling.getPurchases(3, actividad.getPackageName(), "inapp", null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            int response = ownedItemsInApp.getInt("RESPONSE_CODE");
            System.out.println(response);
            if (response == 0) {
                ArrayList<String> ownedSkus = ownedItemsInApp.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> purchaseDataList = ownedItemsInApp.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> signatureList = ownedItemsInApp.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String continuationToken = ownedItemsInApp.getString("INAPP_CONTINUATION_TOKEN");
                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);
                    System.out.println("Inapp Purchase data: " + purchaseData);
                    System.out.println("Inapp Signature: " + signature);
                    System.out.println("Inapp Sku: " + sku);
                    if (sku.equals(ID_ARTICULO)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
