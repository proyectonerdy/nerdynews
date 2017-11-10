package org.proyecto.nerdynews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


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

    public void irRegistro(View v){
        //TODO Esto se hara en la rama principal de Login/Registro
    }

    public void irOlvido(View v){
        //TODO Esto se hara en la rama principal de Login/Registro
    }

}

