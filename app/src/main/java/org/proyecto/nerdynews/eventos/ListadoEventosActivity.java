package org.proyecto.nerdynews.eventos;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.GsonBuilder;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.SimpleDividerItemDecoration;
import org.proyecto.nerdynews.models.Evento;

import static org.proyecto.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class ListadoEventosActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoEventos;
    private ListadoEventosRecyclerAdapter adapterListadoEventos;
    private Evento[] listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_eventos);

        recyclerListadoEventos = (RecyclerView) findViewById(R.id.reciclerViewListadoEventos);
        recyclerListadoEventos.setLayoutManager(new GridLayoutManager(this, 1));

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
        listaEventos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeEventos.json", this), Evento[].class);

        // Pasamos los datos al adaptador para crear la lista
        adapterListadoEventos = new ListadoEventosRecyclerAdapter(listaEventos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerListadoEventos.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerListadoEventos.setAdapter(adapterListadoEventos);
        adapterListadoEventos.notifyDataSetChanged();

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
    }


}
