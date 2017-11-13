package org.proyecto.nerdynews.eventos;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;

public class VisualizarEventoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_evento);

        //obtener el valor pasado en el Bundle
        String title = getIntent().getStringExtra("TITULO");
        String texto = getIntent().getStringExtra("TEXTO");
        String fecha = getIntent().getStringExtra("FECHA");
        String lugar = getIntent().getStringExtra("LUGAR");
        String url = getIntent().getStringExtra("DIBUJO");

        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView titulo = (TextView) this.findViewById(R.id.tituloEventoVisualizar);
        titulo.setText(title);
        TextView textoview = (TextView)this.findViewById(R.id.textoEventoVisualizar);
        textoview.setText(texto);
        TextView fechaview = (TextView)this.findViewById(R.id.cvFechaEvento);
        fechaview.setText(fecha);
        TextView lugarview = (TextView)this.findViewById(R.id.cvLugarEvento);
        lugarview.setText(lugar);
        ImageView imagen = (ImageView) this.findViewById(R.id.imagenEvento);

        if(url!=null && !url.isEmpty()){
            Picasso.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher_round)
                    .into(imagen);
        }
    }

    @Override
    public void onBackPressed() {
        NavigationDrawerNavigate.OnBackPressed(this);
    }

    // Metodo cuando se hce click en los items del menú
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return NavigationDrawerNavigate.Navigate(item,this);
    }
}
