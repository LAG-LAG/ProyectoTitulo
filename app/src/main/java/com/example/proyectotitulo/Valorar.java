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

public class Valorar extends AppCompatActivity {
    private Button mEnviar;
    private TextView mTitulo;
    private ImageView mImage;
    private RatingBar mRBestado, mRBtrato, mRBpuntualidad;
    private String chatId,currentUserID;
    private DatabaseReference chatsDb,usersDb,clothesDb,chatsDbdos,chatsDbtres,usersdbDos;
    private ChildEventListener childEvent1,childEvent2,childEvent3,childEvent4;
    private float puntuacionGeneral,cantidadDePrendas=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorar);
        chatsDb = FirebaseDatabase.getInstance().getReference().child("chat");
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
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
        mImage = (ImageView) findViewById(R.id.imageViewValorar);
        mRBestado = (RatingBar) findViewById(R.id.ratingBar);
        mRBtrato = (RatingBar) findViewById(R.id.ratingBar2);
        mRBpuntualidad = (RatingBar) findViewById(R.id.ratingBar3);



        mEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValoracionGeneral();
               SaveValoracion();
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
                                            float puntualidad = 0, estado = 0, trato = 0,puntuacionAnterior = 0,prendasVendidas=0;
                                            puntuacionAnterior = Float.valueOf(dataSnapshot.child("puntuacionGeneral").getValue().toString());
                                            if(dataSnapshot.child("clothes").child(idPrenda).hasChild("valoracionPuntualidad")){
                                                puntualidad = Float.valueOf(dataSnapshot.child("clothes").child(idPrenda).child("valoracionPuntualidad").getValue().toString());
                                            }
                                            if(dataSnapshot.child("clothes").child(idPrenda).hasChild("valoracionEstado")) {
                                                estado = Float.valueOf(dataSnapshot.child("clothes").child(idPrenda).child("valoracionEstado").getValue().toString());
                                            }
                                            if(dataSnapshot.child("clothes").child(idPrenda).hasChild("valoracionTrato")) {
                                                trato = Float.valueOf(dataSnapshot.child("clothes").child(idPrenda).child("valoracionTrato").getValue().toString());
                                            }
                                            if(dataSnapshot.hasChild("numeroPrendasVendidas")) {
                                                prendasVendidas = Float.valueOf(dataSnapshot.child("numeroPrendasVendidas").getValue().toString());
                                            }
                                            puntuacionGeneral = Math.round((((trato + puntualidad + estado)/3) + puntuacionAnterior * prendasVendidas) / (prendasVendidas+1) * 10) / 10;

                                            //puntuacionGeneral = (((trato + puntualidad + estado)/3) + puntuacionAnterior * prendasVendidas)/(prendasVendidas+1);

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("numeroPrendasVendidas").setValue(prendasVendidas+1);

                                            //puntuacionGeneral = (((puntualidad + trato + estado)/3) + puntuacionAnterior)/2;
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionGeneral").setValue(puntuacionGeneral);
                                        }
                                    }
                                }
                                else{
                                    Log.d("GeneralV","9");

                                    float puntualidad, estado, trato;
                                    puntualidad = mRBpuntualidad.getRating();
                                    estado = mRBestado.getRating();
                                    trato = mRBtrato.getRating();
                                    puntuacionGeneral = (puntualidad + trato + estado)/3;
                                    puntuacionGeneral = Math.round((((trato + puntualidad + estado)/3)) * 10) / 10;

                                    FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("numeroPrendasVendidas").setValue("1");
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("puntuacionGeneral").setValue(puntuacionGeneral);
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