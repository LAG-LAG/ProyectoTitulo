package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
public class Account extends AppCompatActivity {
    private DatabaseReference mCustomerDatabase;
    private FirebaseAuth mAuth;
    private Button mAplicar;
    private EditText mNombre;
    private String nombreUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAplicar = (Button) findViewById(R.id.aplicar);
        mNombre = (EditText) findViewById(R.id.name);
/*
        //////////
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
        Log.i("data", jsonFileString);

        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<cities>>() { }.getType();

        List<cities> cities = gson.fromJson(jsonFileString, listUserType);
        for (int i = 0; i < cities.size(); i++) {
            Log.i("data", "> Item " + i + "\n" + cities.get(i));
        }

        ///////////

 */
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cuenta");
        }

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid(); //aca obtiene el uid de la cuenta, para la ropa abria que hacer esto, cada ropa tiene su uid y linkearla de alguna forma.
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        getUserInfo();

        
        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("addressUser"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
                DatabaseReference currentUserDbDos = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("nameUser"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
                currentUserDbDos.setValue(mNombre.getText().toString()); //Aca va y le asigna el nombre al User.
            }

        });

    }

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.a
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("nameUser") != null) {
                        nombreUsuario = map.get("nameUser").toString();
                        mNombre.setText(nombreUsuario);
                    }
                }
            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    public void back(View view) {
        Intent intent = new Intent(Account.this, PaginaPrincipal.class);
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