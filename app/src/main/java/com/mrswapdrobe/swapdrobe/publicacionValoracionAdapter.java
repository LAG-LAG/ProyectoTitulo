package com.mrswapdrobe.swapdrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class publicacionValoracionAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<publicacion> listItems;
    private DatabaseReference connectionsDb,usersDb;

    public publicacionValoracionAdapter(Context context, ArrayList<publicacion> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_publicacion_puntuacion, null);
        }

        final publicacion item = (publicacion) getItem(position);

        ImageView imgPublicacion = (ImageView) view.findViewById(R.id.imgPublicacionValoracion);
        TextView nombreValoracionPublicacion = (TextView) view.findViewById(R.id.nombreValoracionPublicacion);
        TextView valoracionComentario = (TextView) view.findViewById(R.id.valoracionComentario);
        TextView valoracionPublicacion = (TextView) view.findViewById(R.id.valoracionPublicacion);

        Picasso.get().load(item.getProfileImageUrl()).into(imgPublicacion);
        nombreValoracionPublicacion.setText(item.getTituloPublicacion());
        valoracionComentario.setText(item.getComentario());
        valoracionPublicacion.setText(Float.toString(item.getValoracion()));

        return view;
    }
}
