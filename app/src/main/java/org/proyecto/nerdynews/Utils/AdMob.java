package org.proyecto.nerdynews.Utils;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.proyecto.nerdynews.R;

/**
 * Created by eloy on 11/12/17.
 */

public class AdMob {

    private static AdView adView;
    private static InterstitialAd intersticialAd;
    private static Context ctx;
    private static AdRequest adRequest;

    public static void initialize(Context context) {
        ctx = context;
        adRequest = new AdRequest.Builder().addTestDevice("EF7FB31EE1E155863F06CF1D12FB1B68").build();
        MobileAds.initialize(ctx, ctx.getString(R.string.adMobIdApp));
        cargaIntersticial();
    }

    public static void mostrarBanner(View view) {
        adView = (AdView) view;
        adView.loadAd(adRequest);
    }

    public static void mostrarIntersticial(){
        intersticialAd.show();
    }

    private static void cargaIntersticial() {
        // Intersticial Ad
        intersticialAd = new InterstitialAd(ctx);
        intersticialAd.setAdUnitId(ctx.getString(R.string.adMobIdIntersticial));
        intersticialAd.loadAd(adRequest);

        intersticialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                intersticialAd.loadAd(adRequest);
            }
        });
    }
}
