package org.aficiones.noticias.nerdynews.intereses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.SimpleDividerItemDecoration;
import org.aficiones.noticias.nerdynews.Utils.AdMob;
import org.aficiones.noticias.nerdynews.Utils.InApp;
import org.aficiones.noticias.nerdynews.Utils.NavigationDrawerNavigate;
import org.aficiones.noticias.nerdynews.models.Favorito;

import java.util.ArrayList;
import java.util.List;

import static org.aficiones.noticias.nerdynews.LeerArchivoDatosFake.loadJSONFromAsset;

public class ListadoFavoritosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerListadoFavoritos;
    private ListadoFavoritosSwipeRecyclerAdapter adapterListadoFavoritos;
    private Favorito[] listaFavoritos;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private InApp inApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_favoritos);
        inApp = InApp.getInstance();
        inApp.serviceConectInAppBilling(this);


        // Menu laterar
        Toolbar toolbar = (Toolbar) findViewById(R.id.litoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.titfavoritos);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lidrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.linav_view);
        // Muestra IntersticialAd
        //si es falso, entonces no es premium y se tiene que mostrar la publicidad
        if(!inApp.checkPurchasedInAppProducts(this)) {
            AdMob.mostrarIntersticial();
        }
        else{
            NavigationDrawerNavigate.hideItem(navigationView);
        }
        SharedPreferences prefs = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        View hView =  navigationView.getHeaderView(0);
        TextView nombre = hView.findViewById(R.id.tv_nombre);
        nombre.setText(prefs.getString("nombre", "Nerdy News"));
        navigationView.setNavigationItemSelectedListener(this);

        // Listado de intereses favorotios
        recyclerListadoFavoritos= (RecyclerView) findViewById(R.id.reciclerViewListadoFavoritos);
        recyclerListadoFavoritos.setLayoutManager(new GridLayoutManager(this, 1));

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
        listaFavoritos= new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeInteresesFavoritos.json", this), Favorito[].class);
        List<Favorito> favoritos = new ArrayList<Favorito>();
        // Pasamos los datos al adaptador para crear la listaFavoritos
        for(int i= 0; i < listaFavoritos.length; i++ ){
            favoritos.add(listaFavoritos[i]);
        }
        adapterListadoFavoritos = new ListadoFavoritosSwipeRecyclerAdapter(favoritos, getApplicationContext());
        // Añade un separador entre los elementos de la lista
        recyclerListadoFavoritos.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerListadoFavoritos.setAdapter(adapterListadoFavoritos);

        adapterListadoFavoritos.notifyDataSetChanged();

        // Oculta el circulo de cargar
        swipeRefreshLayout.setRefreshing(false);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
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
        return NavigationDrawerNavigate.Navigate(item,this,inApp);
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
                xMark = ContextCompat.getDrawable(ListadoFavoritosActivity.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) ListadoFavoritosActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
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
                ListadoFavoritosSwipeRecyclerAdapter testAdapter = (ListadoFavoritosSwipeRecyclerAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ListadoFavoritosSwipeRecyclerAdapter adapter = (ListadoFavoritosSwipeRecyclerAdapter) recyclerListadoFavoritos.getAdapter();
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
        mItemTouchHelper.attachToRecyclerView( recyclerListadoFavoritos);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerListadoFavoritos.addItemDecoration(new RecyclerView.ItemDecoration() {

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

