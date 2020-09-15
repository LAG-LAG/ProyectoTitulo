package com.example.proyectotitulo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MisPublicaciones extends AppCompatActivity {
    private ListView lvItems;
    private publicacionAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid, currentOwnerUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis publicaciones");
        }

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();

        lvItems = (ListView) findViewById(R.id.lvMisPublicaciones);
        adapter = new publicacionAdapter(this, GetArrayItems());
        lvItems.setAdapter(adapter);

    }

    private ArrayList<publicacion> GetArrayItems(){
        ArrayList<publicacion> listItems = new ArrayList<>();
        listItems.add(new publicacion("wea"));
        listItems.add(new publicacion("wea"));
        listItems.add(new publicacion("wea"));

        return listItems;
    }

    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), VerMiCuenta.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}