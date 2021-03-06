package org.aficiones.noticias.nerdynews.amigos;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.Utils.GlobalData;
import org.aficiones.noticias.nerdynews.models.Amigo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MyC on 22-11-2017.
 */

public class MisAmigosAdapter extends RecyclerView.Adapter<MisAmigosAdapter.ListadoAmigosViewHolder> implements Filterable {

    private ArrayList<Amigo> listaAmigos;
    private ArrayList<Amigo> listaFiltrada;

    private ArrayList<Amigo> listaAmigosRemoval = null;
    private boolean undoOn;

    private Context mContext;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<Amigo, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    private static final int PENDING_REMOVAL_TIMEOUT = 3000;

    public MisAmigosAdapter(ArrayList<Amigo> listaAmigos, Context mContext) {
        this.listaAmigos = listaAmigos;
        this.listaFiltrada = listaAmigos;

        listaAmigosRemoval = new ArrayList<>();
        this.mContext = mContext;
    }

    @Override
    public ListadoAmigosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_amigo,parent,false);
        return new MisAmigosAdapter.ListadoAmigosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoAmigosViewHolder holder, int position) {
        Amigo amigo = listaFiltrada.get(position);

        holder.txtNombre.setText(amigo.getNombre() + " " + amigo.getApellido());
        holder.txtEdad.setText(mContext.getResources().getString((R.string.edad))+":"+String.valueOf(amigo.getEdad()));
        holder.txtIntereses.setText(mContext.getResources().getString((R.string.interes))+":"+String.valueOf(amigo.getIntereses()));
        holder.txtIdentificador.setText(String.valueOf(amigo.getId()));
        Picasso.with(mContext).load(amigo.getFoto())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher_round)
                .resize(100,100)
                .centerCrop()
                .into(holder.imgAmigo);
        holder.imgAmigo.setTag(amigo.getFoto());
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String filtro = charSequence.toString();

                if(filtro.isEmpty()){
                    listaFiltrada = listaAmigos;
                }else{
                    ArrayList<Amigo> filtrada = new ArrayList<Amigo>();
                    for(Amigo amigo: listaAmigos){
                        if(amigo.getIntereses().toLowerCase().contains(filtro.toLowerCase())){
                            filtrada.add(amigo);
                        }
                    }
                    listaFiltrada = filtrada;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaFiltrada;

                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaFiltrada = (ArrayList<Amigo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class ListadoAmigosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtNombre, txtEdad, txtIntereses, txtIdentificador;
        private final ImageView imgAmigo;

        public ListadoAmigosViewHolder(View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtEdad = (TextView) itemView.findViewById(R.id.txtEdad);
            txtIntereses = (TextView) itemView.findViewById(R.id.txtIntereses);
            txtIdentificador = (TextView) itemView.findViewById(R.id.txtIdentificador);
            imgAmigo = (ImageView) itemView.findViewById(R.id.ivImagenAmigo);

        }
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final Amigo item = listaAmigos.get(position);
        if (!listaAmigosRemoval.contains(item)) {
            listaAmigosRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(listaAmigos.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public boolean isPendingRemoval(int position) {
        Amigo item = listaAmigos.get(position);
        return listaAmigosRemoval.contains(item);
    }

    public void remove(int position) {
        Amigo item = listaAmigos.get(position);
        if (listaAmigosRemoval.contains(item)) {
            listaAmigosRemoval.remove(item);
        }
        if (listaAmigos.contains(item)) {
            listaAmigos.remove(item);
            notifyItemRemoved(position);
            //buscamos en Gd y actualizamos
            GlobalData.getInstance().setMisAmigos(listaAmigos);
        }
    }
}
