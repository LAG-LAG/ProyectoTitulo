package com.mrswapdrobe.swapdrobe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class publicacionAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<publicacion> listItems;
    private DatabaseReference connectionsDb,usersDb;

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

        //Picasso.get().load(item.getProfileImageUrl()).fit().centerCrop().into(imgPublicacion);
        Glide.with(view.getContext()).load(item.getProfileImageUrl()).centerCrop().into(imgPublicacion);

        tituloPublicacion.setText(item.getTituloPublicacion());
        valorPublicacion.setText(item.getValorPublicacion());

        ImageView borrarbtn= (ImageView) view.findViewById(R.id.imgBorrarMiPublicacion);
        ImageView editarbtn= (ImageView) view.findViewById(R.id.imgEditarMiPublicacion);
        borrarbtn.setVisibility(View.VISIBLE);
        editarbtn.setVisibility(View.INVISIBLE);

        borrarbtn.setOnClickListener(new View.OnClickListener(){ //esta funcion esta mal.
            @Override
            public void onClick(View v) {
                Log.d("TAGBorrar", "onClick: Borrar");
                Log.d("TAGBorrar", "Titulo Publicacion: "+item.getTituloPublicacion());

                new AlertDialog.Builder(context)
                        //.setTitle("")
                        .setMessage("¿Esta seguro de remover la publicación?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                final String itemBorrar = item.getIdClothes();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                final String currentUId = user.getUid();
                                usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesGuardadas");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesGuardadas").child(item.getIdClothes()).removeValue();
                                Intent intent = new Intent(context,MisFavoritos.class);
                                context.startActivity(intent);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


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
