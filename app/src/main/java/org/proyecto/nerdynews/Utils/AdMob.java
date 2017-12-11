package org.proyecto.nerdynews.Utils;

import android.content.Context;
import android.view.View;

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
    private InterstitialAd intersticialAd;
    private static Context ctx;
    private static AdRequest adRequest;

    public static void initialize(Context context) {
        ctx = context;
        adRequest = new AdRequest.Builder().addTestDevice("EF7FB31EE1E155863F06CF1D12FB1B68").build();
        MobileAds.initialize(ctx, ctx.getString(R.string.adMobIdApp));
    }

    public static void mostrarBanner(View view) {
        adView = (AdView) view;
        adView.loadAd(adRequest);
    }

}
