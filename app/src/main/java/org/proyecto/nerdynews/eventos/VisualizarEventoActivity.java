package org.proyecto.nerdynews.eventos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.models.Evento;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class VisualizarEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    MapView mapView;
    String coords;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private String title, texto, fecha, lugar, url, urlEvento;
    private static final int MY_PERMISSION_CALENDAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_evento);

        Uri data = getIntent().getData();
        if (data != null) {
            String temp = "";
            try {
                temp = URLDecoder.decode(data.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            eventoDesdeNotificacion(temp);
        } else if (!getIntent().getExtras().isEmpty()) {
            if (getIntent().getExtras().getString("Notificacion", "false").equals("true")) {
                String temp = "";
                try {
                    temp = URLDecoder.decode(getIntent().getStringExtra("url"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                eventoDesdeNotificacion(temp);
            } else {
                //obtener el valor pasado en el Bundle
                title = getIntent().getStringExtra("TITULO");
                texto = getIntent().getStringExtra("TEXTO");
                fecha = getIntent().getStringExtra("FECHA");
                lugar = getIntent().getStringExtra("LUGAR");
                coords = getIntent().getStringExtra("COORDSGPS");
                url = getIntent().getStringExtra("DIBUJO");
                urlEvento = "http://www.nerdynews.org/evento/" + title.trim().replace(" ", "_");
            }
        }

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

        Button bCalendario = (Button) this.findViewById(R.id.bCalendario);
        bCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    agregarEventoCalendario();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button bCompartir = (Button) findViewById(R.id.bCompartir);
        bCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, title + "\n" + texto.substring(0, 97) + "...\n" + urlEvento);
                startActivity(Intent.createChooser(intent, getString(R.string.compartir)));
            }
        });

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

    public void eventoDesdeNotificacion(String temp) {
        String nombreEvento = temp.substring(temp.lastIndexOf("/") + 1).replace("_", " ");
        // Obtenemos los elementos desde el fake .json
        Evento[] listaEventos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeEventos.json", this), Evento[].class);

        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equals(nombreEvento)) {
                title = evento.getTitulo();
                texto = evento.getResumen();
                fecha = evento.getFecha();
                lugar = evento.getLugar();
                coords = evento.getCoordGPS();
                url = evento.getImageUrl();
                urlEvento = "http://www.nerdynews.org/evento/" + title.trim().replace(" ", "_");
            }
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

    public void agregarEventoCalendario() throws ParseException {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", new Locale("es_ES"));
        Date date = format.parse(fecha);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        beginTime.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE),
                20, 00, 00);

        endTime.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE),
                22, 00, 00);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, texto)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, lugar);
        startActivity(intent);
    }
}
