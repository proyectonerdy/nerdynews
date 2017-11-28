package org.proyecto.nerdynews.intereses;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;
import org.proyecto.nerdynews.models.Interes;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class ListadoInteresesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoInteres;
    private ListadoInteresesRecyclerAdapter adapterListadoInteres;
    private Interes[] listaInteres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_intereses);

        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.litoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.titintereses);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.linav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Listado de intereses disponisbles
        recyclerListadoInteres= (RecyclerView) findViewById(R.id.reciclerViewListadoInteres);
        recyclerListadoInteres.setLayoutManager(new GridLayoutManager(this, 1));

        // Permite recargar los datos de la lista haciendo scroll en lo alto de la lista
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.liswipe_refresh_layout);
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
        listaInteres= new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeInteresesDisponibles.json", this), Interes[].class);

        // Pasamos los datos al adaptador para crear la lista
        adapterListadoInteres = new ListadoInteresesRecyclerAdapter(listaInteres, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerListadoInteres.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerListadoInteres.setAdapter(adapterListadoInteres);

        //Marcamos el interes como favorito
        recyclerListadoInteres.addOnItemTouchListener(new RecyclerItemClickListener(ListadoInteresesActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ImageView ib = (ImageView) v.findViewById(R.id.cvimageFavorito);

                if (ListadoInteresesActivity.this.listaInteres[position].getImageFavorito().equals("@drawable/ic_favorite_black_24dp"))
                {
                    Toast.makeText(ListadoInteresesActivity.this, R.string.existsfavorito , Toast.LENGTH_SHORT).show();
                  
                }
                else
                {
                    Toast.makeText(ListadoInteresesActivity.this, R.string.addinteresfavorito, Toast.LENGTH_SHORT).show();
                   ib.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
            }
        }));
        adapterListadoInteres.notifyDataSetChanged();

        // Oculta el circulo de cargar
       swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        NavigationDrawerNavigate.OnBackPressed(this);
    }

    // Metodo cuando se hace click en los items del menú
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return NavigationDrawerNavigate.Navigate(item,this);
    }
}

