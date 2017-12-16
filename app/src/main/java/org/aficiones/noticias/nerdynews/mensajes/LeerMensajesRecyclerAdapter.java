package org.aficiones.noticias.nerdynews.mensajes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.aficiones.noticias.nerdynews.R;
import org.aficiones.noticias.nerdynews.models.Amigo;
import org.aficiones.noticias.nerdynews.models.Mensaje;

import java.util.ArrayList;

/**
 * Created by eloy on 2/12/17.
 */

public class LeerMensajesRecyclerAdapter extends RecyclerView.Adapter<LeerMensajesRecyclerAdapter.ListadoMensajesViewHolder>{
    private final ArrayList<Mensaje> listadoMensajes;
    private final Amigo[] amigos;
    private Context mContext;

    public LeerMensajesRecyclerAdapter(ArrayList<Mensaje> listadoMensajes, Amigo[] amigos, Context ctx) {
        this.listadoMensajes = listadoMensajes;
        this.amigos = amigos;
        this.mContext = ctx;
    }

    @Override
    public ListadoMensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_leer_mensaje, parent, false);
        return new ListadoMensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoMensajesViewHolder holder, int position) {
        final Mensaje mensaje = listadoMensajes.get(position);

        if (mensaje.getAutorId() != 0) {
            for (Amigo amigo : amigos) {
                if (amigo.getId() == mensaje.getAutorId()) {
                    holder.txtTitulo.setText(amigo.getNombre());
                    holder.txtResumen.setText(mensaje.getMensaje());
                    holder.txtFecha.setText((mensaje.getFecha()));
                    Picasso.with(mContext)
                            .load(amigo.getFoto())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.mipmap.ic_launcher_round)
                            .into(holder.imgAmigo);
                    holder.imgAmigo.setTag(amigo.getFoto());
                }
            }
        }else {
            holder.txtTitulo.setText("Yo");
            holder.txtResumen.setText(mensaje.getMensaje());
            holder.txtFecha.setText((mensaje.getFecha()));
            Picasso.with(mContext)
                    .load(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.imgAmigo);
            holder.imgAmigo.setTag("");
        }
    }

    @Override
    public int getItemCount() {
        return listadoMensajes.size();
    }

    public static class ListadoMensajesViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtResumen, txtFecha;
        private final RoundedImageView imgAmigo;

        public ListadoMensajesViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloMensaje);
            txtResumen = (TextView) itemView.findViewById(R.id.cvResumenMensaje);
            txtFecha = (TextView) itemView.findViewById(R.id.cvFechaMensaje);
            imgAmigo = (RoundedImageView) itemView.findViewById(R.id.cvImagenMensaje);

        }
    }
}
