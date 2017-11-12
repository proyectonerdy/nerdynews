package org.proyecto.nerdynews.Login;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import org.proyecto.nerdynews.R;

/**
 * Pantalla de Login que se pide Usuario y Password
 */
public class LoginActivity extends AppCompatActivity{


    /**
     * Método constructor de la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

}

