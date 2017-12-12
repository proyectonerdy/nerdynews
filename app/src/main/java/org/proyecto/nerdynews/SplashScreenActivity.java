package org.proyecto.nerdynews;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import org.proyecto.nerdynews.Login.LoginActivity;
import org.proyecto.nerdynews.Utils.AdMob;

/**
 * Created by MyC on 10-12-2017.
 */

public class SplashScreenActivity extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!


    @Override
    public void initSplash(ConfigSplash configSplash) {
        // Inicializamos AdMob
        AdMob.initialize(this);

			/* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher);
        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("Nerdy News");
        configSplash.setTitleTextColor(android.R.color.white);
        configSplash.setTitleTextSize(24f); //float value
        configSplash.setAnimTitleDuration(800);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
    }

    @Override
    public void animationsFinished() {

        Intent intent = new Intent(this,LoginActivity.class);

        startActivity(intent);
        //transit to another activity here
        //or do whatever you want
    }

    @Override
    public void onBackPressed() {
        return;
    }
}