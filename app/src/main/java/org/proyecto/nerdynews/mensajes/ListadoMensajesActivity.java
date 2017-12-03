package org.proyecto.nerdynews.mensajes;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.GsonBuilder;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;
import org.proyecto.nerdynews.models.Amigo;
import org.proyecto.nerdynews.models.HistorialMensaje;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class ListadoMensajesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerListadoMensajes;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HistorialMensaje[] listaMensajes;
    private ListadoMensajesRecyclerAdapter adapterListadoMensajes;
    private Amigo[] amigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_mensajes);

        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.mensajes));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Listado de eventos
        recyclerListadoMensajes = (RecyclerView) findViewById(R.id.reciclerViewListadoMensajes);
        recyclerListadoMensajes.setLayoutManager(new GridLayoutManager(this, 1));

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

    private void cargarDatosLista() {
        // Obtenemos los elementos desde el fake .json
        listaMensajes = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeMensajes.json", this), HistorialMensaje[].class);
        amigos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeAmigos.json", this), Amigo[].class);

        // Pasamos los datos al adaptador para crear la lista
        adapterListadoMensajes = new ListadoMensajesRecyclerAdapter(listaMensajes, amigos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerListadoMensajes.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerListadoMensajes.setAdapter(adapterListadoMensajes);
        adapterListadoMensajes.notifyDataSetChanged();

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
}
