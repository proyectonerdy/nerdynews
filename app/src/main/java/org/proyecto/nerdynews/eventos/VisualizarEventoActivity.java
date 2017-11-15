package org.proyecto.nerdynews.eventos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.proyecto.nerdynews.R;

public class VisualizarEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    MapView mapView;
    String coords;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_evento);

        //obtener el valor pasado en el Bundle
        String title = getIntent().getStringExtra("TITULO");
        String texto = getIntent().getStringExtra("TEXTO");
        String fecha = getIntent().getStringExtra("FECHA");
        String lugar = getIntent().getStringExtra("LUGAR");
        coords = getIntent().getStringExtra("COORDSGPS");
        String url = getIntent().getStringExtra("DIBUJO");

        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView titulo = (TextView) this.findViewById(R.id.tituloEventoVisualizar);
        titulo.setText(title);
        TextView textoview = (TextView) this.findViewById(R.id.textoEventoVisualizar);
        textoview.setText(texto);
        TextView fechaview = (TextView) this.findViewById(R.id.cvFechaEvento);
        fechaview.setText(fecha);
        TextView lugarview = (TextView) this.findViewById(R.id.cvLugarEvento);
        lugarview.setText(lugar);
        ImageView imagen = (ImageView) this.findViewById(R.id.imagenEvento);

        if (url != null && !url.isEmpty()) {
            Picasso.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher_round)
                    .into(imagen);
        }


        mapView = (MapView) findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);

        if (coords != null && !coords.isEmpty()) {
            mapView.getMapAsync(this);
        } else {
            mapView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        String[] latLong = coords.split(",");

        LatLng lugar = new LatLng(Double.parseDouble(latLong[0]), Double.parseDouble(latLong[1]));
        map.addMarker(new MarkerOptions().position(lugar));
        map.moveCamera(CameraUpdateFactory.newLatLng(lugar));
    }
}
