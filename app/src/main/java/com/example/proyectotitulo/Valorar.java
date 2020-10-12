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
    private DatabaseReference chatsDb,usersDb,clothesDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorar);
        chatsDb = FirebaseDatabase.getInstance().getReference().child("chat");
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

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
               SaveValoracion();
            }
        });
    }

    private void SaveValoracion(){
        chatsDb.addChildEventListener(new ChildEventListener() {
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