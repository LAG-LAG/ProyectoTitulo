package com.example.proyectotitulo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AddPublicaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("AddPublicacion");
        }

    }

    public void back(View view) {
        Intent intent = new Intent(AddPublicaciones.this, PaginaPrincipal.class);
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
                Toast.makeText(getApplicationContext(), "Account", Toast.LENGTH_SHORT).show();
                break;

            case R.id.chatBtn:
                Toast.makeText(getApplicationContext(), "Chat", Toast.LENGTH_SHORT).show();
                break;

            case R.id.addBtn:
                Toast.makeText(getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
                break;

            case R.id.likeBtn:
                Toast.makeText(getApplicationContext(), "Like", Toast.LENGTH_SHORT).show();
                break;

            case R.id.publicacionesBtn:
                Toast.makeText(getApplicationContext(), "Publicaciones", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}