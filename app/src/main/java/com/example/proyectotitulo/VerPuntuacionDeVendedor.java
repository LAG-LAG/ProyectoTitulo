package com.example.proyectotitulo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class VerPuntuacionDeVendedor extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private publicacionValoracionAdapter adapter;
    private TextView mTextViewTrato;
    private TextView mTextViewPuntualidad;
    private TextView mTextViewEstado;
    private String idOwner,idClothes;
    private ListView lvItems;
    private ArrayList<publicacion> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_puntuacion_de_vendedor);
        idOwner = getIntent().getExtras().getString("idOwner");
        idClothes = getIntent().getExtras().getString("idClothes");

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Puntuacion de vendedor");
        }

        mTextViewTrato = (TextView) findViewById(R.id.textViewTratoVendedor);
        mTextViewPuntualidad = (TextView) findViewById(R.id.textViewPuntualidadVendedor);
        mTextViewEstado = (TextView) findViewById(R.id.TextViewEstadoVendedor);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        obtenerPublicaciones();

        lvItems = (ListView) findViewById(R.id.lvPublicacionesVendidasVendedorPuntuacion);
        adapter = new publicacionValoracionAdapter(this, listItems);
        lvItems.setAdapter(adapter);

    }

    private void obtenerPublicaciones(){
        Log.d("PUNTUACION", "0");
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && dataSnapshot.getKey().equals(idOwner)){
                    final String key = dataSnapshot.getKey();
                    final String currentOwnerUid = key;
                    Log.d("PUNTUACION", "1");

                    if(dataSnapshot.hasChild("puntuacionTrato") ){
                        if(dataSnapshot.child("puntuacionTrato") != null){
                            Log.d("PUNTUACION", "2");
                            mTextViewTrato.setText(dataSnapshot.child("puntuacionTrato").getValue().toString());
                        }
                        if(dataSnapshot.child("puntuacionEstado") != null){
                            mTextViewEstado.setText(dataSnapshot.child("puntuacionEstado").getValue().toString());
                        }
                        if(dataSnapshot.child("puntuacionPuntualidad") != null){
                            mTextViewPuntualidad.setText(dataSnapshot.child("puntuacionPuntualidad").getValue().toString());
                        }
                    }
                    else{
                        mTextViewTrato.setText("S/V");
                        mTextViewEstado.setText("S/V");
                        mTextViewPuntualidad.setText("S/V");
                    }

                    clothesDb = usersDb.child(key).child("clothes");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists()) {
                                final String clothesCurrentUid = dataSnapshot.getKey();
                                final String fotoPublicacion;
                                if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                    fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                } else {
                                    fotoPublicacion = "default";
                                }

                                if (dataSnapshot.hasChild("ValorPrenda") && dataSnapshot.hasChild("tituloPublicacion") && dataSnapshot.hasChild("DescripcionPrenda") && dataSnapshot.hasChild("clothesPhotos") && dataSnapshot.hasChild("estaVendida")){
                                    float valoracionGeneral = 0;
                                    if(dataSnapshot.hasChild("valoracionEstado") && dataSnapshot.hasChild("valoracionPuntualidad") && dataSnapshot.hasChild("valoracionTrato")){
                                        float valoracionEstado = Float.parseFloat(dataSnapshot.child("valoracionEstado").getValue().toString());
                                        float valoracionPuntualidad = Float.parseFloat(dataSnapshot.child("valoracionPuntualidad").getValue().toString());
                                        float valoracionTrato = Float.parseFloat(dataSnapshot.child("valoracionTrato").getValue().toString());
                                        valoracionGeneral = (valoracionEstado + valoracionPuntualidad + valoracionTrato) / 3;
                                    }
                                    if(dataSnapshot.hasChild("comment")){
                                        final publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, "$" + dataSnapshot.child("ValorPrenda").getValue().toString(), clothesCurrentUid, dataSnapshot.child("comment").getValue().toString(), valoracionGeneral);
                                        listItems.add(item);
                                        adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    }
                                    else{
                                        final publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, "$" + dataSnapshot.child("ValorPrenda").getValue().toString(), clothesCurrentUid, "Sin comentario", valoracionGeneral);
                                        listItems.add(item);
                                        adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    }

                                }


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

    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

}