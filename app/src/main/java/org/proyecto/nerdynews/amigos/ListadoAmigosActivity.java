package org.proyecto.nerdynews.amigos;

/**
 * Created by MyC on 22-11-2017.
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.proyecto.nerdynews.PerfilAmigoActivity;
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

public class ListadoAmigosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MisAmigosAdapter amigosAdapter;
    private ArrayList<Amigo> listaAmigos;
    private FloatingActionButton button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_amigos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mis Amigos");

        DrawerLayout drawer = findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView =  findViewById(R.id.reciclerViewBusquedaAmigos);
        button = findViewById(R.id.fab_agregar_amigo);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        swipeRefreshLayout =  findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarDatosLista();
            }
        });

        // Cargamos la lista
        cargarDatosLista();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirBusqueda();
            }
        });

    }

    public void abrirBusqueda(){
        Intent intent = new Intent(getApplicationContext(),BusquedaAmigosActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return NavigationDrawerNavigate.Navigate(item, this);
    }

    @Override
    public void onBackPressed() {
        NavigationDrawerNavigate.OnBackPressed(this);
    }


    private void cargarDatosLista() {
        // Obtenemos los elementos desde el fake .json

        Type listType = new TypeToken<ArrayList<Amigo>>() {
        }.getType();
        listaAmigos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeMisAmigos.json", this), listType);

        Log.d("aers", String.valueOf(listaAmigos.size()));
        Log.d("aers", "PASANDO");
        // Pasamos los datos al adaptador para crear la lista
        amigosAdapter = new MisAmigosAdapter(listaAmigos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(amigosAdapter);
        amigosAdapter.notifyDataSetChanged();

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setFilterTouchesWhenObscured(true);
    }

    public void gotoAmigo(View v){
        Intent intent = new Intent(this, PerfilAmigoActivity.class);
        TextView nombre = (TextView) v.findViewById(R.id.txtNombre);
        TextView edad = (TextView) v.findViewById(R.id.txtEdad);
        TextView intereses = (TextView) v.findViewById(R.id.txtIntereses);
        ImageView drawable = (ImageView) v.findViewById(R.id.ivImagenAmigo);
        intent.putExtra("NOMBRE",nombre.getText());
        intent.putExtra("EDAD",edad.getText());
        intent.putExtra("INTERESES",intereses.getText());
        intent.putExtra("DIBUJO",(String)drawable.getTag());
        ActivityOptionsCompat options = ActivityOptionsCompat. makeSceneTransitionAnimation(ListadoAmigosActivity.this, new Pair<View, String>(v.findViewById(R.id.ivImagenAmigo), getString(R.string.transition_name_img_amigo)));
        ActivityCompat.startActivity(ListadoAmigosActivity.this, intent, options .toBundle());
    }

}
