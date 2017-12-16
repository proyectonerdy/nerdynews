package org.aficiones.noticias.nerdynews.amigos;

import android.content.Context;
import android.content.Intent;
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

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.SimpleDividerItemDecoration;
import org.aficiones.noticias.nerdynews.Utils.GlobalData;
import org.aficiones.noticias.nerdynews.Utils.NavigationDrawerNavigate;
import org.aficiones.noticias.nerdynews.mensajes.LeerMensajeActivity;
import org.aficiones.noticias.nerdynews.models.Amigo;
import org.aficiones.noticias.nerdynews.models.Favorito;
import org.aficiones.noticias.nerdynews.models.HistorialMensaje;
import org.aficiones.noticias.nerdynews.models.Interes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import static org.aficiones.noticias.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class PerfilAmigoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoFavoritos;
    private ListadoFavoritosAmigosRecyclerAdapter adapterListadoFavoritos;
    private Favorito[] listaFavoritos;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private String intereses;

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
        String identificadort = extras.getString("IDENTIFICADOR");
        boolean esAmigo = extras.getBoolean("AMIGO");
        intereses = extras.getString("INTERESES");
        intereses = intereses.substring(intereses.indexOf(":")+1);
        String[] intarray = intereses.split(",");
        listaFavoritos = new Favorito[intarray.length];
        //lo obtenemos del listado de Intereses Disponibles
        Interes[] listaDisponibles= new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeInteresesDisponibles.json", this), Interes[].class);
        int i = 0;
        for(String valor:intarray){
           for(Interes f: listaDisponibles){
               if(f.getTitulo().equals(valor.trim())){
                   listaFavoritos[i] = new Favorito(f.getTitulo(), f.getResumen(), f.getImageUrl(), null,0);
                   i++;
                   break;
               }
           }
        }
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
        TextView identificador = (TextView) this.findViewById(R.id.identificador);
        identificador.setText(identificadort);
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
        TextView numintereses = (TextView) this.findViewById(R.id.interesestextnumber);
        numintereses.setText(R.string.interes);
        ImageView imagenadd = (ImageView)this.findViewById(R.id.new_people);
        if(esAmigo){
            imagenadd.setImageResource(R.drawable.ic_person_remove);
            imagenadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    borrarAmigo(view);
                }
            });
        }
        else{
            imagenadd.setImageResource(R.drawable.ic_person_add);
            imagenadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    anadirAmigo(view);
                }
            });
        }

        // Cargamos la lista
        cargarDatosLista();
    }

    private void cargarDatosLista(){

        // Pasamos los datos al adaptador para crear la listaFavoritos
        adapterListadoFavoritos = new ListadoFavoritosAmigosRecyclerAdapter(listaFavoritos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerListadoFavoritos.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerListadoFavoritos.setAdapter(adapterListadoFavoritos);

        adapterListadoFavoritos.notifyDataSetChanged();

    }

    public void irAtras(View v){
        finish();
    }

    public void anadirAmigo(View v){
        TextView usuario = findViewById(R.id.nombreusuario);
        Toast.makeText(this,this.getString(R.string.anyadiramigo)+" " + usuario.getText(),Toast.LENGTH_SHORT).show();

        // Código para añadir un nuevo amigo al JSON fakeMisAmigos.json
        //Cargamos los amigos del JSON si no estan
        GlobalData gd = GlobalData.getInstance();
        Type listType = new TypeToken<ArrayList<Amigo>>() { }.getType();
        String json = "fakeMisAmigos.json";
        ArrayList<Amigo> misAmigos = null;
        if(gd.getMisAmigos()==null) {
            misAmigos = new GsonBuilder().create().fromJson(loadJSONFromAsset(json, this), listType);
        }
        else{
            misAmigos = gd.getMisAmigos();
        }

        // Obtenemos los datos del usuario y creamos el nuevo amigo
        TextView edadview = (TextView) this.findViewById(R.id.edad);
        TextView idview = (TextView) this.findViewById(R.id.identificador);
        String[] nombreyapellidos = usuario.getText().toString().split(" ");
        String nombre = nombreyapellidos[0];
        String apellidos = nombreyapellidos[1];
        if (nombreyapellidos.length>2){
            apellidos = apellidos + " " + nombreyapellidos[2];
        }
        int edad = Integer.parseInt(edadview.getText().toString().split(":")[1]);
        int identificador = Integer.parseInt(idview.getText().toString());
        Random rand = new Random();
        Amigo nuevo = new Amigo(nombre,apellidos,edad,url,"masculino",identificador );
        // Añadimos el amigo nuevo al array de misAmigos
        nuevo.setIntereses(intereses);
        misAmigos.add(nuevo);
        gd.setMisAmigos(misAmigos);
        ImageView imagenadd = (ImageView)this.findViewById(R.id.new_people);
        imagenadd.setImageResource(R.drawable.ic_person_remove);
        imagenadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarAmigo(view);
            }
        });
    }

    public void borrarAmigo(View v){
        TextView usuario = findViewById(R.id.nombreusuario);
         Toast.makeText(this,this.getString(R.string.borraramigo)+" " + usuario.getText(),Toast.LENGTH_SHORT).show();

        // Código para añadir un nuevo amigo al JSON fakeMisAmigos.json
        //Cargamos los amigos del JSON si no estan
        GlobalData gd = GlobalData.getInstance();
        Type listType = new TypeToken<ArrayList<Amigo>>() { }.getType();
        String json = "fakeMisAmigos.json";
        ArrayList<Amigo> misAmigos = null;
        if(gd.getMisAmigos()==null) {
            misAmigos = new GsonBuilder().create().fromJson(loadJSONFromAsset(json, this), listType);
        }
        else{
            misAmigos = gd.getMisAmigos();
        }

        String[] nombreyapellidos = usuario.getText().toString().split(" ");
        String nombre = nombreyapellidos[0].trim();
        String apellidos = nombreyapellidos[1].trim();
        if (nombreyapellidos.length>2){
            apellidos = apellidos + " " + nombreyapellidos[2].trim();
        }

        //buscamos al amigo
        for(Amigo a:misAmigos){
            if(a.getNombre().trim().equals(nombre) && a.getApellido().trim().equals(apellidos)){
                //es este amigo
                misAmigos.remove(a);
                break;
            }
        }

        gd.setMisAmigos(misAmigos);
        ImageView imagenadd = (ImageView)this.findViewById(R.id.new_people);
        imagenadd.setImageResource(R.drawable.ic_person_add);
        imagenadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anadirAmigo(view);
            }
        });
    }

    public void enviarMensaje(View v){
        GlobalData gd = GlobalData.getInstance();
        ArrayList<HistorialMensaje> hm = null;
        Type listType = new TypeToken<ArrayList<HistorialMensaje>>() {
        }.getType();
        if(gd.getListahistoriasmensaje()!=null){
            hm = gd.getListahistoriasmensaje();
        }
        else{
            hm = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeMensajes.json", this), listType);
        }

        //tenemos que ver si este amigo esta en el listado de mensajes
        int idchat = -1;
        int idamigo= -1;
        Type listType2 = new TypeToken<ArrayList<Amigo>>(){}.getType();
        ArrayList<Amigo> listatodosAmigos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeAmigos.json", this),listType2);

        //buscamos el amigo que estamos buscando
        TextView usuario = findViewById(R.id.nombreusuario);
        String[] nombreyapellidos = usuario.getText().toString().split(" ");
        String nombre = nombreyapellidos[0].trim();
        String apellidos = nombreyapellidos[1].trim();
        int amigo = -1;
        if (nombreyapellidos.length>2){
            apellidos = apellidos + " " + nombreyapellidos[2].trim();
        }

        //buscamos al amigo
        for(Amigo a:listatodosAmigos){
            if(a.getNombre().trim().equals(nombre) && a.getApellido().trim().equals(apellidos)){
               amigo = a.getId();
            }
        }


        for(HistorialMensaje h:hm){
            if(h.getAmigoId()==amigo){
                idchat = h.getId();
                idamigo = h.getAmigoId();
                break;
            }
        }

        if(idchat==-1 || idamigo==-1){
            idamigo = amigo;
            //generamos un nuevo id para el chat
            idchat =hm.size() + 1;
        }

        Intent intent = new Intent(this, LeerMensajeActivity.class);
        intent.putExtra("idChat", idchat);
        intent.putExtra("amigoid",idamigo);
        this.startActivity(intent);
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
