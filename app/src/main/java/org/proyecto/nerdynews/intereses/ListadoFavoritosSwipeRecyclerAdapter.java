package org.proyecto.nerdynews.intereses;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.models.Favorito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by Ana 11/11/2017.
 */

public class ListadoFavoritosSwipeRecyclerAdapter extends RecyclerView.Adapter<ListadoFavoritosSwipeRecyclerAdapter.ListadoFavoritosViewHolder>{

    private List<Favorito> listadoFavoritos = null;
    private List<Favorito> listadoFavoritosRemoval = null;
    private Context mContext;
    boolean undoOn;

    private static final int PENDING_REMOVAL_TIMEOUT = 3000;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<Favorito, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public ListadoFavoritosSwipeRecyclerAdapter(List<Favorito> listadoIntereses, Context ctx) {
        this.listadoFavoritos = listadoIntereses;
        this.mContext = ctx;
        listadoFavoritosRemoval = new ArrayList<>();
    }

    @Override
    public ListadoFavoritosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_interes_favoritos, parent, false);
        return new ListadoFavoritosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoFavoritosViewHolder holder, int position) {
        final Favorito favorito = listadoFavoritos.get(position);

        holder.txtTitulo.setText(favorito.getTitulo());
        holder.txtResumen.setText(favorito.getResumen());

        String nombrei=favorito.getImageUrl();; //nombre fichero sin extensión
        int res_imageni = this.mContext.getResources().getIdentifier(nombrei, "drawable",this.mContext.getPackageName());
        holder.imgInteres.setImageResource(res_imageni);

        if (listadoFavoritosRemoval.contains(favorito)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.GREEN);
            holder.txtTitulo.setVisibility(View.GONE);
            holder.txtResumen.setVisibility(View.GONE);
            holder.imgInteres.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(favorito);
                    pendingRunnables.remove(favorito);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    listadoFavoritosRemoval.remove(favorito);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(listadoFavoritos.indexOf(favorito));
                }
            });
        } else {
            // we need to show the "normal" state
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.txtTitulo.setVisibility(View.VISIBLE);
            holder.txtResumen.setVisibility(View.VISIBLE);
            holder.imgInteres.setVisibility(View.VISIBLE);
            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);
        }
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final Favorito item = listadoFavoritos.get(position);
        if (!listadoFavoritosRemoval.contains(item)) {
            listadoFavoritosRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(listadoFavoritos.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Favorito item = listadoFavoritos.get(position);
        if (listadoFavoritosRemoval.contains(item)) {
            listadoFavoritosRemoval.remove(item);
        }
        if (listadoFavoritos.contains(item)) {
            listadoFavoritos.remove(item);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        Favorito item = listadoFavoritos.get(position);
        return listadoFavoritosRemoval.contains(item);
    }

    @Override
    public int getItemCount() {
        return listadoFavoritos.size();
    }

    public static class ListadoFavoritosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtResumen;
        private final ImageView imgInteres;
        private Button undoButton;


        public ListadoFavoritosViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloInteres);
            txtResumen = (TextView) itemView.findViewById(R.id.cvResumenInteres);
            imgInteres = (ImageView) itemView.findViewById(R.id.cvImagenInteres);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);


        }
    }
}
