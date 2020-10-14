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

public class MisFavoritos extends AppCompatActivity {

    private ListView lvItems;
    private publicacionAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid, currentOwnerUid;
    private ArrayList<String> clothesIdGuardados;
    private ArrayList<publicacion> listItems = new ArrayList<>();
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_favoritos);

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis favoritos");
        }
        i=0;
        clothesIdGuardados = new ArrayList<>();

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();

        obtenerPublicaciones();

        lvItems = (ListView) findViewById(R.id.lvMisFavoritos);
        adapter = new publicacionAdapter(this, listItems);
        lvItems.setAdapter(adapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publicacion AuxPublicacion = (publicacion)lvItems.getAdapter().getItem(position);
                String idClothes = AuxPublicacion.getIdClothes();
                Intent intentDetalle = new Intent(MisFavoritos.this, MisFavoritosDetalle.class);
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
                if(dataSnapshot.exists() && dataSnapshot.hasChild("connections") && dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())){
                    Log.d("yunglean","yunglean1");
                    final String key = dataSnapshot.getKey();
                    currentOwnerUid = key;
                    final DatabaseReference clothesDbos = usersDb.child(key).child("connections").child("publicacionesGuardadas");
                    clothesDbos.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() && dataSnapshot.getValue().toString().equals("true")) {
                                Log.d("yunglean","yunglean1");
                                currentUId = mAuth.getCurrentUser().getUid();
                                final String clothesIdGuardado = dataSnapshot.getKey();

                                final DatabaseReference usersDbdos = FirebaseDatabase.getInstance().getReference().child("Users");
                                usersDbdos.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes")) {
                                            Log.d("yunglean","yunglean3");
                                            final String keydos = dataSnapshot.getKey();
                                            currentOwnerUid = keydos;
                                            final DatabaseReference clothesDtres = usersDb.child(keydos).child("clothes");
                                            clothesDtres.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                        if (dataSnapshot.exists() && dataSnapshot.getKey().equals(clothesIdGuardado)) {
                                                            Log.d("yunglean","yunglean4");
                                                            final String fotoPublicacion;
                                                            if(dataSnapshot.hasChild("tituloPublicacion") && dataSnapshot.hasChild("clothesPhotos")& dataSnapshot.hasChild("ValorPrenda")) {
                                                                if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                                                    fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                                                } else {
                                                                    fotoPublicacion = "default";
                                                                }
                                                                final publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, clothesIdGuardado);
                                                                Log.d("yunglean","yunglean5");
                                                                listItems.add(item);
                                                                Log.d("yunglean","yunglean6");
                                                                adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
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







                                //clothesIdGuardados.add(dataSnapshot.getKey());
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


        //obtenerPublicacionesGuardadas();
        //obtenerPublicacionesGuardadasInformacion(); //el problema esta en esta funcion.

    }

    private void obtenerPublicacionesGuardadasInformacion() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes")) {
                    String key = dataSnapshot.getKey();
                    currentOwnerUid = key;
                    clothesDb = usersDb.child(key).child("clothes");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            for(i=0;i<clothesIdGuardados.size();i++) {
                                if (dataSnapshot.getKey().equals(clothesIdGuardados.get(i))) {
                                    String fotoPublicacion;
                                    if(dataSnapshot.hasChild("tituloPublicacion") && dataSnapshot.hasChild("clothesPhotos")& dataSnapshot.hasChild("ValorPrenda")) {
                                        if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                            fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                        } else {
                                            fotoPublicacion = "default";
                                        }
                                        publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, clothesIdGuardados.get(i));
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

    private void obtenerPublicacionesGuardadas() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("connections") && dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid()) && dataSnapshot.child("connections").hasChild("publicacionesGuardadas")){
                    Log.d("yunglean","yunglean2");
                    String key = dataSnapshot.getKey();
                    currentOwnerUid = key;
                    clothesDb = usersDb.child(key).child("connections").child("publicacionesGuardadas");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() ) {
                                currentUId = mAuth.getCurrentUser().getUid();
                                clothesIdGuardados.add(dataSnapshot.getKey());
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
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), VerMiCuenta.class);
        startActivity(myIntent);
        finish();
        return true;
    }

}

