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
import android.widget.Adapter;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditarPublicacion extends AppCompatActivity {
    private DatabaseReference mCustomerDatabase;
    private static final int PICK_FROM_GALLERY = 1;
    private FirebaseAuth mAuth;
    private Button mAplicar;
    private EditText mTitulo;
    private EditText mValor;
    private Spinner mTipoPrendaSpinner;
    private Spinner mTallaSpinner;
    private Spinner mColorSpinner;
    private Spinner mEstadoPrendaSpinner;
    private EditText mDescripcion;
    private Uri resultUri;
    private Uri resultUri2;
    private Uri resultUri3;
    private Uri resultUri4;
    private Uri resultUri5;
    private Uri resultUri6;
    private String currentUId;
    private DatabaseReference mClothesDatabase;
    private DatabaseReference usersDb;
    private DatabaseReference photosDb;
    private ImageView mPublicacionImage1;
    private int publicacion;
    private int borrar1,borrar2,borrar3,borrar4,borrar5,borrar6;
    private ImageView mPublicacionImage2;
    private ImageView mPublicacionImage3;
    private ImageView mPublicacionImage4;
    private ImageView mPublicacionImage5;
    private ImageView mPublicacionImage6;
    private ImageView mBorrarPublicacion1,mBorrarPublicacion2,mBorrarPublicacion3,mBorrarPublicacion4,mBorrarPublicacion5,mBorrarPublicacion6;
    private int publicacion1,publicacion2,publicacion3,publicacion4,publicacion5,publicacion6;
    private int existoFoto1,existoFoto2,existoFoto3,existoFoto4,existoFoto5,existoFoto6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_publicacion);
        mTitulo = (EditText) findViewById(R.id.editTextTituloEditar);
        mValor = (EditText) findViewById(R.id.editTextValorEditar);
        mTipoPrendaSpinner = (Spinner) findViewById(R.id.TipoPrendaSpinnerEditar);
        mTallaSpinner = (Spinner) findViewById(R.id.TallaSpinnerEditar);
        mColorSpinner = (Spinner) findViewById(R.id.ColorSpinnerEditar);
        mEstadoPrendaSpinner = (Spinner) findViewById(R.id.estadoPrendaSpinnerEditar);
        mDescripcion = (EditText) findViewById(R.id.editTextDescripcionEditar);
        mAplicar = (Button) findViewById(R.id.publicarBtnEditar);

        mPublicacionImage1 = (ImageView) findViewById(R.id.publicacionImageCrear1Editar);
        mPublicacionImage2 = (ImageView) findViewById(R.id.publicacionImageCrear2Editar);
        mPublicacionImage3 = (ImageView) findViewById(R.id.publicacionImageCrear3Editar);
        mPublicacionImage4 = (ImageView) findViewById(R.id.publicacionImageCrear4Editar);
        mPublicacionImage5 = (ImageView) findViewById(R.id.publicacionImageCrear5Editar);
        mPublicacionImage6 = (ImageView) findViewById(R.id.publicacionImageCrear6Editar);

        mBorrarPublicacion1 = (ImageView)findViewById(R.id.borrarPublicacionCrear1Editar);
        mBorrarPublicacion2 = (ImageView)findViewById(R.id.borrarPublicacionCrear2Editar);
        mBorrarPublicacion3 = (ImageView)findViewById(R.id.borrarPublicacionCrear3Editar);
        mBorrarPublicacion4 = (ImageView)findViewById(R.id.borrarPublicacionCrear4Editar);
        mBorrarPublicacion5 = (ImageView)findViewById(R.id.borrarPublicacionCrear5Editar);
        mBorrarPublicacion6 = (ImageView)findViewById(R.id.borrarPublicacionCrear6Editar);

        mBorrarPublicacion1.setVisibility(View.INVISIBLE);
        mBorrarPublicacion2.setVisibility(View.INVISIBLE);
        mBorrarPublicacion3.setVisibility(View.INVISIBLE);
        mBorrarPublicacion4.setVisibility(View.INVISIBLE);
        mBorrarPublicacion5.setVisibility(View.INVISIBLE);
        mBorrarPublicacion6.setVisibility(View.INVISIBLE);

        existoFoto1 = 0;
        existoFoto2 = 0;
        existoFoto3 = 0;
        existoFoto4 = 0;
        existoFoto5 = 0;
        existoFoto6 = 0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();

        //if x ispressed then resulturi correspondiente = null y se
        mAuth = FirebaseAuth.getInstance();
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Editar Publicación");
        }

        obtenerInformacionPublicacion();


        mBorrarPublicacion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                   // resultUri = imageUri;

                    mPublicacionImage1.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion1.setVisibility(View.INVISIBLE);
                }
                if(existoFoto1==1){
                    mPublicacionImage1.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion1.setVisibility(View.INVISIBLE);
                    borrar1 = 1;
                }
            }
        });

        mBorrarPublicacion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri2!=null) {
                    final Uri imageUri = null;

                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                   // resultUri2 = imageUri;
                    mPublicacionImage2.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion2.setVisibility(View.INVISIBLE);
                }
                if(existoFoto2==1){
                    mPublicacionImage2.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion2.setVisibility(View.INVISIBLE);
                    borrar2 = 1;
                }
            }
        });

        mBorrarPublicacion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri3!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    //resultUri3 = imageUri;
                    mPublicacionImage3.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion3.setVisibility(View.INVISIBLE);
                }
                if(existoFoto3==1){
                    mPublicacionImage3.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion3.setVisibility(View.INVISIBLE);
                    borrar3 = 1;
                }
            }
        });

        mBorrarPublicacion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri4!=null) {
                    final Uri imageUri = null;
                    Log.d("probanding","se borro foto 4 "+borrar4);
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    //resultUri4 = imageUri;
                    mPublicacionImage4.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion4.setVisibility(View.INVISIBLE);
                }
                if(existoFoto4==1){
                    mPublicacionImage4.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion4.setVisibility(View.INVISIBLE);
                    borrar4 = 1;
                    Log.d("probanding","se borro foto 4 "+borrar4);

                }
            }
        });

        mBorrarPublicacion5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri5!=null) {

                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    //resultUri5 = imageUri;
                    mPublicacionImage5.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion5.setVisibility(View.INVISIBLE);
                }
                if(existoFoto5==1){
                    mPublicacionImage5.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion5.setVisibility(View.INVISIBLE);
                    borrar5 = 1;
                    Log.d("probanding","se borro foto 5 "+borrar5);
                }
            }
        });

        mBorrarPublicacion6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri6!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    //resultUri6 = imageUri;
                    mPublicacionImage6.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion6.setVisibility(View.INVISIBLE);
                }
                if(existoFoto6==1){
                        mPublicacionImage6.setImageResource(R.drawable.ic_launcher_foreground);
                        mBorrarPublicacion6.setVisibility(View.INVISIBLE);
                        borrar6 = 1;
                }
            }
        });

        mPublicacionImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion=1;
                publicacion1=1;
                comprobarImagen();
                mBorrarPublicacion1.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion=2;
                publicacion2=1;
                comprobarImagen();
                mBorrarPublicacion2.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion=3;
                publicacion3=1;
                comprobarImagen();
                mBorrarPublicacion3.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion=4;
                publicacion4=1;
                comprobarImagen();
                mBorrarPublicacion4.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion=5;
                publicacion5=1;
                comprobarImagen();
                mBorrarPublicacion5.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion=6;
                publicacion6=1;
                comprobarImagen();
                mBorrarPublicacion6.setVisibility(View.VISIBLE);
            }
        });
        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePublicacion();
                Intent intentPublicaciones = new Intent(EditarPublicacion.this, MisPublicaciones.class);
                startActivity(intentPublicaciones);
                finish();
            }
        });


    }

    private void obtenerInformacionPublicacion() {
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("clothes"); //esto obtiene todos los usuarios de la bd
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(getIntent().getExtras().getString("idClothes"))) {
                    mTitulo.setText(dataSnapshot.child("tituloPublicacion").getValue().toString());
                    mValor.setText(dataSnapshot.child("ValorPrenda").getValue().toString());
                    mDescripcion.setText(dataSnapshot.child("DescripcionPrenda").getValue().toString());

                    Adapter adapter = mTallaSpinner.getAdapter();
                    int n = adapter.getCount();
                    for (int i = 0; i < n; i++) {
                        String elementoSpinner = (String) adapter.getItem(i);
                        if (elementoSpinner.equals(dataSnapshot.child("TallaPrenda").getValue().toString())) {
                            mTallaSpinner.setSelection(i);
                        }
                    }

                    adapter = mColorSpinner.getAdapter();
                    n = adapter.getCount();
                    for (int i = 0; i < n; i++) {
                        String elementoSpinner = (String) adapter.getItem(i);
                        if (elementoSpinner.equals(dataSnapshot.child("ColorPrenda").getValue().toString())) {
                            mColorSpinner.setSelection(i);
                        }
                    }

                    adapter = mEstadoPrendaSpinner.getAdapter();
                    n = adapter.getCount();
                    for (int i = 0; i < n; i++) {
                        String elementoSpinner = (String) adapter.getItem(i);
                        if (elementoSpinner.equals(dataSnapshot.child("EstadoPrenda").getValue().toString())) {
                            mEstadoPrendaSpinner.setSelection(i);
                        }
                    }

                    adapter = mTipoPrendaSpinner.getAdapter();
                    n = adapter.getCount();
                    for (int i = 0; i < n; i++) {
                        String elementoSpinner = (String) adapter.getItem(i);
                        if (elementoSpinner.equals(dataSnapshot.child("TipoPrenda").getValue().toString())) {
                            mTipoPrendaSpinner.setSelection(i);
                        }
                    }
                    photosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("clothes").child(dataSnapshot.getKey()).child("clothesPhotos"); //esto obtiene todos los usuarios de la bd
                    photosDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists()){

                                if(dataSnapshot.getKey().equals("photoId1")){
                                    mBorrarPublicacion1.setVisibility(View.VISIBLE);
                                    asignarFoto(dataSnapshot.getValue().toString(), mPublicacionImage1);
                                    publicacion1=1;
                                    existoFoto1=1;
                                }
                                if(dataSnapshot.getKey().equals("photoId2")){
                                    mBorrarPublicacion2.setVisibility(View.VISIBLE);
                                    asignarFoto(dataSnapshot.getValue().toString(), mPublicacionImage2);
                                    publicacion2=1;
                                    existoFoto2=1;
                                }
                                if(dataSnapshot.getKey().equals("photoId3")){
                                    mBorrarPublicacion3.setVisibility(View.VISIBLE);
                                    asignarFoto(dataSnapshot.getValue().toString(),mPublicacionImage3);
                                    publicacion3=1;
                                    existoFoto3=1;
                                }
                                if(dataSnapshot.getKey().equals("photoId4")){
                                    mBorrarPublicacion4.setVisibility(View.VISIBLE);
                                    asignarFoto(dataSnapshot.getValue().toString(), mPublicacionImage4);
                                    publicacion4=1;
                                    existoFoto4=1;
                                }
                                if(dataSnapshot.getKey().equals("photoId5")){
                                    mBorrarPublicacion5.setVisibility(View.VISIBLE);
                                    asignarFoto(dataSnapshot.getValue().toString(), mPublicacionImage5);
                                    publicacion5=1;
                                    existoFoto5=1;
                                }
                                if(dataSnapshot.getKey().equals("photoId6")){
                                    mBorrarPublicacion6.setVisibility(View.VISIBLE);
                                    asignarFoto(dataSnapshot.getValue().toString(), mPublicacionImage6);
                                    publicacion6=1;
                                    existoFoto6=1;
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

    private void asignarFoto(String photoId1, ImageView mPublicacionImage1) {
        switch(photoId1) {
            case "default":
                Picasso.get().setLoggingEnabled(true);
                //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
                Picasso.get().load(R.mipmap.ic_launcher).into(mPublicacionImage1);
                break;
            default:
                Picasso.get().setLoggingEnabled(true);
                //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
                Picasso.get().load(photoId1).into(mPublicacionImage1);
                break;
        }

        Picasso.get().setLoggingEnabled(true);
        //Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
        Picasso.get().load(photoId1).into(mPublicacionImage1);
    }


    private void comprobarImagen() {
        if (ActivityCompat.checkSelfPermission(EditarPublicacion.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditarPublicacion.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();

            switch (publicacion){
                case 1:
                    resultUri = imageUri;
                    mPublicacionImage1.setImageURI(resultUri);
                    publicacion1=0;
                    existoFoto1=1;
                    borrar1 = 0;
                    publicacion=0;
                    break;
                case 2:
                    resultUri2 = imageUri;
                    mPublicacionImage2.setImageURI(resultUri2);
                    publicacion2=0;
                    existoFoto2=1;
                    borrar2 = 0;
                    publicacion=0;
                    break;
                case 3:
                    resultUri3 = imageUri;
                    mPublicacionImage3.setImageURI(resultUri3);
                    publicacion3=0;
                    existoFoto3=1;
                    borrar3 = 0;
                    publicacion=0;
                    break;
                case 4:
                    resultUri4 = imageUri;
                    mPublicacionImage4.setImageURI(resultUri4);
                    publicacion4=0;
                    existoFoto4=1;
                    borrar4 = 0;
                    publicacion=0;
                    break;
                case 5:
                    resultUri5 = imageUri;
                    mPublicacionImage5.setImageURI(resultUri5);
                    publicacion5=0;
                    existoFoto5=1;
                    borrar5 = 0;
                    publicacion=0;
                    break;
                case 6:
                    resultUri6 = imageUri;
                    mPublicacionImage6.setImageURI(resultUri6);
                    publicacion6=0;
                    existoFoto5=1;
                    borrar6 = 0;
                    publicacion=0;
                    break;
                default:
                    Log.d("gungaginga","gungagigna");
                    break;
            }
            /*
            if(publicacion1==1){
                resultUri = imageUri;
                mPublicacionImage1.setImageURI(resultUri);
                publicacion1=0;
            }
            else if(publicacion2==1) {
                resultUri2 = imageUri;
                mPublicacionImage2.setImageURI(resultUri2);
                publicacion2=0;
            }
            else if(publicacion3==1) {
                resultUri3 = imageUri;
                mPublicacionImage3.setImageURI(resultUri3);
                publicacion3=0;
            }
            else if(publicacion4==1) {
                resultUri4 = imageUri;
                mPublicacionImage4.setImageURI(resultUri4);
                publicacion4=0;
            }
            else if(publicacion5==1) {
                resultUri5 = imageUri;
                mPublicacionImage5.setImageURI(resultUri5);
                publicacion5=0;
            }
            else if(publicacion6==1) {
                resultUri6 = imageUri;
                mPublicacionImage6.setImageURI(resultUri6);
                publicacion6=0;
            }*/
        }
    }

    private void savePublicacion() {

        String userId = mAuth.getCurrentUser().getUid();
        String id = getIntent().getExtras().getString("idClothes");
        DatabaseReference currentUserNamePrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("tituloPublicacion"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
        String titulo = mTitulo.getText().toString();
        String valor = mValor.getText().toString();
        String tipoPrenda = String.valueOf(mTipoPrendaSpinner.getSelectedItem());
        String tallaPrenda = String.valueOf(mTallaSpinner.getSelectedItem());
        String colorPrenda = String.valueOf(mColorSpinner.getSelectedItem());
        String estadoPrenda = String.valueOf(mEstadoPrendaSpinner.getSelectedItem());
        String descripcionPrenda = mDescripcion.getText().toString();
        //DatabaseReference dueño = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("idDueño");
        //dueño.setValue(userId);

        //mRegionesSpinner.getSelectedItem();
        if(titulo != "" && valor != "" && tipoPrenda != "Seleccione tipo de prenda" && tallaPrenda != "Seleccione talla" && colorPrenda != "Seleccione color" && estadoPrenda != "Seleccione estado"){
            currentUserNamePrenda.setValue(mTitulo.getText().toString()); //Aca va y le asigna el nombre al User.

            DatabaseReference currentUserValorPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("ValorPrenda");
            currentUserValorPrenda.setValue(valor);
            DatabaseReference currentUserTipoPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("TipoPrenda");
            currentUserTipoPrenda.setValue(tipoPrenda);
            DatabaseReference currentUserTallaPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("TallaPrenda");
            currentUserTallaPrenda.setValue(tallaPrenda);
            DatabaseReference currentUserColorPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("ColorPrenda");
            currentUserColorPrenda.setValue(colorPrenda);
            DatabaseReference currentUserEstadoPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("EstadoPrenda");
            currentUserEstadoPrenda.setValue(estadoPrenda);
            DatabaseReference currentUserDescripcionPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("DescripcionPrenda");
            currentUserDescripcionPrenda.setValue(descripcionPrenda);

        }
        else{
            Toast.makeText(EditarPublicacion.this, "Datos Incorrectos.", Toast.LENGTH_SHORT).show();
        }
        guardarImagen(resultUri,userId,"1",id);
        guardarImagen(resultUri2,userId,"2",id);
        guardarImagen(resultUri3,userId,"3",id);
        guardarImagen(resultUri4,userId,"4",id);
        guardarImagen(resultUri5,userId,"5",id);
        guardarImagen(resultUri6,userId,"6",id);

        borrarImagen(resultUri,userId,"photoId1",id,borrar1);
        borrarImagen(resultUri2,userId,"photoId2",id,borrar2);
        borrarImagen(resultUri3,userId,"photoId3",id,borrar3);
        borrarImagen(resultUri4,userId,"photoId4",id,borrar4);
        borrarImagen(resultUri5,userId,"photoId5",id,borrar5);
        borrarImagen(resultUri6,userId,"photoId6",id,borrar6);

    }

    private void borrarImagen(Uri resultUri, String userId, final String idClotheBorrar, final String photoIdBorrar, int borrar) {
        Log.d("probanding","no entro al correspondiente borrar."+photoIdBorrar);
        if(borrar==1){
            Log.d("probanding","entro al correspondiente borrar.");
            usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("clothes").child(photoIdBorrar).child("clothesPhotos");
            usersDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.getKey().equals(idClotheBorrar)){
                            Log.d("probanding","entro");
                            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("clothes").child(photoIdBorrar).child("clothesPhotos").child(idClotheBorrar).removeValue();
                            //FirebaseStorage.getInstance().getReference().child("profileImages").child(userId).child(map.get("profilesImages").toString()).delete();
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
    }

    private void guardarImagen(Uri resultUri, String userId, final String idPrenda,String id) {
        if (resultUri != null) {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("prendasImages").child(userId).child(id).child(idPrenda);
            Bitmap bitmap = null;
            mClothesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("clothesPhotos");

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
                            newImage.put("photoId" + idPrenda, uri.toString());
                            mClothesDatabase.updateChildren(newImage);
                            //finish();
                            //return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //finish();
                            //return;
                        }
                    });
                }
            });
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
                Intent intentAccount = new Intent(EditarPublicacion.this, VerMiCuenta.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.chatBtn:
                Intent intentChat = new Intent(EditarPublicacion.this, Chat.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentPublicaciones = new Intent(EditarPublicacion.this, PaginaPrincipal.class);
                startActivity(intentPublicaciones);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}