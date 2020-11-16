package com.mrswapdrobe.swapdrobe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MisPublicacionesVendidas extends AppCompatActivity {

    private ListView lvItems;
    private misPublicacionVendidasAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private TextView textView1,textView2;
    //private String currentUId, clothesCurrentUid;
    private ArrayList<publicacion> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones_vendidas);

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis publicaciones vendidas");
        }

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //currentUId = user.getUid();

        obtenerPublicaciones();

        lvItems = (ListView) findViewById(R.id.lvMisPublicacionesVendidas);
        adapter = new misPublicacionVendidasAdapter(this, listItems);
        lvItems.setAdapter(adapter);

        textView1 = (TextView) findViewById(R.id.textView1Misfavoritos);
        textView2 = (TextView) findViewById(R.id.textView2Misfavoritos);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publicacion AuxPublicacion = (publicacion)lvItems.getAdapter().getItem(position);
                String idClothes = AuxPublicacion.getIdClothes();
                Intent intentDetalle = new Intent(MisPublicacionesVendidas.this, MiPublicacionDetalle.class);
                intentDetalle.putExtra("idClothes",idClothes);
                intentDetalle.putExtra("idUser",mAuth.getCurrentUser().getUid());
                startActivity(intentDetalle);
                //finish();
            }
        });

    }

    private void obtenerPublicaciones(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid()) ){
                    final String key = dataSnapshot.getKey();
                    final String currentOwnerUid = key;
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
                                if (dataSnapshot.hasChild("ValorPrenda") && dataSnapshot.hasChild("tituloPublicacion") && dataSnapshot.hasChild("DescripcionPrenda") && dataSnapshot.hasChild("clothesPhotos") && dataSnapshot.hasChild("estaVendida")) {
                                    final publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, "$" + dataSnapshot.child("ValorPrenda").getValue().toString(), clothesCurrentUid);
                                    listItems.add(item);
                                    adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                }
                                else if (dataSnapshot.hasChild("ValorPrenda") && dataSnapshot.hasChild("tituloPublicacion") && dataSnapshot.hasChild("DescripcionPrenda") && dataSnapshot.hasChild("clothesPhotos") && dataSnapshot.hasChild("vendidaTemporal")){
                                    if(dataSnapshot.child("vendidaTemporal").getValue().toString().equals("1")){
                                        final publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, "$" + dataSnapshot.child("ValorPrenda").getValue().toString(), clothesCurrentUid);
                                        listItems.add(item);
                                        adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    }
                                }
                                TieneItems();
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

    private void TieneItems()
    {
        if(listItems.isEmpty()){
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "no hay favoritos", Toast.LENGTH_SHORT).show();
        }
        else{
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }
    }

    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), VerMiCuenta.class);
        startActivity(myIntent);
        finish();
        return true;
    }

}