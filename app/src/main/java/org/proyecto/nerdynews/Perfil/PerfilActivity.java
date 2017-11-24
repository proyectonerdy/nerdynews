package org.proyecto.nerdynews.Perfil;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.intereses.ListadoInteresesActivity;

/* Clase que contiene la pantalla del perfil */

public class PerfilActivity extends AppCompatActivity {
    Button b1,b2;
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Bundle extras = getIntent().getExtras();
        String vregistro = extras.getString("registro");
        String vemail = extras.getString("email");
        String vpassword = extras.getString("password");
        if (vregistro.equals("N"))
        {
            LinearLayout linea = (LinearLayout) findViewById(R.id.lcabecerabienvenida);
            linea.removeAllViews();
        }
        else {
            TextView titulo = (TextView) findViewById(R.id.cabeceraperfil);
            titulo.setText(getString(R.string.titperfil) + " " + vemail.toString());
            EditText password = (EditText) findViewById(R.id.passwordperfil);
            int tipo = password.getInputType();
            password.setText(vpassword.toString());
        }

        iv=(ImageView)findViewById(R.id.fotoperfil);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(bp);
    }

    /**
     * Método que permite borrar el campo que apunta la vista
     * @param v
     */
    public void resetField(View v){

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
        EditText contraseña = (EditText) findViewById(R.id.passwordperfil);
        int tipo = contraseña.getInputType();
        ImageButton ib = (ImageButton) findViewById(R.id.seepasswordperfil);
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
     * Método que nos permite ir a la Actividad para mostrar al usuario los intereses disponibles
     * @param v
     */
    public void gotoIntereses(View v){
       EditText password = (EditText) this.findViewById(R.id.passwordperfil);

        if( password==null || password.getText()==null || password.getText().length() < 1 ){
            Toast.makeText(this,R.string.datosobligatoriosperfil,Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this,getString(R.string.perfilcreado),Toast.LENGTH_LONG).show();

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
