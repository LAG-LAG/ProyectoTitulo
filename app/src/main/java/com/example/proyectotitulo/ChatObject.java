package com.example.proyectotitulo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatObject {
    private String message,UserId,hora,nombre;
    private Boolean currentUser;
    private DatabaseReference usersDb;
    private ChildEventListener childEvent;
    public ChatObject(String message, Boolean currentUser){
        this.message = message;
        this.currentUser = currentUser;
    }
    public ChatObject(String message, Boolean currentUser,String UserId, String hora, String nombre){
        this.message = message;
        this.currentUser = currentUser;
        this.UserId = UserId;
        this.hora = hora;
        this.nombre = nombre;
        //getNombreUser();
    }

    private void getNombreUser() {
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        childEvent = usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(UserId)){

                    if(dataSnapshot.hasChild("nameUser")) {
                        nombre = dataSnapshot.child("nameUser").getValue().toString();
                        Log.d("nombrex", " "+nombre);
                        usersDb.removeEventListener(childEvent);

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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }
}
