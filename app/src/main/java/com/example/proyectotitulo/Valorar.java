package com.example.proyectotitulo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Valorar extends AppCompatActivity {
    private Button mEnviar;
    private TextView mTitulo;
    private ImageView mImage;
    private RatingBar mRBestado, mRBtrato, mRBpuntualidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Valorar");
        }

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
        Log.d("rating", "estado producto " + mRBestado.getRating());
        Log.d("rating", "estado producto " + mRBestado.getRating());
        Log.d("rating", "estado producto " + mRBestado.getRating());
    }

    //toolbar
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ChatUserActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }*/
}