package com.mrswapdrobe.swapdrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class misPublicacionVendidasAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<publicacion> listItems;
    private DatabaseReference connectionsDb,usersDb;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public misPublicacionVendidasAdapter(Context context, ArrayList<publicacion> listItems) {
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
            view = inflater.inflate(R.layout.item_publicacion, null);
        }

        final publicacion item = (publicacion) getItem(position);

        //convertView = LayoutInflater.from(context).inflate(R.layout.item_publicacion, null);
        ImageView imgPublicacion = (ImageView) view.findViewById(R.id.imgPublicacion);
        TextView tituloPublicacion = (TextView) view.findViewById(R.id.tituloPublicacion);
        TextView valorPublicacion = (TextView) view.findViewById(R.id.valorPublicacion);

        Glide.with(this.context).load(item.getProfileImageUrl()).into(imgPublicacion);

        //Picasso.get().load(item.getProfileImageUrl()).fit().centerCrop().into(imgPublicacion);

        tituloPublicacion.setText(item.getTituloPublicacion());
        valorPublicacion.setText(item.getValorPublicacion());

        ImageView borrarbtn= (ImageView) view.findViewById(R.id.imgBorrarMiPublicacion);
        ImageView editarbtn= (ImageView) view.findViewById(R.id.imgEditarMiPublicacion);
        editarbtn.setVisibility(View.INVISIBLE);
        borrarbtn.setVisibility(View.INVISIBLE);

        return view;
    }
}
