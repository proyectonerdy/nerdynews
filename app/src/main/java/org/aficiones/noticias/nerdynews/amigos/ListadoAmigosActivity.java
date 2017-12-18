package org.aficiones.noticias.nerdynews.amigos;

/**
 * Created by MyC on 22-11-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.SimpleDividerItemDecoration;
import org.aficiones.noticias.nerdynews.Utils.GlobalData;
import org.aficiones.noticias.nerdynews.Utils.InApp;
import org.aficiones.noticias.nerdynews.Utils.NavigationDrawerNavigate;
import org.aficiones.noticias.nerdynews.models.Amigo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import static org.aficiones.noticias.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

/**
 * Created by MyC on 22-11-2017.
 */

public class ListadoAmigosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MisAmigosAdapter amigosAdapter;
    private ArrayList<Amigo> listaAmigos;
    private FloatingActionButton button;
    private NavigationView navigationView;
    private InApp inApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_amigos);
        inApp = new InApp();
        inApp.serviceConectInAppBilling(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( R.string.misamigos);

        DrawerLayout drawer = findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        SharedPreferences prefs = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        View hView =  navigationView.getHeaderView(0);
        TextView nombre = hView.findViewById(R.id.tv_nombre);
        nombre.setText(prefs.getString("nombre", "Nerdy News"));
        navigationView.setNavigationItemSelectedListener(this);
        if(inApp.checkPurchasedInAppProducts(this)) {
            NavigationDrawerNavigate.hideItem(navigationView);
        }
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
        return NavigationDrawerNavigate.Navigate(item, this,inApp);
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


    public ArrayList<Amigo> getListaAmigos() {
        return listaAmigos;
    }

    public void setListaAmigos(ArrayList<Amigo> listaAmigos) {
        this.listaAmigos = listaAmigos;
    }

    private void cargarDatosLista() {
        // Obtenemos los elementos desde el fake .json
        GlobalData gd = GlobalData.getInstance();
        Type listType = new TypeToken<ArrayList<Amigo>>() {
        }.getType();
        if(gd.getMisAmigos()==null) {
            listaAmigos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeMisAmigos.json", this), listType);
        }
        else{
            listaAmigos = gd.getMisAmigos();
        }
        gd.setMisAmigos(listaAmigos);
        // Pasamos los datos al adaptador para crear la lista
        amigosAdapter = new MisAmigosAdapter(listaAmigos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(amigosAdapter);
        amigosAdapter.notifyDataSetChanged();

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setFilterTouchesWhenObscured(true);

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    public void gotoAmigo(View v){
        Intent intent = new Intent(this, PerfilAmigoActivity.class);
        TextView nombre = (TextView) v.findViewById(R.id.txtNombre);
        TextView edad = (TextView) v.findViewById(R.id.txtEdad);
        TextView intereses = (TextView) v.findViewById(R.id.txtIntereses);
        TextView identificador = (TextView) v.findViewById(R.id.txtIdentificador);
        ImageView drawable = (ImageView) v.findViewById(R.id.ivImagenAmigo);
        intent.putExtra("NOMBRE",nombre.getText());
        intent.putExtra("EDAD",edad.getText());
        intent.putExtra("INTERESES",intereses.getText());
        intent.putExtra("IDENTIFICADOR",identificador.getText());
        intent.putExtra("DIBUJO",(String)drawable.getTag());
        intent.putExtra("AMIGO",true);
        ActivityOptionsCompat options = ActivityOptionsCompat. makeSceneTransitionAnimation(ListadoAmigosActivity.this, new Pair<View, String>(v.findViewById(R.id.ivImagenAmigo), getString(R.string.transition_name_img_amigo)));
        ActivityCompat.startActivity(ListadoAmigosActivity.this, intent, options .toBundle());
    }

    @Override
    public void onResume(){
        super.onResume();
        cargarDatosLista();
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(ListadoAmigosActivity.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) ListadoAmigosActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                MisAmigosAdapter testAdapter = (MisAmigosAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                MisAmigosAdapter adapter = (MisAmigosAdapter) recyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView( recyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inApp.comprobarCompra(requestCode,resultCode,data,this);
    }
}
