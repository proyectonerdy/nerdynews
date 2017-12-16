package org.aficiones.noticias.nerdynews.amigos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.models.Favorito;


/**
 * Created by Ana 11/11/2017.
 */

public class ListadoFavoritosAmigosRecyclerAdapter extends RecyclerView.Adapter<ListadoFavoritosAmigosRecyclerAdapter.ListadoFavoritosViewHolder>{

    private final Favorito[] listadoFavoritos;
    private Context mContext;

    public ListadoFavoritosAmigosRecyclerAdapter(Favorito[] listadoIntereses, Context ctx) {
        this.listadoFavoritos = listadoIntereses;
        this.mContext = ctx;
    }

    @Override
    public ListadoFavoritosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_interes_amigo, parent, false);
        return new ListadoFavoritosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoFavoritosViewHolder holder, int position) {
        final Favorito favorito = listadoFavoritos[position];

        holder.txtTitulo.setText(favorito.getTitulo());

        String nombrei=favorito.getImageUrl();; //nombre fichero sin extensión
        int res_imageni = this.mContext.getResources().getIdentifier(nombrei, "drawable",this.mContext.getPackageName());
        holder.imgInteres.setImageResource(res_imageni);

        String nombre=favorito.getImageFavorito();; //nombre fichero sin extensión
        String recurso="drawable";


    }

    @Override
    public int getItemCount() {
        return listadoFavoritos.length;
    }

    public static class ListadoFavoritosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo;
        private final ImageView imgInteres;


        public ListadoFavoritosViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloInteres);
            imgInteres = (ImageView) itemView.findViewById(R.id.cvImagenInteres);


        }
    }
}
