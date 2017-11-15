package org.proyecto.nerdynews.eventos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.proyecto.nerdynews.R;
import org.proyecto.nerdynews.models.Evento;

/**
 * Created by eloy on 7/11/17.
 */

public class ListadoEventosRecyclerAdapter extends RecyclerView.Adapter<ListadoEventosRecyclerAdapter.ListadoEventosViewHolder>{

    private final Evento[] listadoEventos;
    private Context mContext;

    public ListadoEventosRecyclerAdapter(Evento[] listadoEventos, Context ctx) {
        this.listadoEventos = listadoEventos;
        this.mContext = ctx;
    }

    @Override
    public ListadoEventosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_evento, parent, false);
        return new ListadoEventosRecyclerAdapter.ListadoEventosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListadoEventosViewHolder holder, int position) {
        final Evento evento = listadoEventos[position];

        holder.txtTitulo.setText(evento.getTitulo());
        holder.txtResumen.setText(evento.getResumen());
        holder.txtFecha.setText(evento.getFecha());
        holder.txtLugar.setText(evento.getLugar());
        holder.txtLugar.setTag(evento.getCoordGPS());
        Picasso.with(mContext)
                .load(evento.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.imgEvento);
        holder.imgEvento.setTag(evento.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return listadoEventos.length;
    }

    public static class ListadoEventosViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtResumen, txtFecha, txtLugar;
        private final ImageView imgEvento;

        public ListadoEventosViewHolder(View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.cvTituloEvento);
            txtResumen = (TextView) itemView.findViewById(R.id.cvResumenEvento);
            txtFecha = (TextView) itemView.findViewById(R.id.cvFechaEvento);
            txtLugar = (TextView) itemView.findViewById(R.id.cvLugarEvento);
            imgEvento = (ImageView) itemView.findViewById(R.id.cvImagenEvento);

        }
    }
}
