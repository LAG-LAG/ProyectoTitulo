package com.mrswapdrobe.swapdrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class VerMiCuenta extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private ImageView mProfileImage, mborrarFotoPerfil;
    private Button mEditarBtn;
    private TextView mTextViewNombre;
    private TextView mTextViewComuna;
    private TextView mTextViewRegion;
    private Button misPublicacionesBtn;
    private Button misPublicacionesVendidasBtn;
    private Button misFavoritosBtn;
    private String profileImageUrl, nombreUsuario, regionAnterior, comunaAnterior;
    private int existeFotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mi_cuenta);

        mProfileImage = (ImageView) findViewById(R.id.ImagenPerfilUrl);
        mTextViewNombre = (TextView) findViewById(R.id.TextViewNombre);
        mTextViewComuna = (TextView) findViewById(R.id.TextViewComuna);
        mTextViewRegion = (TextView) findViewById(R.id.TextViewRegion);
        misPublicacionesBtn = (Button) findViewById(R.id.misPublicacionesBtn);
        misPublicacionesVendidasBtn = (Button) findViewById(R.id.misPublicacionesVendidasBtn);
        misFavoritosBtn = (Button) findViewById(R.id.misFavoritosBtn);
        mEditarBtn = (Button) findViewById(R.id.editarBtn);

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cuenta");
        }

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid(); //aca obtiene el uid de la cuenta
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        getUserInfo();

        mEditarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerMiCuenta.this, Account.class);
                startActivity(intent);
                finish();
            }
        });

        misPublicacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerMiCuenta.this, MisPublicaciones.class);
                startActivity(intent);
                finish();
            }
        });

        misPublicacionesVendidasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerMiCuenta.this, MisPublicacionesVendidas.class);
                startActivity(intent);
                finish();
            }
        });

        misFavoritosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerMiCuenta.this, MisFavoritos.class);
                startActivity(intent);
                finish();
            }
        });
    }
/*
    private void getUserInfo(){
        mCustomerDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.a
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("nameUser") != null) {
                        nombreUsuario = map.get("nameUser").toString();
                        mTextViewNombre.setText(nombreUsuario);
                    }

                    if (map.get("region") != null) {
                        regionAnterior = map.get("region").toString();
                        comunaAnterior = map.get("comuna").toString();
                        mTextViewRegion.setText(regionAnterior);
                        mTextViewComuna.setText(comunaAnterior);
                    }


//esto de aca es para cargar la foto de perfil del usuario
                    Glide.with(getApplication()).clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        //mborrarFotoPerfil.setVisibility(View.VISIBLE);
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Picasso.get().setLoggingEnabled(true);
                                //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
                                Picasso.get().load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;
                            default:
                                Picasso.get().setLoggingEnabled(true);
                                //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
                                Picasso.get().load(profileImageUrl).into(mProfileImage);
                                break;

                        }
                        Picasso.get().setLoggingEnabled(true);
                        //Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        Picasso.get().load(profileImageUrl).into(mProfileImage);
                        existeFotoPerfil = 1;
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

 */

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.a
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("nameUser") != null) {
                        nombreUsuario = map.get("nameUser").toString();
                        mTextViewNombre.setText(nombreUsuario);
                    }

                    if (map.get("region") != null) {
                        regionAnterior = map.get("region").toString();
                        comunaAnterior = map.get("comuna").toString();
                        mTextViewRegion.setText(regionAnterior);
                        mTextViewComuna.setText(comunaAnterior);
                    }


//esto de aca es para cargar la foto de perfil del usuario
                    Glide.with(getApplication()).clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        //mborrarFotoPerfil.setVisibility(View.VISIBLE);
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                //Picasso.get().setLoggingEnabled(true);
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                //Picasso.get().load(R.mipmap.ic_launcher).fit().centerCrop().into(mProfileImage);
                                break;
                            default:
                                //Picasso.get().setLoggingEnabled(true);
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                //Picasso.get().load(profileImageUrl).fit().centerCrop().into(mProfileImage);
                                break;

                        }
                        //Picasso.get().setLoggingEnabled(true);
                        Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        //Picasso.get().load(profileImageUrl).fit().centerCrop().into(mProfileImage);
                        existeFotoPerfil = 1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            case R.id.chatBtn:
                Intent intentChat = new Intent(VerMiCuenta.this, misChats.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(VerMiCuenta.this, AddPublicaciones.class);
                startActivity(intentAdd);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentAccount = new Intent(VerMiCuenta.this, PaginaPrincipal.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.cerrarSesionBtn:
                mAuth.signOut(); //desconecta
                //las lineas de abajo mandan de la ventana actual(mainactiviy) a la de chooseloginregistration que es la de antes de estar loguado.
                Intent intent = new Intent(VerMiCuenta.this,Login.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}