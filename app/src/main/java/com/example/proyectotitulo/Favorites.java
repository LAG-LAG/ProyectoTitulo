package com.example.proyectotitulo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Favoritos");
        }

    }

    public void back(View view) {
        Intent intent = new Intent(Favorites.this, PaginaPrincipal.class);
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
                Intent intentAccount = new Intent(Favorites.this, Account.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.chatBtn:
                Intent intentChat = new Intent(Favorites.this, Chat.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(Favorites.this, AddPublicaciones.class);
                startActivity(intentAdd);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentPublicaciones = new Intent(Favorites.this, PaginaPrincipal.class);
                startActivity(intentPublicaciones);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}