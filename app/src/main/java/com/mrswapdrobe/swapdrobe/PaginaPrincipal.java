package com.mrswapdrobe.swapdrobe;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PaginaPrincipal extends AppCompatActivity {
    private ArrayList<String> al, bloqueados;
    // private ArrayAdapter<String> arrayAdapter;
    private arrayAdaptor arrayAdapter;
    private SwipeFlingAdapterView flingContainer;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid;
    private Map<String, Object> map;
    private String userId;
    private double longitudUser, latitudeUser;
    private ChildEventListener childEventListenerClothes;
    private int puedeMostrarCard, noExistenFiltros, kmBusqueda, esBusquedaPorKm;
    private String comunaBusqueda, tallaBusqueda, estadoBusqueda, tipoPrendaBusqueda, regionBusqueda;
    private Button mFiltros;
    private ArrayList<String> IdCarta = new ArrayList<>();
    private TextView perfilEditar;
    private int PosicionCartas = 0;
    private ImageView recuperarBorrados,mLike,mDislike;
    List<cards> rowItems;


    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Publicaciones");
        }
        esBusquedaPorKm = 0;
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        //noExistenFiltros = 0;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();
        perfilEditar = (TextView) findViewById(R.id.perfilEditar);
        mFiltros = (Button) findViewById(R.id.filtrosBtn);
        puedeMostrarCard = 1;
        perfilEditar.setVisibility(View.INVISIBLE);
        recuperarBorrados = (ImageView)  findViewById(R.id.recuperarBorrados);
        mLike = (ImageView)  findViewById(R.id.likePage);
        mDislike = (ImageView)  findViewById(R.id.dislikePage);
        mLike.setVisibility(View.INVISIBLE);
        mDislike.setVisibility(View.INVISIBLE);
        //Toolbar Menu


        //swipecards
        if (regionBusqueda == null) {
            regionBusqueda = "";
        }
        if (comunaBusqueda == null) {
            comunaBusqueda = "";
        }
        if (tipoPrendaBusqueda == null) {
            tipoPrendaBusqueda = "";
        }
        if (estadoBusqueda == null) {
            estadoBusqueda = "";
        }
        if (tallaBusqueda == null) {
            tallaBusqueda = "";
        }
        verSiExiste();
        obtenerRechazados();
        obtenerPublicacionesAceptadasyRechazadas();

        obtenerFiltros();

        //regionBusqueda = "Valparaíso";
        //comunaBusqueda = "Quillota";
        //tipoPrendaBusqueda = "Pantalones";
        //estadoBusqueda = "Nuevo";
        //tallaBusqueda = "XS";

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        al = new ArrayList<String>();
        bloqueados = new ArrayList<String>();
        //obtenerPublicaciones();
/*
        obtenerTodasLasPublicaciones();

        /*

         */
        rowItems = new ArrayList<cards>();
        //crea el arrayadapter y le manda los rowitems que es donde se guardaran todas las cartas y le envia el item que es donde se mostraran las cartas.
        arrayAdapter = new arrayAdaptor(this, R.layout.item, rowItems);
        //el r.id.frame es donde se mostrara el item en el mainactivity.


        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseReference guardarFavorito = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesGuardadas").child(IdCarta.get(PosicionCartas));
                //guardarFavorito.setValue(true);
                flingContainer.getTopCardListener().selectRight();

                //rowItems.remove(0);
                //arrayAdapter.notifyDataSetChanged();
                //PosicionCartas++;
                //aca hay que crear el chat dentro de publicaciones guardadas.
                //Toast.makeText(PaginaPrincipal.this, "Aceptado!", Toast.LENGTH_SHORT).show();
                if(PosicionCartas>=IdCarta.size()) {
                    mLike.setVisibility(View.INVISIBLE);
                    mDislike.setVisibility(View.INVISIBLE);

                }
            }
        });

        mDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseReference guardarFavorito = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesRechazadas").child(IdCarta.get(PosicionCartas));
                //guardarFavorito.setValue(new Date().getTime());
                //rowItems.remove(0);
                //arrayAdapter.notifyDataSetChanged();
                flingContainer.getTopCardListener().selectLeft();
                //PosicionCartas++;
                //aca hay que crear el chat dentro de publicaciones guardadas.
                if(PosicionCartas>=IdCarta.size()) {
                    mLike.setVisibility(View.INVISIBLE);
                    mDislike.setVisibility(View.INVISIBLE);                }
            }
        });


        recuperarBorrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] listItems = getResources().getStringArray(R.array.Recuperar);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PaginaPrincipal.this);
                mBuilder.setTitle("Seleccione cantidad de tiempo que desea recuperar las publicaciones.");
                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //mResult.setText(listItems[i]);
                        Toast.makeText(PaginaPrincipal.this, ""+listItems[i], Toast.LENGTH_SHORT).show();
                        Long ahora = new Date().getTime();

                        switch (i){
                            case 0:
                                borrarUltimaHora(60);
                                break;
                            case 1:
                                borrarUltimaHora(1440);
                                break;
                            case 2:
                                borrarUltimaHora(10080);
                                break;
                            case 3:
                                borrarUltimaHora(43800);
                                break;
                            case 4:
                                borrarRechazados();
                                break;
                        }

                        dialogInterface.dismiss();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }

                    private void borrarUltimaHora(int HorasTotales) {
                        usersDb.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if(dataSnapshot.exists()  && dataSnapshot.getKey().equals(currentUId)){
                                    Log.d("entra2","2");
                                    if(dataSnapshot.hasChild("connections")){
                                        Log.d("entra2","3");
                                        if(dataSnapshot.child("connections").hasChild("publicacionesRechazadas")){
                                            Log.d("entra2","4");
                                            DatabaseReference borradosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesRechazadas");
                                            borradosDb.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                    if(dataSnapshot.exists()){
                                                        Long ahora = new Date().getTime();
                                                        Long horaBorrado = (Long) dataSnapshot.getValue();
                                                        Log.d("horas","ahora: "+ahora +" horaBorrado: "+horaBorrado);
                                                        long diffInMillies = ahora - horaBorrado;
                                                        TimeUnit diferencia = TimeUnit.MINUTES;
                                                        Long diferenciaH = diferencia.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                                        Log.d("horas","diferencia: "+diferencia.convert(diffInMillies, TimeUnit.MILLISECONDS));
                                                        Log.d("horas","MAXIMO: "+HorasTotales);

                                                        if(diferenciaH <= HorasTotales){
                                                            dataSnapshot.getRef().removeValue();

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

                    private void borrarRechazados() {
                        Log.d("entra2","1");

                        usersDb.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if(dataSnapshot.exists()  && dataSnapshot.getKey().equals(currentUId)){
                                    Log.d("entra2","2");
                                    if(dataSnapshot.hasChild("connections")){
                                        Log.d("entra2","3");
                                        if(dataSnapshot.child("connections").hasChild("publicacionesRechazadas")){
                                            Log.d("entra2","4");
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesRechazadas").removeValue();
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
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

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
                usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesRechazadas").child(idClothes).setValue(new Date().getTime()); //esto significa que no le gusto y le dio a la izq
                Toast.makeText(PaginaPrincipal.this,"Rechazado!", Toast.LENGTH_SHORT).show();
                PosicionCartas++;
                if(PosicionCartas>=IdCarta.size()) {
                    mLike.setVisibility(View.INVISIBLE);
                    mDislike.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                cards obj = (cards) dataObject;
                String idClothes = obj.getClothesId();
                //usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(true); //esto significa que le gusto y le dio a la der



                //FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(idClothes);
                DatabaseReference guardarFavorito = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesGuardadas").child(idClothes);
                guardarFavorito.setValue(true);
                PosicionCartas++;
                //aca hay que crear el chat dentro de publicaciones guardadas.
                Toast.makeText(PaginaPrincipal.this,"Aceptado!", Toast.LENGTH_SHORT).show();
                if(PosicionCartas>=IdCarta.size()) {
                    mLike.setVisibility(View.INVISIBLE);
                    mDislike.setVisibility(View.INVISIBLE);

                }
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
                if(childEventListenerClothes!=null) {
                    clothesDb.removeEventListener(childEventListenerClothes);
                }
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
                if(childEventListenerClothes!=null) {
                    clothesDb.removeEventListener(childEventListenerClothes);
                }
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

    private void verSiExiste() {
        DatabaseReference ver = FirebaseDatabase.getInstance().getReference();
        Log.d("verrr","1");

        ver.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("verrr","2 ");
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals("Users")){
                    Log.d("verrr","3 " + currentUId);

                    if(!dataSnapshot.hasChild(currentUId)){
                        Log.d("verrr","4");
                        perfilEditar.setVisibility(View.VISIBLE);
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
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId) && dataSnapshot.hasChild("filtros") ){
                    noExistenFiltros=0;
                    if(dataSnapshot.child("filtros").hasChild("KMBusqueda")){
                        kmBusqueda = Integer.valueOf(dataSnapshot.child("filtros").child("KMBusqueda").getValue().toString());
                    }
                    if(dataSnapshot.child("filtros").hasChild("tipoBusqueda")){
                        Log.d("pnect","weaa3");
                        if(dataSnapshot.child("filtros").child("tipoBusqueda").getValue().toString().equals("1") || dataSnapshot.child("filtros").child("tipoBusqueda").getValue().toString().equals("0")){
                            esBusquedaPorKm = 1;
                            if(dataSnapshot.hasChild("longitude")){
                                longitudUser = Double.valueOf(dataSnapshot.child("longitude").getValue().toString());
                                Log.d("pnect","weaa");
                            }
                            if(dataSnapshot.hasChild("latitude")){
                                latitudeUser = Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                                Log.d("pnect","wea2");

                            }
                        }
                        else{
                            esBusquedaPorKm = 0;
                        }
                    }
                    Log.d("entro","DENTRO DEL IF. DEBERIA SER 0 "+noExistenFiltros);
                    //Toast.makeText(PaginaPrincipal.this, dataSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();
                    if(dataSnapshot.child("filtros").hasChild("comunaAnterior")) {
                        comunaBusqueda = dataSnapshot.child("filtros").child("comunaAnterior").getValue().toString();
                    }
                    //Toast.makeText(PaginaPrincipal.this, comunaBusqueda, Toast.LENGTH_SHORT).show();
                    if(dataSnapshot.child("filtros").hasChild("comunaAnterior")) {
                        regionBusqueda = dataSnapshot.child("filtros").child("regionAnterior").getValue().toString();
                    }
                    tipoPrendaBusqueda = dataSnapshot.child("filtros").child("tipoPrendaAnterior").getValue().toString();
                    estadoBusqueda = dataSnapshot.child("filtros").child("estadoAnterior").getValue().toString();
                    tallaBusqueda = dataSnapshot.child("filtros").child("tallaAnterior").getValue().toString();
                    if(esBusquedaPorKm==1){
                        obtenerpublicacionesPorKm();
                    }
                    else if(esBusquedaPorKm==0){
                        obtenerPublicaciones();
                    }

                }
                else if(dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId) && !dataSnapshot.hasChild("filtros")){
                    obtenerTodasLasPublicaciones();
                }

            }

            private void obtenerpublicacionesPorKm() {

                Log.d("pnect","1");

                currentUId = mAuth.getCurrentUser().getUid();
                usersDb.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.hasChild("Bloqueados")) {
                            if (!dataSnapshot.child("Bloqueados").hasChild(currentUId) && dataSnapshot.hasChild("puntuacionGeneral")) {
                                if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && !dataSnapshot.getKey().equals(currentUId)) {
                                    Log.d("pnect","2");

                                    if(dataSnapshot.hasChild("latitudeVenta") && dataSnapshot.hasChild("longitudeVenta") || dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")){
                                        double latitude,longitude;
                                        Log.d("pnect","3");
//esto esta bien
                                        if(dataSnapshot.hasChild("latitudeVenta") && dataSnapshot.hasChild("longitudeVenta")) {
                                            latitude = Double.valueOf(dataSnapshot.child("latitudeVenta").getValue().toString());
                                            longitude = Double.valueOf(dataSnapshot.child("longitudeVenta").getValue().toString());
                                        }
                                        else if (dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")){
                                            latitude = Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                                            longitude = Double.valueOf(dataSnapshot.child("longitude").getValue().toString());
                                        }
                                        else{
                                            latitude = 0;
                                            longitude = 0;
                                        }
                                        if(estaEnRadioKM(kmBusqueda,latitude,longitude)){
                                            double puntuacionGeneral;
                                            if(dataSnapshot.hasChild("puntuacionGeneral")){
                                                if(dataSnapshot.child("puntuacionGeneral").getValue().toString().equals("-1")){
                                                    puntuacionGeneral = 3;
                                                }
                                                else {
                                                    puntuacionGeneral = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                                                }
                                            }
                                            else{
                                                puntuacionGeneral = 3;
                                            }
                                            final double finalPuntuacionGeneral = puntuacionGeneral;
                                            Log.d("pnect","4");
                                            String key = dataSnapshot.getKey();
                                            final String currentOwnerUid = key;
                                            clothesDb = usersDb.child(key).child("clothes");
                                            childEventListenerClothes = clothesDb.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //aqui ya recorre los productos.
                                                    Log.d("pnect","4");
                                                    currentUId = mAuth.getCurrentUser().getUid();
                                                    clothesCurrentUid = dataSnapshot.getKey();
                                                    int puede = 1;
                                                    if(dataSnapshot.exists() && dataSnapshot.hasChild("vendidaTemporal")){
                                                        if(dataSnapshot.child("vendidaTemporal").getValue().toString().equals("1")){
                                                            Log.d("puede","puede");
                                                            puede = 0;
                                                        }
                                                    }
                                                    if (dataSnapshot.exists() && dataSnapshot.hasChild("clothesPhotos") && !verSiSeEncuentraEnArrayList2(clothesCurrentUid) && !estaBloqueado(currentOwnerUid) && !dataSnapshot.hasChild("estaVendida") && puede==1) {

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
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        } else {
                                                            if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals("")) {
                                                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                                rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                                IdCarta.add(dataSnapshot.getKey());
                                                                ordenarPorPuntuacion();
                                                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                            } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                                rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                                IdCarta.add(dataSnapshot.getKey());
                                                                ordenarPorPuntuacion();
                                                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                            } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                                rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                                IdCarta.add(dataSnapshot.getKey());
                                                                ordenarPorPuntuacion();
                                                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                            } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                                rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                                IdCarta.add(dataSnapshot.getKey());
                                                                ordenarPorPuntuacion();
                                                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                            } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                                rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                                IdCarta.add(dataSnapshot.getKey());
                                                                ordenarPorPuntuacion();
                                                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                            } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                                rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                                IdCarta.add(dataSnapshot.getKey());
                                                                ordenarPorPuntuacion();
                                                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                            } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                                cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                                rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                                IdCarta.add(dataSnapshot.getKey());
                                                                ordenarPorPuntuacion();
                                                                arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                            }
                                                        }
                                                    }
                                                }

                                                private void ordenarPorPuntuacion() {
                                                    //for(int i =0; i < rowItems.size();i++){
                                                    Collections.sort(rowItems, new Comparator<cards>() {
                                                        public int compare(cards o1, cards o2) {

                                                            final double puntuacionGeneral =o1.getPuntuacionGeneral();
                                                            final double puntuacionGeneral2 =o2.getPuntuacionGeneral();
                                                            return Double.compare(puntuacionGeneral, puntuacionGeneral2);
                                                            //return o2.getPuntuacionGeneral() - o1.getPuntuacionGeneral();
//                                                        return DESCENDING_COMPARATOR.compare(d, d1);
                                                        }

                                                        @Override
                                                        public Comparator<cards> reversed() {
                                                            return null;
                                                        }
                                                    });
                                                    //   }
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




                                }
                            }
                        }
                        else{
                            if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && !dataSnapshot.getKey().equals(currentUId)) {
                                Log.d("pnect","2");

                                if(dataSnapshot.hasChild("latitudeVenta") && dataSnapshot.hasChild("longitudeVenta") || dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")){
                                    double latitude,longitude;
                                    Log.d("pnect","3");
//esto esta bien
                                    if(dataSnapshot.hasChild("latitudeVenta") && dataSnapshot.hasChild("longitudeVenta")) {
                                        latitude = Double.valueOf(dataSnapshot.child("latitudeVenta").getValue().toString());
                                        longitude = Double.valueOf(dataSnapshot.child("longitudeVenta").getValue().toString());
                                    }
                                    else if (dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")){
                                        latitude = Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                                        longitude = Double.valueOf(dataSnapshot.child("longitude").getValue().toString());
                                    }
                                    else{
                                        latitude = 0;
                                        longitude = 0;
                                    }
                                    if(estaEnRadioKM(kmBusqueda,latitude,longitude)){
                                        double puntuacionGeneral;
                                        if(dataSnapshot.hasChild("puntuacionGeneral")){
                                            if(dataSnapshot.child("puntuacionGeneral").getValue().toString().equals("-1")){
                                                puntuacionGeneral = 3;
                                            }
                                            else {
                                                puntuacionGeneral = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                                            }
                                        }
                                        else{
                                            puntuacionGeneral = 3;
                                        }
                                        final double finalPuntuacionGeneral = puntuacionGeneral;
                                        Log.d("pnect","4");
                                        String key = dataSnapshot.getKey();
                                        final String currentOwnerUid = key;
                                        clothesDb = usersDb.child(key).child("clothes");
                                        childEventListenerClothes = clothesDb.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //aqui ya recorre los productos.
                                                Log.d("pnect","4");
                                                currentUId = mAuth.getCurrentUser().getUid();
                                                clothesCurrentUid = dataSnapshot.getKey();
                                                int puede = 1;
                                                if(dataSnapshot.exists() && dataSnapshot.hasChild("vendidaTemporal")){
                                                    if(dataSnapshot.child("vendidaTemporal").getValue().toString().equals("1")){
                                                        Log.d("puede","puede");
                                                        puede = 0;
                                                    }
                                                }
                                                if (dataSnapshot.exists() && dataSnapshot.hasChild("clothesPhotos") && !verSiSeEncuentraEnArrayList2(clothesCurrentUid) && !estaBloqueado(currentOwnerUid) && !dataSnapshot.hasChild("estaVendida") && puede==1) {

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
                                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                        IdCarta.add(dataSnapshot.getKey());
                                                        ordenarPorPuntuacion();
                                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                    } else {
                                                        if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals("")) {
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                                            cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                                            rowItems.add(Item); //aca añade la persona a la tarjetita.
                                                            IdCarta.add(dataSnapshot.getKey());
                                                            ordenarPorPuntuacion();
                                                            arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        }
                                                    }
                                                }
                                            }

                                            private void ordenarPorPuntuacion() {
                                                //for(int i =0; i < rowItems.size();i++){
                                                Collections.sort(rowItems, new Comparator<cards>() {
                                                    public int compare(cards o1, cards o2) {

                                                        final double puntuacionGeneral =o1.getPuntuacionGeneral();
                                                        final double puntuacionGeneral2 =o2.getPuntuacionGeneral();
                                                        return Double.compare(puntuacionGeneral, puntuacionGeneral2);
                                                        //return o2.getPuntuacionGeneral() - o1.getPuntuacionGeneral();
//                                                        return DESCENDING_COMPARATOR.compare(d, d1);
                                                    }

                                                    @Override
                                                    public Comparator<cards> reversed() {
                                                        return null;
                                                    }
                                                });
                                                //   }
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




                            }
                        }


                    }

                    private boolean estaEnRadioKM(int kmBusqueda, double lat2, double lon2) {

                        Location startPoint=new Location("locationA");
                        startPoint.setLatitude(latitudeUser);
                        startPoint.setLongitude(longitudUser);

                        Location endPoint=new Location("locationA");
                        endPoint.setLatitude(lat2);
                        endPoint.setLongitude(lon2);

                        double distance = startPoint.distanceTo(endPoint);
                        kmBusqueda=kmBusqueda*1000;
                        Log.d("pnect","latitudeUser "+latitudeUser);
                        Log.d("pnect","longitudUser "+longitudUser);
                        Log.d("pnect","lon2 "+lon2);
                        Log.d("pnect","lat2 "+lat2);

                        Log.d("pnect","dist "+distance);
                        Log.d("pnect","kmBusqueda "+kmBusqueda);

                        if(distance<=kmBusqueda){
                            return true;
                        }
                        return false;
                        /*
                        Log.d("pnect","kmBusqueda "+kmBusqueda);
                        Log.d("pnect","lon2 "+lon2);
                        Log.d("pnect","lat2 "+lat2);

                        double theta = longitudUser - lon2;
                        double dist = Math.sin(deg2rad(latitudeUser))
                                * Math.sin(deg2rad(lat2))
                                + Math.cos(deg2rad(latitudeUser))
                                * Math.cos(deg2rad(lat2))
                                * Math.cos(deg2rad(theta));
                        dist = Math.acos(dist);
                        dist = rad2deg(dist);
                        dist = dist * 60 * 1.1515;
                        dist =dist / 0.62137;
                        Log.d("pnect","dist "+dist);

                        if(dist<=kmBusqueda){
                            return true;
                        }
                        return false;
                    */
                    }

                    private double deg2rad(double deg) {
                        return (deg * Math.PI / 180.0);
                    }

                    private double rad2deg(double rad) {
                        return (rad * 180.0 / Math.PI);
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


                //clothesDb.removeEventListener(childEventListenerClothes);


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
                    double puntuacionGeneral;
                    if(dataSnapshot.hasChild("puntuacionGeneral")){
                        puntuacionGeneral = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                    }
                    else{
                        puntuacionGeneral = 3;
                    }
                    final double finalPuntuacionGeneral = puntuacionGeneral;
                    Log.d("puntuacionGeneral",""+puntuacionGeneral);
                    childEventListenerClothes = clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //aqui ya recorre los productos.
                            currentUId = mAuth.getCurrentUser().getUid();
                            clothesCurrentUid = dataSnapshot.getKey();
                            Log.d("entro","currentOwnerUid:"+currentOwnerUid);
                            int puede = 1;
                            if(dataSnapshot.exists() && dataSnapshot.hasChild("vendidaTemporal")){
                                if(dataSnapshot.child("vendidaTemporal").getValue().toString().equals("1")){
                                    Log.d("puede","puede");
                                    puede = 0;
                                }
                            }
                            if (!verSiSeEncuentraEnArrayList2(clothesCurrentUid) && !estaBloqueado(currentOwnerUid) && !dataSnapshot.hasChild("estaVendida") && puede==1) {
                                mDislike.setVisibility(View.VISIBLE);
                                mLike.setVisibility(View.VISIBLE);
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
                                    cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid, finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                    rowItems.add(Item); //aca añade la persona a la tarjetita.
                                    IdCarta.add(dataSnapshot.getKey());
                                    ordenarPorPuntuacion();
                                    arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                } else {
                                    if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals("")) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        IdCarta.add(dataSnapshot.getKey());
                                        ordenarPorPuntuacion();
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        IdCarta.add(dataSnapshot.getKey());
                                        ordenarPorPuntuacion();
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        ordenarPorPuntuacion();
                                        IdCarta.add(dataSnapshot.getKey());
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        IdCarta.add(dataSnapshot.getKey());
                                        ordenarPorPuntuacion();
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals("")) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        IdCarta.add(dataSnapshot.getKey());
                                        ordenarPorPuntuacion();
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    } else if (tipoPrendaBusqueda.equals(dataSnapshot.child("TipoPrenda").getValue().toString()) && tallaBusqueda.equals("") && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        IdCarta.add(dataSnapshot.getKey());
                                        ordenarPorPuntuacion();
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                    } else if (tipoPrendaBusqueda.equals("") && tallaBusqueda.equals(dataSnapshot.child("TallaPrenda").getValue().toString()) && estadoBusqueda.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        IdCarta.add(dataSnapshot.getKey());
                                        ordenarPorPuntuacion();
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        //clothesDb.removeEventListener(childEventListenerClothes);

                                    }
                                }
                            }
                        }

                        private void ordenarPorPuntuacion() {
                            //for(int i =0; i < rowItems.size();i++){
                            Collections.sort(rowItems, new Comparator<cards>() {
                                public int compare(cards o1, cards o2) {
                                    final double puntuacionGeneral =o1.getPuntuacionGeneral();
                                    final double puntuacionGeneral2 =o2.getPuntuacionGeneral();
                                    return Double.compare(puntuacionGeneral2, puntuacionGeneral);
                                    //                                                     return DESCENDING_COMPARATOR.compare(d, d1);
                                }

                                @Override
                                public Comparator<cards> reversed() {
                                    return null;
                                }
                            });
                            //}
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
        //clothesDb.removeEventListener(childEventListenerClothes);

    }

    private void obtenerTodasLasPublicaciones() {

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && !dataSnapshot.getKey().equals(currentUId)) {
                    if(dataSnapshot.hasChild("Bloqueados")) {
                        if (!dataSnapshot.child("Bloqueados").hasChild(currentUId) && dataSnapshot.hasChild("puntuacionGeneral")) {
                            Log.d("puede","1");
                            //aca si le añadimos la localizacion, habria que hacer un metodo que calculara distancia y ponerlo arriba y compararlo por el ingresado x usuario.
                            String key = dataSnapshot.getKey();
                            final String currentOwnerUid = key;
                            clothesDb = usersDb.child(key).child("clothes");
                            //final double finalPuntuacionGeneral = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());

                            double puntuacionGeneral;
                            if(dataSnapshot.hasChild("puntuacionGeneral")) {
                                if(dataSnapshot.child("puntuacionGeneral").getValue().toString().equals("-1")){
                                    puntuacionGeneral = 3;
                                }
                                else {
                                    puntuacionGeneral = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                                }
                            }
                            else{
                                puntuacionGeneral = 3;
                            }
                            final double finalPuntuacionGeneral = puntuacionGeneral;


//                            final int puntuacion = Integer.parseInt(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                            Log.d("puntuacionGeneral","UID: "+dataSnapshot.getKey());
                            Log.d("puntuacionGeneral","UID: "+dataSnapshot.getKey()+"Puntuacion: "+finalPuntuacionGeneral);

                            childEventListenerClothes = clothesDb.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //aqui ya recorre los productos.
                                    currentUId = mAuth.getCurrentUser().getUid();
                                    clothesCurrentUid = dataSnapshot.getKey();
                                    int puede = 1;

                                    if(dataSnapshot.exists() && dataSnapshot.hasChild("vendidaTemporal")){
                                        if(dataSnapshot.child("vendidaTemporal").getValue().toString().equals("1")){
                                            Log.d("puede","puede");
                                            puede = 0;
                                        }
                                    }

                                    if (dataSnapshot.exists() && dataSnapshot.hasChild("clothesPhotos") && !verSiSeEncuentraEnArrayList(clothesCurrentUid) && !estaBloqueado(currentOwnerUid) && !dataSnapshot.hasChild("estaVendida") && puede==1) {
                                        String idPrenda = dataSnapshot.getKey();
                                        mDislike.setVisibility(View.VISIBLE);
                                        mLike.setVisibility(View.VISIBLE);
                                        String tituloPublicacion = dataSnapshot.child("tituloPublicacion").getValue().toString();
                                        String fotoPublicacion;
                                        if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                            Log.d("primero", "cuarto");
                                            fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                        } else {
                                            fotoPublicacion = "default";
                                        }
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid,finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        ordenarPorPuntuacion();
                                        IdCarta.add(dataSnapshot.getKey());
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        //clothesDb.removeEventListener(childEventListenerClothes);
                                    }
                                }

                                private void ordenarPorPuntuacion() {
                                    //for(int i =0; i < rowItems.size();i++){
                                    Collections.sort(rowItems, new Comparator<cards>() {
                                        public int compare(cards o1, cards o2) {
                                            final double puntuacionGeneral =o1.getPuntuacionGeneral();
                                            final double puntuacionGeneral2 =o2.getPuntuacionGeneral();
                                            return Double.compare(puntuacionGeneral2, puntuacionGeneral);//                                                        return DESCENDING_COMPARATOR.compare(d, d1);
                                        }

                                        @Override
                                        public Comparator<cards> reversed() {
                                            return null;
                                        }
                                    });
                                    //}
                                }

                                private boolean estaBloqueado(String currentOwnerUid) {
                                    for (int i = 0; i < bloqueados.size(); i++) {
                                        if (bloqueados.get(i).equals(currentOwnerUid)) {
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
                    else{
                        if(dataSnapshot.hasChild("puntuacionGeneral")) {
                            String key = dataSnapshot.getKey();
                            final String currentOwnerUid = key;
                            clothesDb = usersDb.child(key).child("clothes");
                            //final double finalPuntuacionGeneral = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());

                            double puntuacionGeneral;
                            if(dataSnapshot.hasChild("puntuacionGeneral")){
                                if(dataSnapshot.child("puntuacionGeneral").getValue().toString().equals("-1")){
                                    puntuacionGeneral = 3;
                                }
                                else {
                                    puntuacionGeneral = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                                }
                            }
                            else{
                                puntuacionGeneral = 3;
                            }
                            final double finalPuntuacionGeneral = puntuacionGeneral;

//                            final int puntuacion = Integer.parseInt(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                            Log.d("puntuacionGeneral", "UID: " + dataSnapshot.getKey());
                            Log.d("puntuacionGeneral", "UID: " + dataSnapshot.getKey() + "Puntuacion: " + finalPuntuacionGeneral);

                            childEventListenerClothes = clothesDb.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //aqui ya recorre los productos.
                                    currentUId = mAuth.getCurrentUser().getUid();
                                    clothesCurrentUid = dataSnapshot.getKey();

                                    int puede = 1;

                                    if(dataSnapshot.exists() && dataSnapshot.hasChild("vendidaTemporal")){
                                        if(dataSnapshot.child("vendidaTemporal").getValue().toString().equals("1")){
                                            Log.d("puede","puede");
                                            puede = 0;
                                        }
                                    }

                                    if (dataSnapshot.exists() &&  dataSnapshot.hasChild("clothesPhotos") && !verSiSeEncuentraEnArrayList(clothesCurrentUid) && !estaBloqueado(currentOwnerUid)&& !dataSnapshot.hasChild("estaVendida") && puede==1) {
                                        String idPrenda = dataSnapshot.getKey();
                                        mDislike.setVisibility(View.VISIBLE);
                                        mLike.setVisibility(View.VISIBLE);
                                        String tituloPublicacion = dataSnapshot.child("tituloPublicacion").getValue().toString();
                                        String fotoPublicacion;
                                        if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                            Log.d("primero", "cuarto");
                                            fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                        } else {
                                            fotoPublicacion = "default";
                                        }
                                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, currentOwnerUid, finalPuntuacionGeneral,dataSnapshot.child("ValorPrenda").getValue().toString()); //aca se puebla la card con un constructor
                                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                                        ordenarPorPuntuacion();
                                        IdCarta.add(dataSnapshot.getKey());
                                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                        //clothesDb.removeEventListener(childEventListenerClothes);

                                    }
                                }

                                private void ordenarPorPuntuacion() {
                                    //for(int i =0; i < rowItems.size();i++){
                                    Collections.sort(rowItems, new Comparator<cards>() {
                                        public int compare(cards o1, cards o2) {
                                            final double puntuacionGeneral = o1.getPuntuacionGeneral();
                                            final double puntuacionGeneral2 = o2.getPuntuacionGeneral();
                                            return Double.compare(puntuacionGeneral2, puntuacionGeneral);//                                                        return DESCENDING_COMPARATOR.compare(d, d1);
                                        }

                                        @Override
                                        public Comparator<cards> reversed() {
                                            return null;
                                        }
                                    });
                                    //}
                                }


                                private boolean estaBloqueado(String currentOwnerUid) {
                                    for (int i = 0; i < bloqueados.size(); i++) {
                                        if (bloqueados.get(i).equals(currentOwnerUid)) {
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
        //clothesDb.removeEventListener(childEventListenerClothes);

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
                if (dataSnapshot.exists()  ) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.
                    //!dataSnapshot.getValue().toString().equals("Guardado")
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

    //Crea el menu en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public void onBackPressed()
    {

    }
    //Controla los botones del menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.accountBtn:
                Intent intentAccount = new Intent(PaginaPrincipal.this, VerMiCuenta.class);
                if(childEventListenerClothes!=null){
                    clothesDb.removeEventListener(childEventListenerClothes);

                }
                startActivity(intentAccount);
                finish();
                break;

            case R.id.chatBtn:
                Intent intentChat = new Intent(PaginaPrincipal.this, misChats.class);
                startActivity(intentChat);
                if(childEventListenerClothes!=null){
                    clothesDb.removeEventListener(childEventListenerClothes);

                }
                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(PaginaPrincipal.this, AddPublicaciones.class);
                startActivity(intentAdd);
                if(childEventListenerClothes!=null){
                    clothesDb.removeEventListener(childEventListenerClothes);

                }
                finish();
                break;

            case R.id.cerrarSesionBtn:
                if(childEventListenerClothes!=null){
                    clothesDb.removeEventListener(childEventListenerClothes);

                }
                mAuth.signOut(); //desconecta
                //las lineas de abajo mandan de la ventana actual(mainactiviy) a la de chooseloginregistration que es la de antes de estar loguado.
                Intent intent = new Intent(PaginaPrincipal.this,Login.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}