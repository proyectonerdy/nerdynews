package org.aficiones.noticias.nerdynews.Login;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.aficiones.noticias.nerdynews.Perfil.PerfilActivity;
import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.intereses.ListadoInteresesActivity;

/* Clase que contiene la pantalla de registro */

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    /**
     * Método que permite borrar el campo que apunta la vista
     * @param v
     */
    public void resetField(View v){
        if(v.getId() == R.id.crossemailregistro){
            AutoCompleteTextView ac = (AutoCompleteTextView) this.findViewById(R.id.emailregistro);
            ac.setText("");
        }
        if(v.getId() == R.id.crosspasswordregistro){
            EditText e = (EditText) this.findViewById(R.id.passwordregistro);
            e.setText("");
        }
        //si es otro se ignora
    }

    /**
     * Método que permite mostrar u ocultar la contraseña
     * @param v
     */
    public void tooglePassword(View v){
        EditText contraseña = (EditText) findViewById(R.id.passwordregistro);
        int tipo = contraseña.getInputType();
        ImageButton ib = (ImageButton) findViewById(R.id.seepasswordregistro);
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
     * Método que va a la actividad de Login
     * @param v
     */
    public void irLogin(View v){
        View view = this.getCurrentFocus();
        if(view!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();

    }

    /**
     * Método que nos permite ir a la Actividad para mostrar al usuario los intereses disponibles
     * @param v
     */
    public void gotoIntereses(View v){
        AutoCompleteTextView email = (AutoCompleteTextView) this.findViewById(R.id.emailregistro);
        EditText password = (EditText) this.findViewById(R.id.passwordregistro);

        if(email==null || email.getText()==null || email.getText().length() < 1 || password==null || password.getText()==null || password.getText().length() < 1 ){
            Toast.makeText(this,R.string.datosobligatoriosregistro,Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this,String.format(getString(R.string.cuentacreada),email.getText()),Toast.LENGTH_LONG).show();

       // Redireccionamos a la actividad de pefil
        // Intent intent = new Intent(this,ListadoInteresesActivity.class);
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("registro", "S");
        intent.putExtra("email",email.getText().toString());
        intent.putExtra("password",password.getText().toString());


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

    public void gotoInteresesRedSocial(View v){
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
