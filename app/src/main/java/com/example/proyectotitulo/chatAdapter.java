package com.example.proyectotitulo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class chatAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<chats> listItems;

    public chatAdapter(Context context, ArrayList<chats> listItems) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        chats item = (chats) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_publicacion, null);
        ImageView imgPublicacion = (ImageView) convertView.findViewById(R.id.imgPublicacion);
        TextView tituloPublicacion = (TextView) convertView.findViewById(R.id.tituloPublicacion);
        TextView valorPublicacion = (TextView) convertView.findViewById(R.id.valorPublicacion);

        Picasso.get().load(item.getProfileImageUrl()).into(imgPublicacion);
        tituloPublicacion.setText(item.getTituloPublicacion());

        return convertView;
    }
}
