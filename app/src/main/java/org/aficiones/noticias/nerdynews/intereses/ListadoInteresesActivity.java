package org.aficiones.noticias.nerdynews.intereses;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.gson.GsonBuilder;

import org.aficiones.noticias.nerdynews.BuildConfig;
import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.RateMyApp;
import org.aficiones.noticias.nerdynews.SimpleDividerItemDecoration;
import org.aficiones.noticias.nerdynews.Utils.AdMob;
import org.aficiones.noticias.nerdynews.Utils.InApp;
import org.aficiones.noticias.nerdynews.Utils.NavigationDrawerNavigate;
import org.aficiones.noticias.nerdynews.models.Interes;

import static org.aficiones.noticias.nerdynews.Firebase.MyFirebaseMessagingService.displayNotification;
import static org.aficiones.noticias.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class ListadoInteresesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = ListadoInteresesActivity.class.getSimpleName();

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoInteres;
    private ListadoInteresesRecyclerAdapter adapterListadoInteres;
    private Interes[] listaInteres;
    private static final int MY_PERMISSION_LOCATION = 1;
    // Declare variables for pending intent and fence receiver.

    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mPendingIntent;
    private LocationFenceReceiver mLocationFenceReceiver;
    private InApp inApp;

    private static final String IN_LOCATION_FENCE_KEY = "IN_LOCATION_FENCE_KEY";
    private static final String EXITING_LOCATION_FENCE_KEY = "EXITING_LOCATION_FENCE_KEY";
    private static final String ENTERING_LOCATION_FENCE_KEY = "ENTERING_LOCATION_FENCE_KEY";

    public static final int STATUS_IN = 0;
    public static final int STATUS_OUT = 1;
    public static final int STATUS_ENTERING = 2;
    public static final int STATUS_EXITING = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_intereses);
        //preparamos el InApp
        inApp = InApp.getInstance();
        inApp.serviceConectInAppBilling(this);

        // Inicializamos el api awareness
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

        mLocationFenceReceiver = new LocationFenceReceiver();
        Intent intent = new Intent(LocationFenceReceiver.FENCE_RECEIVER_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

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
        // Mostramos el banner
        //si es falso, entonces no es premium y se tiene que mostrar la publicidad
        if(!inApp.checkPurchasedInAppProducts(this)) {
            AdMob.mostrarBanner(findViewById(R.id.adView));
        }
        else{
            NavigationDrawerNavigate.hideItem(navigationView);
            //buscamos el adMob y nos lo cepillamos
            com.google.android.gms.ads.AdView adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
            if(adView!=null){
                adView.setVisibility(View.GONE);
            }
        }
        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        View hView =  navigationView.getHeaderView(0);
        TextView nombre = hView.findViewById(R.id.tv_nombre);
        nombre.setText(prefs.getString("nombre", "Nerdy News"));
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

        new RateMyApp(this).app_launched();
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
        if(NavigationDrawerNavigate.isOpened(this)){
            NavigationDrawerNavigate.OnBackPressed(this);
        }
        else{
            this.finish();
        }
    }

    // Metodo cuando se hace click en los items del menú
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return NavigationDrawerNavigate.Navigate(item,this,inApp);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG,"PERMISION OK ----");
                    registerFences();
                    //getLocationSnapshot();
                } else {
                    Log.e(TAG, "Location permission denied.");
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerFences();
        registerReceiver(mLocationFenceReceiver, new IntentFilter(LocationFenceReceiver.FENCE_RECEIVER_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterFences();
        unregisterReceiver(mLocationFenceReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void registerFences() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            //se debe mostrar un dialog indicando que se van a solicitar permisos
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final Activity _this = this;
            builder.setMessage(R.string.permisomessage).setTitle(R.string.permisotitle).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ActivityCompat.requestPermissions(_this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSION_LOCATION);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            AwarenessFence inLocationFence = LocationFence.in(40.4642823, -3.6179828, 200, 1);

            Awareness.FenceApi.updateFences(
                    mGoogleApiClient,
                    new FenceUpdateRequest.Builder()
                            .addFence(IN_LOCATION_FENCE_KEY, inLocationFence, mPendingIntent)
                            .build())
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.e(TAG,"Fence Registered");
                            } else {
                                Log.e(TAG, "Fence Not Registered");
                            }
                        }
                    });
        }
    }
    private void unregisterFences() {
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(IN_LOCATION_FENCE_KEY)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                Log.e(TAG,"Fence Removed");
            }

            @Override
            public void onFailure(@NonNull Status status) {
                Log.e(TAG, "Fence Not Removed");
            }
        });
    }

    private void setHeadphoneState(int status) {
        switch (status) {
            case STATUS_IN:
                Log.e(TAG, "satus in");
                Bundle extras = new Bundle();
                extras.putString("Notificacion","true");
                extras.putString("url","http://www.nerdynews.org/evento/Heroes_Comic_Con_Madrid_2017");
                displayNotification("Evento cercano","Pulsa aquí para ver la información del evento",extras,getApplicationContext());

                break;
            case STATUS_OUT:
                Log.e(TAG, "satus out");
                break;
            case STATUS_ENTERING:
                Log.e(TAG, "satus entering");
                break;
            case STATUS_EXITING:
                Log.e(TAG, "satus exiting");
                break;
        }
    }
    class LocationFenceReceiver extends BroadcastReceiver {
        private static final String FENCE_RECEIVER_ACTION =
                BuildConfig.APPLICATION_ID + "FENCE_RECEIVER_ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"ON RECIVE---");
            FenceState fenceState = FenceState.extract(intent);

            if (TextUtils.equals(fenceState.getFenceKey(), IN_LOCATION_FENCE_KEY)) {
                Log.e(TAG,"IN LOCATION FENCE KEY");
                switch (fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        setHeadphoneState(STATUS_IN);
                        break;
                    case FenceState.FALSE:
                        setHeadphoneState(STATUS_OUT);
                        break;
                    case FenceState.UNKNOWN:
                        Log.e(TAG,"Oops, your headphone status is unknown!");
                        break;
                }
            } else if (TextUtils.equals(fenceState.getFenceKey(), EXITING_LOCATION_FENCE_KEY)) {
                switch (fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        setHeadphoneState(STATUS_EXITING);
                        break;
                    case FenceState.FALSE:

                        break;
                    case FenceState.UNKNOWN:
                        Log.e(TAG, "Oops, your headphone status is unknown!");
                        break;
                }
            } else if (TextUtils.equals(fenceState.getFenceKey(), ENTERING_LOCATION_FENCE_KEY)) {
                switch (fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        setHeadphoneState(STATUS_ENTERING);
                        break;
                    case FenceState.FALSE:

                        break;
                    case FenceState.UNKNOWN:
                        Log.e(TAG, "Oops, your headphone status is unknown!");
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inApp.comprobarCompra(requestCode,resultCode,data,this);
    }
}

