package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private ArrayList<String> al,bloqueados;
    // private ArrayAdapter<String> arrayAdapter;
    private arrayAdaptor arrayAdapter;
    private SwipeFlingAdapterView flingContainer;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid;
    private Map<String, Object> map;
    private String userId;
    private int puedeMostrarCard,noExistenFiltros;
    private String comunaBusqueda, tallaBusqueda, estadoBusqueda, tipoPrendaBusqueda,regionBusqueda;
    private Button mFiltros;
    List<cards> rowItems;


    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        //noExistenFiltros = 0;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();
        mFiltros = (Button) findViewById(R.id.filtrosBtn);
        puedeMostrarCard=1;
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Publicaciones");
        }

        //swipecards
        if(regionBusqueda==null) {
            regionBusqueda = "";
        }
        if(comunaBusqueda==null) {
            comunaBusqueda = "";
        }
        if(tipoPrendaBusqueda==null) {
            tipoPrendaBusqueda = "";
        }
        if(estadoBusqueda==null) {
            estadoBusqueda = "";
        }
        if(tallaBusqueda==null) {
            tallaBusqueda = "";
        }
        obtenerFiltros();
        //regionBusqueda = "Valparaíso";
        //comunaBusqueda = "Quillota";
        //tipoPrendaBusqueda = "Pantalones";
        //estadoBusqueda = "Nuevo";
        //tallaBusqueda = "XS";

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        obtenerRechazados();
        obtenerPublicacionesAceptadasyRechazadas();
        al = new ArrayList<String>();
        bloqueados = new ArrayList<String>();
        //obtenerPublicaciones();
/*
        obtenerTodasLasPublicaciones();

        /*

         */
        rowItems = new ArrayList<cards>();
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
                String idClothes = obj.getClothesId();
                usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesRechazadas").child(idClothes).setValue(true); //esto significa que no le gusto y le dio a la izq
                Toast.makeText(PaginaPrincipal.this,"Rechazado!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String idClothes = obj.getClothesId();
                usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(true); //esto significa que le gusto y le dio a la der
                //aca hay que crear el chat dentro de publicaciones guardadas.
                Toast.makeText(PaginaPrincipal.this,"Aceptado!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //Toast.makeText(PaginaPrincipal.this, "No se encuentran mas coincidencias.", Toast.LENGTH_SHORT).show();
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
                cards obj = (cards) dataObject;
                String idClothes = obj.getClothesId();
                String idOwner = obj.getOwnerId();
                //Toast.makeText(PaginaPrincipal.this,"click", Toast.LENGTH_SHORT).show();
                Intent intentDetalle = new Intent(PaginaPrincipal.this, detallePublicacion.class);
                intentDetalle.putExtra("idClothes",idClothes);
                intentDetalle.putExtra("idOwner",idOwner);
                startActivity(intentDetalle);
                finish();
            }
        });

        mFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFiltros = new Intent(PaginaPrincipal.this, Filtros.class);
                intentFiltros.putExtra("comunaAnterior",comunaBusqueda);
                intentFiltros.putExtra("regionAnterior",regionBusqueda);
                intentFiltros.putExtra("tipoPrendaAnterior",tipoPrendaBusqueda);
                intentFiltros.putExtra("estadoAnterior",estadoBusqueda);
                intentFiltros.putExtra("tallaAnterior",tallaBusqueda);
                startActivity(intentFiltros);
                finish();
                return;
            }
        });
    }

    private void obtenerRechazados() {
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId) && dataSnapshot.hasChild("Bloqueados")){
                    DatabaseReference blockDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("Bloqueados");
                    blockDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists()){
                                bloqueados.add(dataSnapshot.getKey());
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

    private void obtenerFiltros() {
        currentUId = mAuth.getCurrentUser().getUid();
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId) && dataSnapshot.hasChild("filtros")){
                    noExistenFiltros=0;
                    Log.d("entro","DENTRO DEL IF. DEBERIA SER 0 "+noExistenFiltros);
                    //Toast.makeText(PaginaPrincipal.this, dataSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();
                    comunaBusqueda = dataSnapshot.child("filtros").child("comunaAnterior").getValue().toString();
                    //Toast.makeText(PaginaPrincipal.this, comunaBusqueda, Toast.LENGTH_SHORT).show();
                    regionBusqueda = dataSnapshot.child("filtros").child("regionAnterior").getValue().toString();
                    tipoPrendaBusqueda = dataSnapshot.child("filtros").child("tipoPrendaAnterior").getValue().toString();
                    estadoBusqueda = dataSnapshot.child("filtros").child("estadoAnterior").getValue().toString();
                    tallaBusqueda = dataSnapshot.child("filtros").child("tallaAnterior").getValue().toString();
                    if (comunaBusqueda != "") {
                        obtenerPublicaciones();
                    }
                }
                else if(dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId) && !dataSnapshot.hasChild("filtros")){
                        obtenerTodasLasPublicaciones();
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

    private void obtenerPublicaciones() {
        currentUId = mAuth.getCurrentUser().getUid();
            usersDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && dataSnapshot.child("comuna").getValue().toString().equals(comunaBusqueda)
                            && !dataSnapshot.getKey().equals(currentUId)) {
                        //aca si le añadimos la localizacion, habria que hacer un metodo que calculara distancia y ponerlo arriba y compararlo por el ingresado x usuario.
                        String key = dataSnapshot.getKey();
                        final String currentOwnerUid = key;
                        Log.d("entro","currentOwnerUid antes :"+currentOwnerUid);

                        clothesDb = usersDb.child(key).child("clothes");
                        clothesDb.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //aqui ya recorre los productos.
                                currentUId = mAuth.getCurrentUser().getUid();
                                clothesCurrentUid = dataSnapshot.getKey();
                                Log.d("entro","currentOwnerUid:"+currentOwnerUid);
                                if (!verSiSeEncuentraEnArrayList2(clothesCurrentUid) && !estaBloqueado(currentOwnerUid)) {
                                    String idPrenda = dataSnapshot.getKey();
                                    String tituloPublicacion = dataSnapshot.child("tituloPublicacion").getValue().toString();
                                    String fotoPublicacion;
                                    if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                        Log.d("primero", "cuarto");
                                        fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                    } else {
                                        fotoPublicacion = "default";
                                    }
                                    if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals("") && estadoBusqueda.equals("")) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    } else {
                                        if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals("")) {
                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        }
                                    }
                                }
                            }

                            private boolean verSiSeEncuentraEnArrayList2(String clothesCurrentUid) {
                                for(int i=0;i<al.size();i++){
                                    if(al.get(i).equals(clothesCurrentUid)){
                                        return true;
                                    }
                                }
                                return false;
                            }

                            private boolean estaBloqueado(String ownerId) {
                                for(int i=0;i<bloqueados.size();i++){
                                    if(bloqueados.get(i).equals(ownerId)){
                                        return true;
                                    }
                                }
                                return false;
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

    private void obtenerTodasLasPublicaciones() {

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && !dataSnapshot.getKey().equals(currentUId)) {
                    //aca si le añadimos la localizacion, habria que hacer un metodo que calculara distancia y ponerlo arriba y compararlo por el ingresado x usuario.
                    String key = dataSnapshot.getKey();
                    final String currentOwnerUid = key;
                    clothesDb = usersDb.child(key).child("clothes");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //aqui ya recorre los productos.
                            currentUId = mAuth.getCurrentUser().getUid();
                            clothesCurrentUid = dataSnapshot.getKey();
                            if (!verSiSeEncuentraEnArrayList(clothesCurrentUid)) {
                                String idPrenda = dataSnapshot.getKey();
                                String tituloPublicacion = dataSnapshot.child("tituloPublicacion").getValue().toString();
                                String fotoPublicacion;
                                if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                    Log.d("primero", "cuarto");
                                    fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                } else {
                                    fotoPublicacion = "default";
                                }
                                    cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid); //aca se puebla la card con un constructor
                                    rowItems.add(Item); //aca añade la persona a la tarjetita.
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

    private boolean verSiSeEncuentraEnArrayList(String clothesCurrentUid) { //esta funcion ve si la publicacion esta rechazada o aceptada.

        for(int i=0;i<al.size();i++){
            if(al.get(i).equals(clothesCurrentUid)){
                return true;
            }
        }
        return false;
    }

    private void obtenerPublicacionesAceptadasyRechazadas() {
        usersDb.child(currentUId).child("connections").child("publicacionesGuardadas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.
                    al.add(dataSnapshot.getKey().toString());
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
                Intent intentAccount = new Intent(PaginaPrincipal.this, VerMiCuenta.class);
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

        }

        return super.onOptionsItemSelected(item);
    }


}