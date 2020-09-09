package com.example.proyectotitulo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Account extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 1;
    private DatabaseReference mCustomerDatabase;
    private FirebaseAuth mAuth;
    private Button mAplicar;
    private EditText mNombre;
    private ImageView mProfileImage;
    private String nombreUsuario;
    private Spinner mRegionesSpinner;
    private String comunaAnterior,profileImageUrl;
    private String regionAnterior;
    private Spinner mComunasSpinner;
    private Uri resultUri;
    private int estadoComunas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAplicar = (Button) findViewById(R.id.aplicar);
        mNombre = (EditText) findViewById(R.id.name);
        mProfileImage = (ImageView) findViewById(R.id.profileImageUrl);
        mRegionesSpinner = (Spinner) findViewById(R.id.regionesSpinner);
        mComunasSpinner = (Spinner) findViewById(R.id.comunasSpinner);
        estadoComunas = 0;


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

        llenarComboBoxRegiones();


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Account.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Account.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }
            }
        });


        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();

            }

        });

        mRegionesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String nombreRegion = mRegionesSpinner.getSelectedItem().toString();
                final String regionAnterior;
                //txt_region.setText(nombreRegion);

                String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
                Log.i("data", jsonFileString);

                Gson gson = new Gson();
                Type listUserType = new TypeToken<List<cities>>() { }.getType();

                List<cities> cities = gson.fromJson(jsonFileString, listUserType);

                List<String> list = new ArrayList<String>();

                if(estadoComunas==1){
                    int posicionComuna=0;
                    List<String> comunas = cities.get(position-1).getComunas(); //esto esta bien, lo revise con toast y posicionregion-1 corresponde a lo que buscamos.
                    //Toast.makeText(Account.this,cities.get(posicionRegion-1).region,Toast.LENGTH_SHORT).show();

                    for (int x = 0; x < comunas.size(); x++) {
                        if(comunas.get(x).equals(comunaAnterior)){
                            posicionComuna = x;
                            break;
                        }
                    }

                    //Toast.makeText(Account.this, posicionComuna, Toast.LENGTH_SHORT).show();
                    ArrayAdapter<String> arrayAdapterComunas = new ArrayAdapter<>(Account.this, android.R.layout.simple_expandable_list_item_1,comunas);
                    arrayAdapterComunas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mComunasSpinner.setAdapter(arrayAdapterComunas);
                    mComunasSpinner.setSelection(posicionComuna);
                }
                else if(position!=0) {
                    List<String> comunas = cities.get(position - 1).getComunas();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, comunas);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mComunasSpinner.setAdapter(adapter);
                }

                else{
                    List<String> listVacia = new ArrayList<String>();
                    listVacia.add("Seleccione Comuna");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, listVacia);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    mComunasSpinner.setAdapter(adapter);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //aca hay que añadir que cuando no seleccione nada, se borre el spinner de comunas y solo deje seleccione comuna.
            }
        });

    }

    private void saveUserInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        String regionGuardar = String.valueOf(mRegionesSpinner.getSelectedItem());
        String comunaGuardar = String.valueOf(mComunasSpinner.getSelectedItem());

        DatabaseReference currentUserName = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("nameUser"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
        //mRegionesSpinner.getSelectedItem();
        if(regionGuardar != "Seleccione Región" && comunaGuardar != "Seleccione Comuna" && mNombre.getText().toString() != ""){
            DatabaseReference currentUserRegion = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("region"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
            currentUserRegion.setValue(regionGuardar);
            DatabaseReference currentUserComuna = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("comuna");
            currentUserComuna.setValue(comunaGuardar);
            currentUserName.setValue(mNombre.getText().toString()); //Aca va y le asigna el nombre al User.
        }
        else{
            Toast.makeText(Account.this, "Datos Incorrectos.", Toast.LENGTH_SHORT).show();
        }

        //aca lo de la imagen

        if(resultUri != null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileImageUrl", uri.toString());
                            mCustomerDatabase.updateChildren(newImage);
                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            finish();
                            return;
                        }
                    });
                }
            });




        }else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);

        }
    }

    private void llenarComboBoxRegiones() {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
        Log.i("data", jsonFileString);
        int posicionRegion = 0;
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<cities>>() {
        }.getType();

        List<cities> cities = gson.fromJson(jsonFileString, listUserType);

        List<String> list = new ArrayList<String>();
        list.add("Seleccione Región");
        for (int i = 0; i < cities.size(); i++) {
            list.add(cities.get(i).region);
            if (cities.get(i).region.equals(regionAnterior)) {
                posicionRegion = i+1;
            }
        }
        //Toast.makeText(this, regionAnterior,Toast.LENGTH_LONG).show();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRegionesSpinner.setAdapter(dataAdapter);
        mRegionesSpinner.setSelection(posicionRegion);


    }

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CountDownLatch done = new CountDownLatch(1);
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.a
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("nameUser") != null) {
                        nombreUsuario = map.get("nameUser").toString();
                        mNombre.setText(nombreUsuario);
                    }

                    if (map.get("region") != null) {
                        regionAnterior = map.get("region").toString();
                        comunaAnterior = map.get("comuna").toString();
                        ArrayList<String> regiones = new ArrayList<>();
                        regiones.add(regionAnterior);

                        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
                        Log.i("data", jsonFileString);
                        int posicionRegion = 0;
                        Gson gson = new Gson();
                        Type listUserType = new TypeToken<List<cities>>() {
                        }.getType();

                        List<cities> cities = gson.fromJson(jsonFileString, listUserType);

                        List<String> list = new ArrayList<String>();
                        list.add("Seleccione Región");
                        for (int i = 0; i < cities.size(); i++) {
                            list.add(cities.get(i).region);
                            if (cities.get(i).region.equals(regionAnterior)) {
                                posicionRegion = i+1;
                            }
                        }
                        //Toast.makeText(this, regionAnterior,Toast.LENGTH_LONG).show();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Account.this, android.R.layout.simple_expandable_list_item_1,list);
                        mRegionesSpinner.setAdapter(arrayAdapter);
                        mRegionesSpinner.setSelection(posicionRegion);

                        //aca ve las comunas/////////////////

/*
                        int posicionComuna=0;
                        List<String> comunas = cities.get(posicionRegion-1).getComunas(); //esto esta bien, lo revise con toast y posicionregion-1 corresponde a lo que buscamos.
                        //Toast.makeText(Account.this,cities.get(posicionRegion-1).region,Toast.LENGTH_SHORT).show();

                        for (int x = 0; x < comunas.size(); x++) {
                            if(comunas.get(x).equals(comunaAnterior)){
                                posicionComuna = x;
                                break;
                            }
                        }

                        //Toast.makeText(Account.this, posicionComuna, Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> arrayAdapterComunas = new ArrayAdapter<>(Account.this, android.R.layout.simple_expandable_list_item_1,comunas);
                        //arrayAdapterComunas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mComunasSpinner.setAdapter(arrayAdapterComunas);
                        mComunasSpinner.setSelection(4);
                        */

                        estadoComunas=1;
                    }


//esto de aca es para cargar la foto de perfil del usuario
                    Glide.with(getApplication()).clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /*


     */
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
            case R.id.chatBtn:
                Intent intentChat = new Intent(Account.this, Chat.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(Account.this, AddPublicaciones.class);
                startActivity(intentAdd);
                finish();
                break;

            case R.id.likeBtn:
                Intent intentLike = new Intent(Account.this, Favorites.class);
                startActivity(intentLike);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentAccount = new Intent(Account.this, PaginaPrincipal.class);
                startActivity(intentAccount);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}