package org.proyecto.nerdynews.mensajes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.models.Amigo;
import org.proyecto.nerdynews.models.HistorialMensaje;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ana on 03/12/2017.
 */


public class ListadoMensajesSwipeRecyclerAdapter extends RecyclerView.Adapter<ListadoMensajesSwipeRecyclerAdapter.ListadoMensajesViewHolder> {

    private List<HistorialMensaje> listadoMensajes = null;
    private List<HistorialMensaje> listadoMensajesRemoval = null;
    private Context mContext;
    boolean undoOn;
    private Amigo[] amigos;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<HistorialMensaje, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public ListadoMensajesSwipeRecyclerAdapter(List<HistorialMensaje> listadoMensajes, Amigo[] amigos, Context ctx) {
        this.listadoMensajes = listadoMensajes;
        this.mContext = ctx;
        this.amigos = amigos;
        listadoMensajesRemoval = new ArrayList<>();
    }

    @Override
    public ListadoMensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_mensaje, parent, false);
        return new ListadoMensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoMensajesViewHolder holder, int position) {
        final HistorialMensaje mensaje = listadoMensajes.get(position);
        //amigos = new GsonBuilder().create().fromJson(loadJSONFromAsset("fakeAmigos.json",this), Amigo[].class);

      //  holder.txtTitulo.setText(mensaje.getHistorial().getTitulo());
       // holder.txtTitulo.setText(mensaje.getAmigoId().getNombre());
        for (final Amigo amigo : amigos) {
            if (amigo.getId() == mensaje.getAmigoId()) {
                holder.txtTitulo.setText(amigo.getNombre());
                holder.txtResumen.setText(mensaje.getHistorial().get(mensaje.getHistorial().size() - 1).getMensaje());
                Picasso.with(mContext)
                        .load(amigo.getFoto())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.mipmap.ic_launcher_round)
                        .into(holder.imgAmigo);
                holder.imgAmigo.setTag(amigo.getFoto());
                holder.txtFecha.setText(mensaje.getHistorial().get(mensaje.getHistorial().size()-1).getFecha());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, LeerMensajeActivity.class);
                        intent.putExtra("idChat", mensaje.getId());
                        intent.putExtra("amigoid",mensaje.getAmigoId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
            }
        }

        if (listadoMensajesRemoval.contains(mensaje)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.GREEN);
            holder.txtTitulo.setVisibility(View.GONE);
            holder.txtResumen.setVisibility(View.GONE);
            holder.imgAmigo.setVisibility(View.GONE);
            holder.txtFecha.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(mensaje);
                    pendingRunnables.remove(mensaje);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    listadoMensajesRemoval.remove(mensaje);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(listadoMensajes.indexOf(mensaje));
                }
            });
        } else {
            // we need to show the "normal" state
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.txtTitulo.setVisibility(View.VISIBLE);
            holder.txtResumen.setVisibility(View.VISIBLE);
            holder.imgAmigo.setVisibility(View.VISIBLE);
            holder.txtFecha.setVisibility(View.VISIBLE);
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
        final HistorialMensaje item = listadoMensajes.get(position);
        if (!listadoMensajesRemoval.contains(item)) {
            listadoMensajesRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(listadoMensajes.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        HistorialMensaje item = listadoMensajes.get(position);
        if (listadoMensajesRemoval.contains(item)) {
            listadoMensajesRemoval.remove(item);
        }
        if (listadoMensajes.contains(item)) {
            listadoMensajes.remove(item);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        HistorialMensaje item = listadoMensajes.get(position);
        return listadoMensajesRemoval.contains(item);
    }

    @Override
    public int getItemCount() {
        return listadoMensajes.size();
    }
    public static class ListadoMensajesViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtResumen, txtFecha;
        private final RoundedImageView imgAmigo;
        private Button undoButton;

        public ListadoMensajesViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloMensaje);
            txtResumen = (TextView) itemView.findViewById(R.id.cvResumenMensaje);
            imgAmigo = (RoundedImageView) itemView.findViewById(R.id.cvImagenMensaje);
            txtFecha = (TextView) itemView.findViewById(R.id.cvFechaMensaje);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);

        }
    }

}
