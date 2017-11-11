package org.proyecto.nerdynews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

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
        if(v.getId() == R.id.crossemail){
            AutoCompleteTextView ac = (AutoCompleteTextView) this.findViewById(R.id.email);
            ac.setText("");
        }
        if(v.getId() == R.id.crosspassword){
            EditText e = (EditText) this.findViewById(R.id.password);
            e.setText("");
        }
        //si es otro se ignora
    }

    /**
     * Método que permite mostrar u ocultar la contraseña
     * @param v
     */
    public void tooglePassword(View v){
        EditText contraseña = (EditText) findViewById(R.id.password);
        int tipo = contraseña.getInputType();
        ImageButton ib = (ImageButton) findViewById(R.id.seepassword);
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
        //se termina la actividad para evitar un bucle
        finish();
    }

}
