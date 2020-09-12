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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DatosPublicacion extends AppCompatActivity {

    private TextView mTitulo;
    private TextView mValor;
    private TextView mTipoPrenda;
    private TextView mTalla;
    private TextView mColor;
    private TextView mEstadoPrenda;
    private TextView mDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_publicacion);
        mTitulo = (TextView) findViewById(R.id.TextViewTitulo);
        mValor = (TextView) findViewById(R.id.TextViewValor);
        mTipoPrenda = (TextView) findViewById(R.id.TextViewTipo);
        mTalla = (TextView) findViewById(R.id.TextViewTalla);
        mColor = (TextView) findViewById(R.id.TextViewColor);
        mEstadoPrenda = (TextView) findViewById(R.id.TextViewEstado);
        mDescripcion = (TextView) findViewById(R.id.editTextDescripcion);

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
                Intent intentAccount = new Intent(DatosPublicacion.this, VerMiCuenta.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(DatosPublicacion.this, AddPublicaciones.class);
                startActivity(intentAdd);
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