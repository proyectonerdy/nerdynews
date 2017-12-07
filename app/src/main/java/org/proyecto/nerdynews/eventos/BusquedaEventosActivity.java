package org.proyecto.nerdynews.eventos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.GsonBuilder;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.Utils.NavigationDrawerNavigate;
import org.proyecto.nerdynews.models.Evento;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class BusquedaEventosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoEventos;
    private ListadoEventosRecyclerAdapter adapterListadoEventos;
    private Evento[] listaEventos;
    private Double latitud;
    private Double longitud;
    private static final int MY_PERMISSION_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_eventos);

        final Spinner spinner = (Spinner) findViewById(R.id.spinneropciones);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opcionesbusqueda, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_LOCATION);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_LOCATION);
        }

        LocationListener locationListener = new BusquedaEventosLocation();
        if ( Build.VERSION.SDK_INT < 23 ||
                (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                            }
                        }
                    });
        }

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
                cargarDatosLista(spinner.getSelectedItem().toString().equals(getString(R.string.proximidad))?0:1);
            }
        });

        // Cargamos la lista
        cargarDatosLista(0);
    }

    private void cargarDatosLista(int pos){
        // Obtenemos los elementos desde el fake .json
        listaEventos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeEventos.json", this), Evento[].class);
        Evento[] eventosnuevos = new Evento[listaEventos.length];
        eventosnuevos[0]=listaEventos[0];
        int rellenos = 1;
        if(pos==0){
            //hay que ordenar los eventos por proximidad del GPS
            //aqui hay que obtener las coordenadas GPS del dispositivo
            if(latitud==null || longitud==null ){
                eventosnuevos = listaEventos;
            }
            else{
                for(int i = 1; i < listaEventos.length; i++){
                    Evento e = listaEventos[i];
                    if(e.getCoordGPS()==null || e.getCoordGPS().isEmpty() || e.getCoordGPS().equals("")){
                        //lo ponemos como ultimo hueco que este a null
                        eventosnuevos[rellenos]=e;
                        rellenos++;
                    }
                    else{
                        //sino es igual a eso es que tiene una fecha
                        String coordgps = e.getCoordGPS();
                        String[] cords = coordgps.split(",");
                        try {
                            double latitudevento1 = Double.parseDouble(cords[0]);
                            double longitudevento1 = Double.parseDouble(cords[1]);
                            boolean colocado = false;
                            for(int j = 0; j < rellenos;j++){
                                Evento ev1 = eventosnuevos[j];
                                try{
                                    if(ev1.getCoordGPS()==null || ev1.getCoordGPS().isEmpty() || ev1.getCoordGPS().equals("")){
                                        eventosnuevos[rellenos]=eventosnuevos[j];
                                        eventosnuevos[j] = e;
                                        rellenos++;
                                        colocado = true;
                                        break;
                                    }
                                    String coordgps2 = ev1.getCoordGPS();
                                    String[] cords2 = coordgps2.split(",");
                                    double latitudevento2 = Double.parseDouble(cords2[0]);
                                    double longitudevento2 = Double.parseDouble(cords2[1]);
                                    if(distance(latitud,latitudevento1,longitud,longitudevento1,0,0) >= distance(latitud,latitudevento2,longitud,longitudevento2,0,0)){
                                        continue;
                                    }
                                    else{
                                        //toca ponerlo en esta posicion
                                        for(int x = listaEventos.length-1; x > j; x--){
                                            eventosnuevos[x] = eventosnuevos[x-1];
                                        }
                                        eventosnuevos[j]=e;
                                        colocado = true;
                                        rellenos++;
                                        break;
                                    }
                                }
                                catch(Exception ex){
                                    //si da excepcion, se inseta en la posicion actual y se arrastra al resto
                                    for(int x = listaEventos.length-1; x > j ; x--){
                                        eventosnuevos[x] = eventosnuevos[x-1];
                                    }
                                    eventosnuevos[j]=e;
                                    rellenos++;
                                    break;
                                }
                            }
                            //si sale aqui y no esta puesto aun, tenemos que colocarlo en la ultim posicion
                            if(!colocado) {
                                eventosnuevos[rellenos] = e;
                                rellenos++;
                            }
                        }
                        catch(Exception ex){
                            //lo ponemos como ultima fecha rellenada
                            for(int j = 0; j < listaEventos.length;j++){
                                if(eventosnuevos[j]==null){
                                    eventosnuevos[j]=e;
                                    break;
                                }
                                continue;
                            }
                            rellenos++;
                        }
                    }
                }
            }

        }
        else{
            //hay que ordenar los eventos por Fecha
            //hay que obtener las fechas de los eventos
            for(int i = 1; i < listaEventos.length; i++){
                Evento e = listaEventos[i];
                if(e.getFecha().equals("00/00/0000")){
                    //lo ponemos como primero
                    for(int j = listaEventos.length-1; j > 0 ; j--){
                        eventosnuevos[j] = eventosnuevos[j-1];
                    }
                    eventosnuevos[0]=e;
                    rellenos++;
                }
                else{
                    //sino es igual a eso es que tiene una fecha
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    Date d = null;
                    try {
                        d = format.parse(e.getFecha());
                        boolean colocado = false;
                        for(int j = 0; j < rellenos;j++){
                            Evento ev1 = eventosnuevos[j];
                            Date d2 = null;
                            try{
                                d2 = format.parse(ev1.getFecha());
                                if(d.compareTo(d2)>=0){
                                    continue;
                                }
                                else{
                                    //toca ponerlo en esta posicion
                                    for(int x = listaEventos.length-1; x > j; x--){
                                        eventosnuevos[x] = eventosnuevos[x-1];
                                    }
                                    eventosnuevos[j]=e;
                                    colocado = true;
                                    rellenos++;
                                    break;
                                }
                            }
                            catch(Exception ex){
                                //si da excepcion, se inseta en la posicion actual y se arrastra al resto
                                for(int x = listaEventos.length-1; x > j ; x--){
                                    eventosnuevos[x] = eventosnuevos[x-1];
                                }
                                eventosnuevos[j]=e;
                                rellenos++;
                                break;
                            }
                        }
                        //si sale aqui y no esta puesto aun, tenemos que colocarlo en la ultim posicion
                        if(!colocado) {
                            eventosnuevos[rellenos] = e;
                            rellenos++;
                        }
                    }
                    catch(Exception ex){
                        //lo ponemos como ultima fecha rellenada
                        for(int j = 0; j < listaEventos.length;j++){
                            if(eventosnuevos[j]==null){
                                eventosnuevos[j]=e;
                                break;
                            }
                            continue;
                        }
                        rellenos++;
                    }
                }
            }
        }

        // Pasamos los datos al adaptador para crear la lista
        adapterListadoEventos = new ListadoEventosRecyclerAdapter(eventosnuevos, getApplicationContext());
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        cargarDatosLista(pos);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        //esta funcion nunca se llamara
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    private class BusquedaEventosLocation implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
