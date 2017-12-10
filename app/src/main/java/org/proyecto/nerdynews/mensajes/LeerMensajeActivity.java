package org.proyecto.nerdynews.mensajes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.Utils.GlobalData;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;
import org.proyecto.nerdynews.models.Amigo;
import org.proyecto.nerdynews.models.HistorialMensaje;
import org.proyecto.nerdynews.models.Mensaje;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class LeerMensajeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerListadoMensajes;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<HistorialMensaje> listaMensajes;
    private LeerMensajesRecyclerAdapter adapterListadoMensajes;
    private Amigo[] amigos;
    private int idChat;
    private int idAmigo;
    private FloatingActionButton fabResponder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer_mensaje);

        Bundle extras = getIntent().getExtras();
        idChat = extras.getInt("idChat");
        idAmigo = extras.getInt("amigoid");
        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.mensajes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        View hView =  navigationView.getHeaderView(0);
        TextView nombre = hView.findViewById(R.id.tv_nombre);
        nombre.setText(prefs.getString("nombre", "Nerdy News"));
        navigationView.setNavigationItemSelectedListener(this);*/

        // Listado de eventos
        recyclerListadoMensajes = (RecyclerView) findViewById(R.id.reciclerViewLeerMensajes);
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
        GlobalData gd = GlobalData.getInstance();
        Type listType = new TypeToken<ArrayList<HistorialMensaje>>() {
        }.getType();
        if(gd.getListahistoriasmensaje()==null){
            listaMensajes = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeMensajes.json", this), listType);
        }
        else{
            listaMensajes = gd.getListahistoriasmensaje();
        }

        gd.setListahistoriasmensaje(listaMensajes);

        amigos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeAmigos.json", this), Amigo[].class);

        for (HistorialMensaje chat : listaMensajes) {
            if (chat.getId() == idChat) {
                // Pasamos los datos al adaptador para crear la lista
                adapterListadoMensajes = new LeerMensajesRecyclerAdapter(chat.getHistorial(), amigos, getApplicationContext());
                // Añade un separador entre los elementos de la lista
                recyclerListadoMensajes.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
                recyclerListadoMensajes.setAdapter(adapterListadoMensajes);
                adapterListadoMensajes.notifyDataSetChanged();
            }
        }

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
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

    public void emoticono(View v){
        Toast.makeText(this,getString(R.string.faltaimplementar),Toast.LENGTH_SHORT).show();
    }

    public void enviarmensajetexto(View v){
        //tenemos que añadir el nuevo mensaje en este listado
        EditText texto = (EditText) findViewById(R.id.mensajetexto);
        GlobalData gd = GlobalData.getInstance();
        if(texto==null || texto.getText()==null || texto.getText().length() < 1){
            //no hacemos nada
            return;
        }

        //vamos a crear un nuevo mensaje para el amigo asociado
        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date actual = new Date();
        Mensaje m = new Mensaje(gd.getIdmensaje(),0,texto.getText().toString(),formato.format(actual));
        texto.setText("");
        //hay que meterlo ahora en el historial
        //buscamos el historial asociado
        boolean encontrado = false;
        for(HistorialMensaje h:listaMensajes){
            if(h.getAmigoId()==idAmigo){
                h.getHistorial().add(m);
                encontrado = true;
                break;
            }
        }

        if(!encontrado){
            ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
            mensajes.add(m);
            listaMensajes.add(new HistorialMensaje(idChat,idAmigo,mensajes ));
        }
        //actualizamos el gd
        gd.setListahistoriasmensaje(listaMensajes);
        //refrescamos lista
        cargarDatosLista();
    }
}
