package org.proyecto.nerdynews.Login;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.proyecto.nerdynews.BuildConfig;
import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.intereses.ListadoInteresesActivity;

/**
 * Pantalla de Login que se pide Usuario y Password
 */
public class LoginActivity extends AppCompatActivity{

    private FirebaseAnalytics analytics;
    private FirebaseRemoteConfig remoteConfig;
    private static final int CACHE_TIME_SECONDS = 30;

    /**
     * Método constructor de la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("LOGIN", "--------FCM Token Refresh: " + FirebaseInstanceId.getInstance().getToken());

        analytics = FirebaseAnalytics.getInstance(this);

        //Test A/B: Cambio de color de icono
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings config = new FirebaseRemoteConfigSettings
                .Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        remoteConfig.setConfigSettings(config);
        remoteConfig.setDefaults(R.xml.remote_config);
        remoteConfig.fetch(CACHE_TIME_SECONDS)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Fetch OK",
                                    Toast.LENGTH_SHORT).show();
                            remoteConfig.activateFetched();
                        } else {
                            Toast.makeText(LoginActivity.this, "Fetch ha fallado",
                                    Toast.LENGTH_SHORT).show();
                        }
                        String cambio_color = remoteConfig.getString("cambio_color");
                        if (cambio_color.equals("azul")){
                            findViewById(R.id.logologin).setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.primaryColor));
                            ((ImageView)findViewById(R.id.logologin)).setImageResource(R.drawable.ic_logo_nerdy_blue);
                            findViewById(R.id.email_sign_in_button).setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.primaryColor));
                            findViewById(R.id.group_login).setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.primaryColor));
                            findViewById(R.id.cabeceralogin).setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.primaryColor));
                            getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.primaryColor));
                            analytics.setUserProperty( "experimento_colores_log", "azul" );
                        } else {
                            analytics.setUserProperty( "experimento_colores_log", "verde" );
                        }
                    }
                });
        sesionIniciada();
    }


    /**
     * Método que nos permite resetear el campo al que apunte la vista
     * @param v
     */
    public void resetField(View v){
        if(v.getId() == R.id.crossemaillogin){
            AutoCompleteTextView ac = (AutoCompleteTextView) this.findViewById(R.id.emaillogin);
            ac.setText("");
        }
        if(v.getId() == R.id.crosspasswordlogin){
            EditText e = (EditText) this.findViewById(R.id.passwordlogin);
            e.setText("");
        }
        //si es otro se ignora
    }

    /**
     * Método que nos permite mostrar u ocultar la contraseña
     * @param v
     */
    public void tooglePassword(View v){
        EditText contraseña = (EditText) findViewById(R.id.passwordlogin);
        int tipo = contraseña.getInputType();
        ImageButton ib = (ImageButton) findViewById(R.id.seepasswordlogin);
        if (tipo == InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            //hay que cambiar el icono del boton por el icono tachado
            ib.setImageResource(R.drawable.eye_off);
        }
        else {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //hay que cambiar el icono del boton por el icono no tachado
            ib.setImageResource(R.drawable.eye);
        }
    }

    /**
     * Método que nos permite ir a la Actividad para Registrarse
     * @param v
     */
    public void irRegistro(View v){
        Intent intent = new Intent(this,RegistroActivity.class);
        View view = this.getCurrentFocus();
        if(view!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

    /**
     * Método que nos permite ir a la Actividad para el Olvido de la contraseña
     * @param v
     */
    public void irOlvido(View v){
        Intent intent = new Intent(this,OlvidoActivity.class);
        View view = this.getCurrentFocus();
        if(view!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /**
     * Método que nos permite ir a la Actividad para mostrar al usuario los intereses disponibles
     * @param v
     */
    public void gotoIntereses(View v){
        AutoCompleteTextView email = (AutoCompleteTextView) this.findViewById(R.id.emaillogin);
        EditText password = (EditText) this.findViewById(R.id.passwordlogin);

        if(email==null || email.getText()==null || email.getText().length() < 1 || password==null || password.getText()==null || password.getText().length() < 1 ){
            Toast.makeText(this,R.string.datosobligatorioslogin,Toast.LENGTH_LONG).show();
            return;
        }
        mantenerSesion();
        Intent intent = new Intent(this,ListadoInteresesActivity.class);
        View view = this.getCurrentFocus();
        if(view!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch(Exception e){

                }
                finish();
            }
        });
        t.start();
    }

    private void mantenerSesion() {
        SharedPreferences pref = getSharedPreferences("nerdy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        CheckBox checkBox = findViewById(R.id.recordarmelogin);
        editor.putBoolean("sesionIniciada", checkBox.isChecked());
        editor.commit();
    }

    private void sesionIniciada(){
        SharedPreferences pref = getSharedPreferences("nerdy", Context.MODE_PRIVATE);
        Boolean isActive = pref.getBoolean("sesionIniciada", false);
        if (isActive){
            Intent intent = new Intent(this,ListadoInteresesActivity.class);
            startActivity(intent);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    finish();
                }
            });
            t.start();
        }
    }

    public void gotoInteresesRedSocial(View v){
        mantenerSesion();
        Intent intent = new Intent(this,ListadoInteresesActivity.class);
        View view = this.getCurrentFocus();
        if(view!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch(Exception e){

                }
                finish();
            }
        });
        t.start();
    }
}

