package org.aficiones.noticias.nerdynews.Perfil;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.Utils.NavigationDrawerNavigate;
import org.aficiones.noticias.nerdynews.intereses.ListadoInteresesActivity;

/* Clase que contiene la pantalla del perfil */

public class PerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button b1, b2;
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);
        Bundle extras = getIntent().getExtras();
        String vregistro = extras.getString("registro");
        String vemail = extras.getString("email");
        String vpassword = extras.getString("password");

        iv = (ImageView) findViewById(R.id.fotoperfil);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.linav_view);
        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        View hView =  navigationView.getHeaderView(0);
        TextView user = hView.findViewById(R.id.tv_nombre);
        user.setText(prefs.getString("nombre", "Nerdy News"));
        navigationView.setNavigationItemSelectedListener(this);

        if (vregistro.equals("N")) {
            LinearLayout linea = (LinearLayout) findViewById(R.id.lcabecerabienvenida);
            linea.removeAllViews();
            EditText password = (EditText) findViewById(R.id.passwordperfil);
            password.setText("1111");
            EditText alias = (EditText) findViewById(R.id.aliasperfil);
            alias.setText("GPR1985");
            EditText nombre = (EditText) findViewById(R.id.nombreperfil);
            nombre.setText("José Manuel Castellano Domínguez");
            EditText edad = (EditText) findViewById(R.id.edad);
            edad.setText("32");
            EditText masdatos = (EditText) findViewById(R.id.masdatos);
            masdatos.setText("Programador muy friki");
            TextView titulo = (TextView) findViewById(R.id.cabeceraperfil);
            titulo.setText(getString(R.string.titperfil) + " " + "GPR1985@gmail.com");

            iv.setImageResource(R.drawable.foto_perfil_default);

        } else {
            TextView titulo = (TextView) findViewById(R.id.cabeceraperfil);
            titulo.setText(getString(R.string.titperfil) + " " + vemail.toString());
            EditText password = (EditText) findViewById(R.id.passwordperfil);
            int tipo = password.getInputType();
            password.setText(vpassword.toString());
        }


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
     *
     * @param v
     */
    public void resetField(View v) {

        if (v.getId() == R.id.crosspasswordregistro) {
            EditText e = (EditText) this.findViewById(R.id.passwordregistro);
            e.setText("");
        }
        //si es otro se ignora
    }

    /**
     * Método que permite mostrar u ocultar la contraseña
     *
     * @param v
     */
    public void tooglePassword(View v) {
        EditText contraseña = (EditText) findViewById(R.id.passwordperfil);
        int tipo = contraseña.getInputType();
        ImageButton ib = (ImageButton) findViewById(R.id.seepasswordperfil);
        if (tipo == InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            //hay que cambiar el icono del boton por el icono tachado
            ib.setImageResource(R.drawable.eye_off);
        } else {
            contraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //hay que cambiar el icono del boton por el icono no tachado
            ib.setImageResource(R.drawable.eye);
        }
    }

    /**
     * Método que nos permite ir a la Actividad para mostrar al usuario los intereses disponibles
     *
     * @param v
     */
    public void gotoIntereses(View v) {
        EditText password = (EditText) this.findViewById(R.id.passwordperfil);
        EditText usuario = this.findViewById(R.id.aliasperfil);

        if (password == null || password.getText() == null || password.getText().length() < 1) {
            Toast.makeText(this, R.string.datosobligatoriosperfil, Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, getString(R.string.perfilcreado), Toast.LENGTH_LONG).show();

        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre", usuario.getText().toString());
        editor.commit();

        Intent intent = new Intent(this, ListadoInteresesActivity.class);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                finish();
            }
        });
        t.start();
    }

    @Override
    public void onBackPressed() {
        if(NavigationDrawerNavigate.isOpened(this)){
            NavigationDrawerNavigate.OnBackPressed(this);
        }
        else{
            this.finish();
        }
    }

    // Metodo cuando se hce click en los items del menú
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return NavigationDrawerNavigate.Navigate(item,this);
    }

}
