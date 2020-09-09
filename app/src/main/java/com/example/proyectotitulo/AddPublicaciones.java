package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddPublicaciones extends AppCompatActivity {
    private DatabaseReference mCustomerDatabase;
    private FirebaseAuth mAuth;
    private Button mAplicar;
    private EditText mTitulo;
    private EditText mValor;
    private Spinner mTipoPrendaSpinner;
    private Spinner mTallaSpinner;
    private Spinner mColorSpinner;
    private EditText mDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publicaciones);
        mTitulo = (EditText) findViewById(R.id.editTextTitulo);
        mValor = (EditText) findViewById(R.id.editTextValor);
        mTipoPrendaSpinner = (Spinner) findViewById(R.id.TipoPrendaSpinner);
        mTallaSpinner = (Spinner) findViewById(R.id.TallaSpinner);
        mColorSpinner = (Spinner) findViewById(R.id.ColorSpinner);
        mDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
        mAplicar = (Button) findViewById(R.id.publicarBtn);
        mAuth = FirebaseAuth.getInstance();
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Añadir publicacion");
        }

        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePublicacion();
            }
        });


    }

    private void savePublicacion() {

        String userId = mAuth.getCurrentUser().getUid();
        String titulo = mTitulo.getText().toString();
        String valor = mValor.getText().toString();
        String tipoPrenda = String.valueOf(mTipoPrendaSpinner.getSelectedItem());
        String tallaPrenda = String.valueOf(mTallaSpinner.getSelectedItem());
        String colorPrenda = String.valueOf(mColorSpinner.getSelectedItem());
        String descripcionPrenda = mDescripcion.getText().toString();

        DatabaseReference currentUserNamePrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(titulo); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo

        //mRegionesSpinner.getSelectedItem();
        if(titulo != "" && valor != "" && tipoPrenda != "Seleccione tipo de prenda" && tallaPrenda != "Seleccione talla" && colorPrenda != "Seleccione color"){
            currentUserNamePrenda.setValue(mTitulo.getText().toString()); //Aca va y le asigna el nombre al User.

            DatabaseReference currentUserValorPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(titulo).child("ValorPrenda");
            currentUserValorPrenda.setValue(valor);
            DatabaseReference currentUserTipoPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(titulo).child("TipoPrenda");
            currentUserTipoPrenda.setValue(tipoPrenda);
            DatabaseReference currentUserTallaPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(titulo).child("TallaPrenda");
            currentUserTallaPrenda.setValue(tallaPrenda);
            DatabaseReference currentUserColorPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(titulo).child("ColorPrenda");
            currentUserColorPrenda.setValue(colorPrenda);
            DatabaseReference currentUserDescripcionPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(titulo).child("DescripcionPrenda");
            currentUserDescripcionPrenda.setValue(descripcionPrenda);

        }
        else{
            Toast.makeText(AddPublicaciones.this, "Datos Incorrectos.", Toast.LENGTH_SHORT).show();
        }

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
                Intent intentAccount = new Intent(AddPublicaciones.this, Account.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.chatBtn:
                Intent intentChat = new Intent(AddPublicaciones.this, Chat.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.likeBtn:
                Intent intentLike = new Intent(AddPublicaciones.this, Favorites.class);
                startActivity(intentLike);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentPublicaciones = new Intent(AddPublicaciones.this, PaginaPrincipal.class);
                startActivity(intentPublicaciones);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}