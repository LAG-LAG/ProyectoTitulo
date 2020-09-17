package com.example.proyectotitulo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MisFavoritosDetalle extends AppCompatActivity {

    private TextView mTitulo,mPrecio,mDescripcion,mColor,mTalla,mtipoPrenda;
    private FirebaseAuth mAuth;
    private DatabaseReference clothesDb,photosDb,usersDb;
    private String idUser,idClothes;
    private String currentUId;
    private String currentOwnerUid;
    private Button mEditar, mRechazar;
    private ImageView mFotoActual;
    private int indiceFotoActual, tamanoUrlImagenes;
    private ArrayList<String> urlImagenes;
    private ImageSlider mSlider;
    private ArrayList<SlideModel> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_favoritos_detalle);
        //idOwner = getIntent().getExtras().getString("idUser");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();
        idClothes = getIntent().getExtras().getString("idClothes");

        tamanoUrlImagenes = 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalle favorito");
        }

        mEditar = (Button) findViewById(R.id.guardarPublicacionFavoritosDetalle);
        mRechazar = (Button) findViewById(R.id.descartarPublicacionFavoritosDetalle);

        urlImagenes = new ArrayList<String>();

        mTitulo = (TextView) findViewById(R.id.tituloFavoritosDetalle);
        mtipoPrenda = (TextView) findViewById(R.id.tipoFavoritosDetalle);

        mPrecio = (TextView) findViewById(R.id.precioFavoritosDetalle);
        mDescripcion = (TextView) findViewById(R.id.descripcionFavoritosDetalle);
        mColor = (TextView) findViewById(R.id.colorFavoritosDetalle);
        mTalla = (TextView) findViewById(R.id.tallaPrendaFavoritosDetalle);
        indiceFotoActual=0;
        mSlider = (ImageSlider) findViewById(R.id.fotoFavoritosDetalle);
        imageList = new ArrayList<SlideModel>();
        obtenerDatosPublicacion();
        mAuth = FirebaseAuth.getInstance();


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        mSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }
        });
        mEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //hacer boton derecha visible.
        //el onclick del boton y que se cambie la foto
        //      }

    }

    private void obtenerDatosPublicacion() {
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && !dataSnapshot.getKey().equals(currentUId)) {
                    String key = dataSnapshot.getKey();
                    currentOwnerUid = key;
                    clothesDb = usersDb.child(key).child("clothes");
                    Log.d("MFD", "primero");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dataSnapshot.exists() && dataSnapshot.getKey().equals(idClothes)) {
                                Log.d("MFD", dataSnapshot.child("tituloPublicacion").getValue().toString() + dataSnapshot.child("ValorPrenda").getValue().toString() + dataSnapshot.child("DescripcionPrenda").getValue().toString() + dataSnapshot.child("TipoPrenda").getValue().toString() + dataSnapshot.child("ColorPrenda").getValue().toString() + dataSnapshot.child("TallaPrenda").getValue().toString());
                                mTitulo.setText(dataSnapshot.child("tituloPublicacion").getValue().toString());
                                mPrecio.setText("$" + dataSnapshot.child("ValorPrenda").getValue().toString());
                                mDescripcion.setText(dataSnapshot.child("DescripcionPrenda").getValue().toString());
                                mtipoPrenda.setText(dataSnapshot.child("TipoPrenda").getValue().toString());
                                mColor.setText(dataSnapshot.child("ColorPrenda").getValue().toString());
                                mTalla.setText(dataSnapshot.child("TallaPrenda").getValue().toString());
                                guardarUrlPhotos();
                                //SUBIR FOTOS.
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

    private void mostrarFoto(String urlFotoActual) {
        Glide.with(getApplication()).clear(mFotoActual);
        switch(urlFotoActual){
            case "default":
                Picasso.get().setLoggingEnabled(true);
                //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
                Picasso.get().load(R.mipmap.ic_launcher).into(mFotoActual);
                break;
            default:
                Picasso.get().setLoggingEnabled(true);
                //Glide.with(getApplication()).load(card_item.getProfileImageUrl()).into(image);
                Picasso.get().load(urlFotoActual).into(mFotoActual);
                break;

        }
        Picasso.get().setLoggingEnabled(true);
        //Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
        Picasso.get().load(urlFotoActual).into(mFotoActual);
    }


    private void guardarUrlPhotos() {
        Log.d("weaweawea","xd");
        photosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentOwnerUid).child("clothes").child(idClothes).child("clothesPhotos");

        photosDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    urlImagenes.add(dataSnapshot.getValue().toString());
                    SlideModel aux = new SlideModel(dataSnapshot.getValue().toString(), ScaleTypes.FIT);
                    imageList.add(aux);
                    mSlider.setImageList(imageList,ScaleTypes.FIT);
                    if(indiceFotoActual==0) {
                        //   mostrarFoto(urlImagenes.get(0));
                        indiceFotoActual++;
                    }
                    tamanoUrlImagenes++;
                    //Log.d("weaweawea",dataSnapshot.getKey());
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

    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MisFavoritos.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}