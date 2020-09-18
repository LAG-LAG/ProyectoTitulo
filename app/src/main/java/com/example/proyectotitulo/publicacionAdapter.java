package com.example.proyectotitulo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.nio.file.ClosedDirectoryStreamException;
import java.util.ArrayList;

public class publicacionAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<publicacion> listItems;

    public publicacionAdapter(Context context, ArrayList<publicacion> listItems) {
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
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_publicacion, null);
        }

        publicacion item = (publicacion) getItem(position);

        //convertView = LayoutInflater.from(context).inflate(R.layout.item_publicacion, null);
        ImageView imgPublicacion = (ImageView) view.findViewById(R.id.imgPublicacion);
        TextView tituloPublicacion = (TextView) view.findViewById(R.id.tituloPublicacion);
        TextView valorPublicacion = (TextView) view.findViewById(R.id.valorPublicacion);

        Picasso.get().load(item.getProfileImageUrl()).into(imgPublicacion);
        tituloPublicacion.setText(item.getTituloPublicacion());
        valorPublicacion.setText(item.getValorPublicacion());

        ImageView borrarbtn= (ImageView) view.findViewById(R.id.imgBorrarMiPublicacion);
        //ImageView editarbtn= (ImageView) view.findViewById(R.id.imgEditarMiPublicacion);

        borrarbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Borrar");

            }
        });
        /*editarbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: editar");
            }
        });*/

        return view;
    }
}
