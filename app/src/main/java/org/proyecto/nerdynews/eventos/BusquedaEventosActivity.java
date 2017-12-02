package org.proyecto.nerdynews.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;
import org.proyecto.nerdynews.models.Evento;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class BusquedaEventosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoEventos;
    private ListadoEventosRecyclerAdapter adapterListadoEventos;
    private Evento[] listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_eventos);

        Spinner spinner = (Spinner) findViewById(R.id.spinneropciones);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opcionesbusqueda, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.buscareventos));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Listado de eventos
        recyclerListadoEventos = (RecyclerView) findViewById(R.id.reciclerViewListadoEventos);
        recyclerListadoEventos.setLayoutManager(new GridLayoutManager(this, 1));

        // Permite recargar los datos de la lista haciendo scroll en lo alto de la lista
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarDatosLista();
            }
        });

        // Cargamos la lista
        cargarDatosLista();
    }

    private void cargarDatosLista(){
        // Obtenemos los elementos desde el fake .json
        listaEventos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeEventos.json", this), Evento[].class);

        // Pasamos los datos al adaptador para crear la lista
        adapterListadoEventos = new ListadoEventosRecyclerAdapter(listaEventos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerListadoEventos.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerListadoEventos.setAdapter(adapterListadoEventos);
        adapterListadoEventos.notifyDataSetChanged();

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
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

    public void gotoEvento(View v){
        Intent intent = new Intent(this,VisualizarEventoActivity.class);
        TextView titulo = (TextView) v.findViewById(R.id.cvTituloEvento);
        TextView texto = (TextView) v.findViewById(R.id.cvResumenEvento);
        TextView fecha = (TextView) v.findViewById(R.id.cvFechaEvento);
        TextView lugar = (TextView) v.findViewById(R.id.cvLugarEvento);
        ImageView drawable = (ImageView) v.findViewById(R.id.cvImagenEvento);
        intent.putExtra("TITULO",titulo.getText());
        intent.putExtra("TEXTO",texto.getText());
        intent.putExtra("FECHA",fecha.getText());
        intent.putExtra("LUGAR",lugar.getText());
        intent.putExtra("DIBUJO",(String)drawable.getTag());
        intent.putExtra("COORDSGPS",(String)lugar.getTag());
        ActivityOptionsCompat options = ActivityOptionsCompat. makeSceneTransitionAnimation(BusquedaEventosActivity.this, new Pair<View, String>(v.findViewById(R.id.cvImagenEvento), getString(R.string.transition_name_img)));
        ActivityCompat.startActivity(BusquedaEventosActivity.this, intent, options .toBundle());
    }
}
