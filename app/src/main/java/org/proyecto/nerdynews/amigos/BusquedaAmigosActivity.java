package org.proyecto.nerdynews.amigos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;
import org.proyecto.nerdynews.models.Amigo;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

/**
 * Created by MyC on 22-11-2017.
 */

public class BusquedaAmigosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , SearchView.OnQueryTextListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private AmigosAdapter amigosAdapter;
    private ArrayList<Amigo> listaAmigos;
    private SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_busqueda_amigos);

        // Menu laterar
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Busqueda Amigos");

        DrawerLayout drawer =  findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView =  findViewById(R.id.reciclerViewBusquedaAmigos);
        searchView =  findViewById(R.id.searchView1);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        swipeRefreshLayout =  findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarDatosLista();
            }
        });

        cargarDatosLista();
    }

    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Buscar intereses");
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return NavigationDrawerNavigate.Navigate(item,this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //NavigationDrawerNavigate.OnBackPressed(this);
    }

    private void cargarDatosLista(){
        // Obtenemos los elementos desde el fake .json
        Type listType = new TypeToken<ArrayList<Amigo>>(){}.getType();
        listaAmigos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeAmigos.json", this),listType);
        // Pasamos los datos al adaptador para crear la lista
        amigosAdapter = new AmigosAdapter(listaAmigos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(amigosAdapter);
        amigosAdapter.notifyDataSetChanged();

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setFilterTouchesWhenObscured(true);
        setupSearchView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        amigosAdapter.getFilter().filter(newText);
        return true;
    }
}
