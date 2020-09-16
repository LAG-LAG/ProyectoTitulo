package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MisPublicaciones extends AppCompatActivity {
    private ListView lvItems;
    private publicacionAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid, currentOwnerUid;
    private ArrayList<publicacion> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis publicaciones");
        }

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();

        obtenerPublicaciones();

        lvItems = (ListView) findViewById(R.id.lvMisPublicaciones);
        adapter = new publicacionAdapter(this, listItems);
        lvItems.setAdapter(adapter);



        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publicacion AuxPublicacion = (publicacion)lvItems.getAdapter().getItem(position);
                String idClothes = AuxPublicacion.getIdClothes();
                Intent intentDetalle = new Intent(MisPublicaciones.this, MiPublicacionDetalle.class);
                intentDetalle.putExtra("idClothes",idClothes);
                intentDetalle.putExtra("idUser",mAuth.getCurrentUser().getUid());
                startActivity(intentDetalle);
                finish();
            }
        });


    }

    private void obtenerPublicaciones(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid()) ){
                    String key = dataSnapshot.getKey();
                    currentOwnerUid = key;
                    Log.d("MisPublicaciones",key);
                    clothesDb = usersDb.child(key).child("clothes");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.d("primero","tercero");
                            currentUId = mAuth.getCurrentUser().getUid();
                            clothesCurrentUid = dataSnapshot.getKey();
                            Log.d("MisPublicaciones",clothesCurrentUid);

                            String fotoPublicacion;
                            if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                Log.d("primero", "cuarto");
                                fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                            } else {
                                fotoPublicacion = "default";
                            }

                            publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion,clothesCurrentUid);
                            listItems.add(item);
                            adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
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



    /*private ArrayList<publicacion> GetArrayItems(){
        ArrayList<publicacion> listItems = new ArrayList<>();
        listItems.add(new publicacion("wea"));
        listItems.add(new publicacion("wea"));
        listItems.add(new publicacion("wea"));

        return listItems;
    }*/

    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), VerMiCuenta.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}