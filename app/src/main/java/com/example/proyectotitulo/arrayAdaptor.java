package com.example.proyectotitulo;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import java.util.List;
//esta funcion se encarga de desplegar la lista de cartas en mainactivity.
public class arrayAdaptor extends ArrayAdapter<cards>{

    Context context;
    /*
        public arrayAdaptor(Context context, int resourceId, List<cards> items){
            super(context,resourceId,items);
        }
    */
    public arrayAdaptor(@NonNull android.content.Context context, int resourceId, @NonNull List<cards> items, Context context1) {
        super(context, resourceId, items);
        this.context = context1;
    }

    public arrayAdaptor(android.content.Context context, int item, List<cards> rowItems) {
        super(context,item,rowItems);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView precio = (TextView) convertView.findViewById(R.id.precio);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        name.setText(card_item.getName());
        precio.setText("$"+card_item.getPrecio());
        if(card_item.getProfileImageUrl()!="default"||card_item.getProfileImageUrl()!=""){
            Picasso.get().setLoggingEnabled(true);
            //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
            Picasso.get().load(card_item.getProfileImageUrl()).into(image);
        }
        else {
            Picasso.get().setLoggingEnabled(true);
            //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
            Picasso.get().load(R.mipmap.ic_launcher).into(image);
        }

        // Picasso.get().setLoggingEnabled(true);
        //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
        //Picasso.get().load(card_item.getProfileImageUrl()).into(image);
        // image.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }
}




