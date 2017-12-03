package org.proyecto.nerdynews.mensajes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.models.Amigo;
import org.proyecto.nerdynews.models.HistorialMensaje;

/**
 * Created by eloy on 2/12/17. Modify Ana 03/12/2017
 */

public class ListadoMensajesRecyclerAdapter  extends RecyclerView.Adapter<ListadoMensajesRecyclerAdapter.ListadoMensajesViewHolder>{
    private final HistorialMensaje[] listadoMensajes;
    private final Amigo[] amigos;
    private Context mContext;

    public ListadoMensajesRecyclerAdapter(HistorialMensaje[] listadoMensajes, Amigo[] amigos, Context ctx) {
        this.listadoMensajes = listadoMensajes;
        this.amigos = amigos;
        this.mContext = ctx;
    }

    @Override
    public ListadoMensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_mensaje, parent, false);
        return new ListadoMensajesRecyclerAdapter.ListadoMensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoMensajesViewHolder holder, int position) {
        final HistorialMensaje mensaje = listadoMensajes[position];

        for (Amigo amigo : amigos) {
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

            }
       }
    }

    @Override
    public int getItemCount() {
        return listadoMensajes.length;
    }

    public static class ListadoMensajesViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtResumen, txtFecha;
        private final RoundedImageView imgAmigo;

        public ListadoMensajesViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloMensaje);
            txtResumen = (TextView) itemView.findViewById(R.id.cvResumenMensaje);
            imgAmigo = (RoundedImageView) itemView.findViewById(R.id.cvImagenMensaje);
            txtFecha = (TextView) itemView.findViewById(R.id.cvFechaMensaje);


        }
    }

}
