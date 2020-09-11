package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaginaPrincipal extends AppCompatActivity {
    private ArrayList<String> al;
   // private ArrayAdapter<String> arrayAdapter;
    private arrayAdaptor arrayAdapter;
    private SwipeFlingAdapterView flingContainer;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid;
    private Map<String, Object> map;
    private String userId;
    private int puedeMostrarCard;
    List<cards> rowItems;


    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();
        Log.d("weawea",currentUId);
        puedeMostrarCard=1;
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Publicaciones");
        }


        //swipecards
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        obtenerPublicacionesAceptadasyRechazadas();
        al = new ArrayList<String>();
        obtenerPublicaciones();
/*
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
*/

//        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.name, al );


        rowItems = new ArrayList<cards>();/*

         */
        //crea el arrayadapter y le manda los rowitems que es donde se guardaran todas las cartas y le envia el item que es donde se mostraran las cartas.
         arrayAdapter = new arrayAdaptor(this, R.layout.item, rowItems );
        //el r.id.frame es donde se mostrara el item en el mainactivity.


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String idClothes = obj.getUserId();
                usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesRechazadas").child(idClothes).setValue(true); //esto significa que no le gusto y le dio a la izq
                Toast.makeText(PaginaPrincipal.this,"left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String idClothes = obj.getUserId();
                usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(true); //esto significa que le gusto y le dio a la der
                //aca hay que crear el chat dentro de publicaciones guardadas.
                Toast.makeText(PaginaPrincipal.this,"right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here/
            /*al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;*/
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // AL HACER CLICK LLEVA A LA DESCRIPCION.
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(PaginaPrincipal.this,"click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void obtenerPublicaciones() {

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && dataSnapshot.child("comuna").getValue().toString().equals("Quillota")){
                    Log.d("primero","segundo");
                    String key = dataSnapshot.getKey();
                    Log.d("primero",key);
                    clothesDb = usersDb.child(key).child("clothes");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.d("primero","tercero");
                            currentUId = mAuth.getCurrentUser().getUid();
                            clothesCurrentUid = dataSnapshot.getKey();
                            Log.d("weawea",clothesCurrentUid);
                            //Toast.makeText(PaginaPrincipal.this, clothesCurrentUid, Toast.LENGTH_SHORT).show();
                            if(!verSiSeEncuentraEnArrayList(clothesCurrentUid)) {

                                String fotoPublicacion;
                                if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                    Log.d("primero", "cuarto");
                                    fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                } else {
                                    fotoPublicacion = "default";
                                }
                                Log.d("primero", "quinto");

                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion); //aca se puebla la card con un constructor
                                //cards Item = new cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("profileImageUrl").getValue().toString()); //aca se puebla la card con un constructor
                                rowItems.add(Item); //aca añade la persona a la tarjetita.

                                Log.d("primero", "sexto");
                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
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

    private boolean verSiSeEncuentraEnArrayList(String clothesCurrentUid) {

        for(int i=0;i<al.size();i++){
            Log.d("weawea","funcaono");
            Log.d("weawea","keykey "+al.get(i));
            if(al.get(i).equals(clothesCurrentUid)){
                Log.d("weawea","gungagigna");
                return true;
            }
        }
        return false;
    }

    private void obtenerPublicacionesAceptadasyRechazadas() {

        usersDb.child(currentUId).child("connections").child("publicacionesGuardadas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.d("weawea","cmasdasd"+dataSnapshot.getKey());
                if (dataSnapshot.exists()) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.
                    al.add(dataSnapshot.getKey().toString());
                    Log.d("weawea","guardada");
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
        usersDb.child(currentUId).child("connections").child("publicacionesRechazadas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.
                    al.add(dataSnapshot.getKey().toString());
                    Log.d("weawea","gunga ginga"+ dataSnapshot.getKey().toString());
                    Log.d("weawea","Rechazada");
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

    public void logoutUser(View view) {
        mAuth.signOut(); //desconecta
        //las lineas de abajo mandan de la ventana actual(mainactiviy) a la de chooseloginregistration que es la de antes de estar loguado.
        Intent intent = new Intent(PaginaPrincipal.this,Login.class);
        startActivity(intent);
        finish();
        return;
    }

    //Crea el menu en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //Controla los botones del menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.accountBtn:
                Intent intentAccount = new Intent(PaginaPrincipal.this, Account.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.chatBtn:
                Intent intentChat = new Intent(PaginaPrincipal.this, Chat.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(PaginaPrincipal.this, AddPublicaciones.class);
                startActivity(intentAdd);
                finish();
                break;

            case R.id.likeBtn:
                Intent intentLike = new Intent(PaginaPrincipal.this, Favorites.class);
                startActivity(intentLike);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}