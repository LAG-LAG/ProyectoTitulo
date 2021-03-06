
package com.mrswapdrobe.swapdrobe;

/* Esta clase corresponde a la vista item_chat_lista. se encarga de obtener los items de la lista de chats (provenientes de la clase Chat) y los añade en un recycler view con sus correspondientes atributos.

 */

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class chatAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Chat> listItems;
    private DatabaseReference chatDb;

    public chatAdapter(Context context, ArrayList<Chat> listItems) {
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
            view = inflater.inflate(R.layout.item_chat_lista, null);
        }

        final Chat item = (Chat) getItem(position);
        //convertView = LayoutInflater.from(context).inflate(R.layout.item_publicacion, null);
        ImageView imgPublicacion = (ImageView) view.findViewById(R.id.imgPublicacionChat);
        TextView tituloPublicacion = (TextView) view.findViewById(R.id.tituloPublicacionChat);
        TextView valorPublicacion = (TextView) view.findViewById(R.id.valorPublicacionChat);
        TextView nombreVendedor = (TextView) view.findViewById(R.id.nombreVendedor);
        Glide.with(this.context).load(item.getProfileImageUrl()).into(imgPublicacion);

        //Picasso.get().load(item.getProfileImageUrl()).into(imgPublicacion);
        tituloPublicacion.setText(item.getTituloPublicacion());
        valorPublicacion.setText(item.getValorPublicacion());
        nombreVendedor.setText(item.getNombreVendedor());
        Log.d("positionVector","ID Publicacion Item: "+item.getIdClothes());

        ImageView editarbtn= (ImageView) view.findViewById(R.id.imgEditarMiPublicacion);
        ImageView borrarbtn= (ImageView) view.findViewById(R.id.imgBorrarMiPublicacionChat);
        //ImageView editarbtn= (ImageView) view.findViewById(R.id.imgEditarMiPublicacion);

        borrarbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Borrar");
                Log.d("TAG", "Titulo Publicacion: "+item.getTituloPublicacion());

                new AlertDialog.Builder(context)
                        //.setTitle("")
                        .setMessage("¿Esta seguro de remover el chat?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                item.getIdClothes();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                final String currentUId = user.getUid();
                                chatDb = FirebaseDatabase.getInstance().getReference().child("chat");
                                chatDb.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        if(dataSnapshot.exists() && dataSnapshot.getKey().equals(item.getIdClothes())){
                                            //dataSnapshot.getRef().removeValue();
                                            if(dataSnapshot.child("idUserVendedor").equals(currentUId)){
                                                FirebaseDatabase.getInstance().getReference().child("chat").child(item.getIdClothes()).child("idUserVendedor").setValue("");
                                            }
                                            else{
                                                FirebaseDatabase.getInstance().getReference().child("chat").child(item.getIdClothes()).child("idUserComprador").setValue("");
                                            }
                                            Intent intent = new Intent(context, misChats.class);
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
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });



        return view;
    }



}
