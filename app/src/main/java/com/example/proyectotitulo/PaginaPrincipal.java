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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class PaginaPrincipal extends AppCompatActivity {
    private ArrayList<String> al;
   // private ArrayAdapter<String> arrayAdapter;
    private arrayAdaptor arrayAdapter;
    private SwipeFlingAdapterView flingContainer;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    List<cards> rowItems;

    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        mAuth = FirebaseAuth.getInstance();

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Publicaciones");
        }

        //swipecards
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
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
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(PaginaPrincipal.this,"left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(PaginaPrincipal.this,"right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
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
                if(dataSnapshot.exists()){
                    Log.d("primero","primero");
                    //al.add(dataSnapshot.child("nameUser").getValue().toString());
                    if(dataSnapshot.exists() ){
                        //                    rowItems.add(dataSnapshot.child("name").getValue().toString()); //aca añade la persona a la tarjetita.
                        String profileImageUrl;
                        if(dataSnapshot.hasChild("profileImageUrl")){
                            profileImageUrl= dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        else{
                            profileImageUrl = "default";
                        }
                        cards Item = new cards(dataSnapshot.getKey(),dataSnapshot.child("nameUser").getValue().toString(),profileImageUrl); //aca se puebla la card con un constructor
                        //cards Item = new cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("profileImageUrl").getValue().toString()); //aca se puebla la card con un constructor
                        rowItems.add(Item); //aca añade la persona a la tarjetita.
                        arrayAdapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
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