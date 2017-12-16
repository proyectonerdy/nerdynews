package org.aficiones.noticias.nerdynews.intereses;

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

public class ListadoFavoritosRecyclerAdapter extends RecyclerView.Adapter<ListadoFavoritosRecyclerAdapter.ListadoFavoritosViewHolder>{

    private  Favorito[] listadoFavoritos;
    private Context mContext;

    public ListadoFavoritosRecyclerAdapter(Favorito[] listadoIntereses, Context ctx) {
        this.listadoFavoritos = listadoIntereses;
        this.mContext = ctx;
    }

    @Override
    public ListadoFavoritosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_interes, parent, false);
        return new ListadoFavoritosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoFavoritosViewHolder holder, int position) {
        final Favorito favorito = listadoFavoritos[position];

        holder.txtTitulo.setText(favorito.getTitulo());
        holder.txtResumen.setText(favorito.getResumen());

        String nombrei=favorito.getImageUrl();; //nombre fichero sin extensión
        int res_imageni = this.mContext.getResources().getIdentifier(nombrei, "drawable",this.mContext.getPackageName());
        holder.imgInteres.setImageResource(res_imageni);

        String nombre=favorito.getImageFavorito();; //nombre fichero sin extensión
        String recurso="drawable";
        int res_imagen = this.mContext.getResources().getIdentifier(nombre, recurso,this.mContext.getPackageName());
        holder.imgFavorito.setImageResource(res_imagen);

    }

    @Override
    public int getItemCount() {
        return listadoFavoritos.length;
    }

    public static class ListadoFavoritosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtResumen;
        private final ImageView imgInteres, imgFavorito;


        public ListadoFavoritosViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloInteres);
            txtResumen = (TextView) itemView.findViewById(R.id.cvResumenInteres);
            imgInteres = (ImageView) itemView.findViewById(R.id.cvImagenInteres);
            imgFavorito = (ImageView) itemView.findViewById(R.id.cvimageFavorito);


        }
    }
}
