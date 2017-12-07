package org.proyecto.nerdynews;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;
import org.proyecto.nerdynews.amigos.AmigosAdapter;
import org.proyecto.nerdynews.amigos.ListadoAmigosActivity;
import org.proyecto.nerdynews.intereses.ListadoFavoritosActivity;
import org.proyecto.nerdynews.intereses.ListadoFavoritosRecyclerAdapter;
import org.proyecto.nerdynews.intereses.RecyclerItemClickListener;
import org.proyecto.nerdynews.models.Amigo;
import org.proyecto.nerdynews.models.Favorito;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class PerfilAmigoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoFavoritos;
    private ListadoFavoritosAmigosRecyclerAdapter adapterListadoFavoritos;
    private Favorito[] listaFavoritos;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private ArrayList<Amigo> listaAmigos;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //cogemos los extras
        Bundle extras = getIntent().getExtras();
        String nombreyapellidos = extras.getString("NOMBRE");
        String edad = extras.getString("EDAD");
        String intereses = extras.getString("INTERESES");
        url = extras.getString("DIBUJO");

        ImageView imagen = (ImageView) this.findViewById(R.id.image_paralax);

        if (url != null && !url.isEmpty()) {
            Picasso.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher_round)
                    .into(imagen);
        }

        TextView nombre = (TextView) this.findViewById(R.id.nombreyapellidos);
        TextView usuario = (TextView) this.findViewById(R.id.nombreusuario);
        nombre.setText(nombreyapellidos);
        usuario.setText(nombreyapellidos);
        TextView edadview = (TextView) this.findViewById(R.id.edad);
        edadview.setText(edad);

        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.litoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.linav_view);
        SharedPreferences prefs = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        View hView =  navigationView.getHeaderView(0);
        TextView user = hView.findViewById(R.id.tv_nombre);
        user.setText(prefs.getString("nombre", "Nerdy News"));
        navigationView.setNavigationItemSelectedListener(this);

        // Listado de intereses favorotios
        recyclerListadoFavoritos= (RecyclerView) findViewById(R.id.reciclerViewListadoInteresesAmigos);
        recyclerListadoFavoritos.setLayoutManager(new GridLayoutManager(this, 1));

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
        listaFavoritos= new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeInteresesAmigos.json", this), Favorito[].class);

        // Pasamos los datos al adaptador para crear la listaFavoritos
        adapterListadoFavoritos = new ListadoFavoritosAmigosRecyclerAdapter(listaFavoritos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerListadoFavoritos.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerListadoFavoritos.setAdapter(adapterListadoFavoritos);

        //Desmarcamos el interes como favorito
        recyclerListadoFavoritos.addOnItemTouchListener(new RecyclerItemClickListener(PerfilAmigoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ImageView ib = (ImageView) v.findViewById(R.id.cvimageFavorito);
                if (PerfilAmigoActivity.this.listaFavoritos[position].getImageFavorito().equals("@drawable/ic_favorite_black_24dp"))
                {
                    Toast.makeText(PerfilAmigoActivity.this, R.string.deletefavorito , Toast.LENGTH_SHORT).show();

                    // ListadoFavoritosActivity.this.listaFavoritos[position].setEsFavorito(0);
                    ib.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    PerfilAmigoActivity.this.listaFavoritos[position].setEsFavorito(0);

                }

            }
        }));
        adapterListadoFavoritos.notifyDataSetChanged();

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
    }

    public void irAtras(View v){
        finish();
    }

    public void anadirAmigo(View v){
        TextView usuario = findViewById(R.id.nombreusuario);
        Toast.makeText(this,"Ahora sigues a " + usuario.getText(),Toast.LENGTH_SHORT).show();

        // Código para añadir un nuevo amigo al JSON fakeMisAmigos.json
        //Cargamos el Json
        Type listType = new TypeToken<ArrayList<Amigo>>() { }.getType();
        String json = "fakeMisAmigos.json";
        ArrayList<Amigo> misAmigos = new GsonBuilder().create().fromJson(loadJSONFromAsset(json, this), listType);

        // Obtenemos los datos del usuario y creamos el nuevo amigo
        TextView edadview = (TextView) this.findViewById(R.id.edad);
        String[] nombreyapellidos = usuario.getText().toString().split(" ");
        String nombre = nombreyapellidos[0];
        String apellidos = nombreyapellidos[1];
        if (nombreyapellidos.length>2){
            apellidos = apellidos + " " + nombreyapellidos[2];
        }
        int edad = Integer.parseInt(edadview.getText().toString().split(":")[1]);
        Amigo nuevo = new Amigo(nombre,apellidos,edad,url,"masculino",1);
        // Añadimos el amigo nuevo al array de misAmigos
        misAmigos.add(nuevo);

        json = new Gson().toJson(misAmigos,listType);
    }

    public void enviarMensaje(View v){
        Toast.makeText(this,"Este es el boton enviarmensaje",Toast.LENGTH_SHORT).show();
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
