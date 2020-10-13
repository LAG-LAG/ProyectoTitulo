package com.example.proyectotitulo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class misPublicacionAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<publicacion> listItems;
    private DatabaseReference connectionsDb,usersDb;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public misPublicacionAdapter(Context context, ArrayList<publicacion> listItems) {
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

        Picasso.get().load(item.getProfileImageUrl()).into(imgPublicacion);
        tituloPublicacion.setText(item.getTituloPublicacion());
        valorPublicacion.setText(item.getValorPublicacion());

        ImageView borrarbtn= (ImageView) view.findViewById(R.id.imgBorrarMiPublicacion);
        ImageView editarbtn= (ImageView) view.findViewById(R.id.imgEditarMiPublicacion);
        borrarbtn.setVisibility(View.VISIBLE);
        editarbtn.setVisibility(View.VISIBLE);

        borrarbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Borrar");
                Log.d("TAG", "Titulo Publicacion: "+item.getTituloPublicacion());
                item.getIdClothes();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUId = user.getUid();
                usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("clothes");
                usersDb.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.exists() && dataSnapshot.getKey().equals(item.getIdClothes())){
                            dataSnapshot.getRef().removeValue();
                            Intent intent = new Intent(context,MisPublicaciones.class);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        editarbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: editar");
                Log.d("TAG", "Titulo Publicacion: "+item.getTituloPublicacion());
                item.getIdClothes();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUId = user.getUid();
                usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("clothes");
                usersDb.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.exists() && dataSnapshot.getKey().equals(item.getIdClothes())){
                            //Intent intent = new Intent(context, MisPublicaciones.class);
                            Intent intent = new Intent(context,EditarPublicacion.class);
                            intent.putExtra("idClothes",item.getIdClothes());
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
