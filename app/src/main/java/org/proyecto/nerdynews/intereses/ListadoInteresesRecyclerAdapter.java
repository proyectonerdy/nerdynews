package org.proyecto.nerdynews.intereses;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.models.Interes;




/**
 * Created by Ana 11/11/2017.
 */

public class ListadoInteresesRecyclerAdapter extends RecyclerView.Adapter<ListadoInteresesRecyclerAdapter.ListadoInteresesViewHolder>{

    private final Interes[] listadoIntereses;
    private Context mContext;

    public ListadoInteresesRecyclerAdapter(Interes[] listadoIntereses, Context ctx) {
        this.listadoIntereses = listadoIntereses;
        this.mContext = ctx;
    }

    @Override
    public ListadoInteresesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_interes, parent, false);
        return new ListadoInteresesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoInteresesViewHolder holder, int position) {
        final Interes interes = listadoIntereses[position];

        holder.txtTitulo.setText(interes.getTitulo());
        holder.txtResumen.setText(interes.getResumen());
      /*  Picasso.with(mContext)
                .load(interes.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.imgInteres);*/
        String nombrei=interes.getImageUrl();; //nombre fichero sin extensión
        int res_imageni = this.mContext.getResources().getIdentifier(nombrei, "drawable",this.mContext.getPackageName());
        holder.imgInteres.setImageResource(res_imageni);
      //  if (interes.getImageFavorito() == "ic_favorite_black_24dp")
     /*    if(interes.getImageFavorito().equals("ic_favorite_black_24dp"))
        {
            Picasso.with(mContext)
                    .load(R.drawable.ic_favorite_black_24dp)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.imgFavorito);
        }
        else
        {
            Picasso.with(mContext)
                    .load(R.drawable.ic_favorite_border_black_24dp)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.imgFavorito);
        }*/
    /*    String uri = interes.getImageFavorito();
        int imageResource =  this.mContext.getResources().getIdentifier(uri, null, this.mContext.getPackageName());
        Drawable imagen = ContextCompat.getDrawable(this.mContext.getApplicationContext(), imageResource);
        holder.imgInteres.setImageDrawable(imagen);*/
        String nombre=interes.getImageFavorito();; //nombre fichero sin extensión
        String recurso="drawable";
        int res_imagen = this.mContext.getResources().getIdentifier(nombre, recurso,this.mContext.getPackageName());
        holder.imgFavorito.setImageResource(res_imagen);

    }

    @Override
    public int getItemCount() {
        return listadoIntereses.length;
    }

    public static class ListadoInteresesViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtResumen;
        private final ImageView imgInteres, imgFavorito;


        public ListadoInteresesViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloInteres);
            txtResumen = (TextView) itemView.findViewById(R.id.cvResumenInteres);
            imgInteres = (ImageView) itemView.findViewById(R.id.cvImagenInteres);
            imgFavorito = (ImageView) itemView.findViewById(R.id.cvimageFavorito);


        }
    }
}
