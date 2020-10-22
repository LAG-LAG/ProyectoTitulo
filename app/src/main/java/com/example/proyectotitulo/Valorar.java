package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Valorar extends AppCompatActivity {
    private Button mEnviar;
    private TextView mTitulo;
    private TextView mComentario;
    private ImageView mImage;
    private RatingBar mRBestado, mRBtrato, mRBpuntualidad;
    private String chatId,currentUserID;
    private DatabaseReference chatsDb,usersDb,clothesDb,chatsDbdos,chatsDbtres,usersdbDos,chatsDbCuatro,usersDbTres;
    private ChildEventListener childEvent1,childEvent2,childEvent3,childEvent4,childEvent5,childEvent6;
    private double puntuacionGeneral,cantidadDePrendas=0, puntuacionGeneralTrato, puntuacionGeneralPuntualidad, puntuacionGeneralEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorar);
        chatsDb = FirebaseDatabase.getInstance().getReference().child("chat");
        chatsDbCuatro = FirebaseDatabase.getInstance().getReference().child("chat");

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        usersDbTres = usersDb;
        chatsDbdos = FirebaseDatabase.getInstance().getReference().child("chat");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Valorar");
        }
        chatId = getIntent().getExtras().getString("chatId");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mEnviar = (Button) findViewById(R.id.BtnEnviarValorar);
        mTitulo = (TextView) findViewById(R.id.TVtituloPublicacionValorar);
        mComentario = (TextView) findViewById(R.id.comentarioText);

        mImage = (ImageView) findViewById(R.id.imageViewValorar);
        mRBestado = (RatingBar) findViewById(R.id.ratingBar);
        mRBtrato = (RatingBar) findViewById(R.id.ratingBar2);
        mRBpuntualidad = (RatingBar) findViewById(R.id.ratingBar3);

        getInfoPrenda();
        mEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValoracionGeneral();
               SaveValoracion();
            }
        });
    }

    private void getInfoPrenda() {
        childEvent5 = chatsDbCuatro.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)){
                    final String idPrenda = dataSnapshot.child("idPrenda").getValue().toString();
                    final String idUserVendedor = dataSnapshot.child("idUserVendedor").getValue().toString();
                    childEvent6 = usersDbTres.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idUserVendedor)){
                                if(dataSnapshot.hasChild("clothes")){
                                    if(dataSnapshot.child("clothes").hasChild(idPrenda)){
                                        mTitulo.setText(dataSnapshot.child("clothes").child(idPrenda).child("tituloPublicacion").getValue().toString());
                                        if(dataSnapshot.child("clothes").child(idPrenda).hasChild("clothesPhotos")){
                                            if(dataSnapshot.child("clothes").child(idPrenda).child("clothesPhotos").hasChild("photoId1")){
                                                Picasso.get().load(dataSnapshot.child("clothes").child(idPrenda).child("clothesPhotos").child("photoId1").getValue().toString()).into(mImage);
                                            }
                                        }
                                        chatsDbCuatro.removeEventListener(childEvent5);
                                        usersDbTres.removeEventListener(childEvent6);
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

    private void saveValoracionGeneral() {

        chatsDbtres = FirebaseDatabase.getInstance().getReference().child("chat");
        childEvent3 =chatsDbtres.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)){
                    Log.d("GeneralV","3");
                    final String idVendedor = dataSnapshot.child("idUserVendedor").getValue().toString();
                    final String idPrenda = dataSnapshot.child("idPrenda").getValue().toString();
                    usersdbDos = FirebaseDatabase.getInstance().getReference().child("Users");
                    childEvent4 = usersdbDos.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idVendedor)){
                                if(dataSnapshot.hasChild("puntuacionGeneral") && !dataSnapshot.child("puntuacionGeneral").getValue().toString().equals("-1")){
                                    if(dataSnapshot.hasChild("clothes")){
                                        if(dataSnapshot.child("clothes").hasChild(idPrenda)){
                                            double puntualidad = 0, estado = 0, trato = 0,puntuacionAnterior = 0,prendasVendidas=0, puntuacionAntEstado, puntuacionAntPuntualidad,puntuacionAntTrato;
                                            puntuacionAnterior = Double.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                                            puntuacionAntEstado = Double.valueOf(dataSnapshot.child("puntuacionEstado").getValue().toString());
                                            puntuacionAntPuntualidad = Double.valueOf(dataSnapshot.child("puntuacionPuntualidad").getValue().toString());
                                            puntuacionAntTrato = Double.valueOf(dataSnapshot.child("puntuacionTrato").getValue().toString());

                                            if(dataSnapshot.hasChild("numeroPrendasVendidas")){
                                                prendasVendidas = Double.valueOf(dataSnapshot.child("numeroPrendasVendidas").getValue().toString());

                                            }
                                            puntualidad = mRBpuntualidad.getRating();
                                            estado = mRBestado.getRating();
                                            trato = mRBtrato.getRating();
                                            Log.d("puntuacion","puntualidad: "+puntualidad+" estado: "+estado+" trato "+trato);
                                            Log.d("puntuacion Anterior ","puntualidadA: "+puntuacionAntPuntualidad+" estadoA: "+puntuacionAntEstado+" tratoA "+puntuacionAntTrato);
                                            puntuacionGeneral = (double) Math.round((((trato + puntualidad + estado)/3) + puntuacionAnterior * prendasVendidas) / (prendasVendidas+1) * 10) / 10;

                                            //puntuacionGeneral = (((trato + puntualidad + estado)/3) + puntuacionAnterior * prendasVendidas)/(prendasVendidas+1);

                                            puntuacionGeneralTrato = (double) Math.round((trato + puntuacionAntTrato * prendasVendidas) / (prendasVendidas+1) * 10) / 10;
                                            puntuacionGeneralPuntualidad = (double) Math.round((puntualidad + puntuacionAntPuntualidad * prendasVendidas) / (prendasVendidas+1) * 10) / 10;
                                            puntuacionGeneralEstado = (double) Math.round((estado + puntuacionAntEstado * prendasVendidas) / (prendasVendidas+1) * 10) / 10;

                                            double sinMath =((((trato + puntualidad + estado)/3) + puntuacionAnterior * prendasVendidas)/(prendasVendidas+1) * 10)/10;
                                            double conMath =(double) Math.round((((trato + puntualidad + estado)/3) + puntuacionAnterior * prendasVendidas) / (prendasVendidas+1) * 10) / 10;

                                            Log.d("puntuacion","puntuacion con math"+conMath);
                                            Log.d("puntuacion","puntuacion sin math"+sinMath);

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("numeroPrendasVendidas").setValue(prendasVendidas+1);
                                            //puntuacionGeneral = (((puntualidad + trato + estado)/3) + puntuacionAnterior)/2;
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionGeneral").setValue(puntuacionGeneral);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionEstado").setValue(puntuacionGeneralEstado);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionPuntualidad").setValue(puntuacionGeneralPuntualidad);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionTrato").setValue(puntuacionGeneralTrato);

                                        }
                                    }
                                }
                                else{
                                    Log.d("GeneralV","9");

                                    float puntualidad, estado, trato;
                                    puntualidad = mRBpuntualidad.getRating();
                                    estado = mRBestado.getRating();
                                    trato = mRBtrato.getRating();
                                    //puntuacionGeneral = (puntualidad + trato + estado)/3;
                                    Log.d("puntuacion","puntualidad: "+puntualidad+" estado: "+estado+" trato "+trato);

                                    puntuacionGeneral = (double) Math.round((((trato + puntualidad + estado)/3)) * 10) / 10;
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("numeroPrendasVendidas").setValue("1");
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionGeneral").setValue(puntuacionGeneral);

                                    FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionTrato").setValue(trato);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionPuntualidad").setValue(puntualidad);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionEstado").setValue(estado);
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

    private void SaveValoracion(){
        childEvent1 = chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)){
                    final String idVendedor = dataSnapshot.child("idUserVendedor").getValue().toString();
                    final String idPrenda = dataSnapshot.child("idPrenda").getValue().toString();
                    DatabaseReference tratoValoracion =  FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("clothes").child(idPrenda).child("valoracionTrato");
                    tratoValoracion.setValue(mRBtrato.getRating());
                    DatabaseReference estadoValoracion =  FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("clothes").child(idPrenda).child("valoracionEstado");
                    estadoValoracion.setValue(mRBestado.getRating());
                    DatabaseReference puntualidadValoracion =  FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("clothes").child(idPrenda).child("valoracionPuntualidad");
                    puntualidadValoracion.setValue(mRBpuntualidad.getRating());
                    DatabaseReference estadoFinalizado = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("estadoFinalizado");
                    estadoFinalizado.setValue("1");
                    DatabaseReference estadoPublicacion =  FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("clothes").child(idPrenda).child("estaVendida");
                    estadoPublicacion.setValue("1");
                    childEvent2 = chatsDbdos.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() && dataSnapshot.child("idPrenda").getValue().toString().equals(idPrenda)){
                                Log.d("valoracion","weaaaa "+dataSnapshot.getKey());
                                Log.d("valoracion", "idprenda "+dataSnapshot.child("idPrenda").getValue().toString());
                                Log.d("valoracion", "idprenda de arriba "+idPrenda);
                                final DatabaseReference finalizarChats =  FirebaseDatabase.getInstance().getReference().child("chat").child(dataSnapshot.getKey()).child("estadoFinalizado");
                                //comentarioPublicacion =
                                FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("clothes").child(idPrenda).child("comment").setValue(mComentario.getText().toString());
                                finalizarChats.setValue("1");
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

                    Intent myIntent = new Intent(getApplicationContext(), ChatUserActivity.class);
                    myIntent.putExtra("chatId",chatId);
                    startActivityForResult(myIntent, 0);
                    finish();
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
        Log.d("rating", "estado producto " + mRBestado.getRating());
        Log.d("rating", "estado producto " + mRBtrato.getRating());
        Log.d("rating", "estado producto " + mRBpuntualidad.getRating());
    }

    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ChatUserActivity.class);
        myIntent.putExtra("chatId",chatId);
        startActivityForResult(myIntent, 0);
        return true;
    }
}