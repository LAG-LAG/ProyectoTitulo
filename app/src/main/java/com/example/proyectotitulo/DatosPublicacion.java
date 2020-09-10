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

public class DatosPublicacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_publicacion);
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Datos publicacion");
        }

    }

    public void back(View view) {
        Intent intent = new Intent(DatosPublicacion.this, PaginaPrincipal.class);
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
                Intent intentAccount = new Intent(DatosPublicacion.this, Account.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(DatosPublicacion.this, AddPublicaciones.class);
                startActivity(intentAdd);
                finish();
                break;

            case R.id.likeBtn:
                Intent intentLike = new Intent(DatosPublicacion.this, Favorites.class);
                startActivity(intentLike);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentPublicaciones = new Intent(DatosPublicacion.this, PaginaPrincipal.class);
                startActivity(intentPublicaciones);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}