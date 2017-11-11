package org.proyecto.nerdynews.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.GsonBuilder;

import org.proyecto.nerdynews.Login.LoginActivity;
import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.models.Evento;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class ListadoEventosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoEventos;
    private ListadoEventosRecyclerAdapter adapterListadoEventos;
    private Evento[] listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_eventos);

        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Eventos");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Metodo cuando se hce click en los items del menú
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here
        switch (item.getItemId()){
            case R.id.nav_eventos:
                startActivity(new Intent(this, ListadoEventosActivity.class));
                break;
            case R.id.nav_perfil:
                //startActivity(new Intent(this, PerfilActivity.class));
                break;
            case R.id.nav_cerrar_sesion:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
