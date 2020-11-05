package com.mrswapdrobe.swapdrobe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
Esta clase se encarga de mostrar los datos de la publicacion en la vista activity_detalle_publicacion.xml.
entrada: recibe el id de la prenda y del usuario que publico la prenda y obtiene los datos de esa prenda.
salida: permite ir al detalle del vendedor.
 */

public class detallePublicacion extends AppCompatActivity {
    private TextView mTitulo,mPrecio,mDescripcion,mColor,mTalla,mtipoPrenda,mVendedor,mNombreVendedor,mEstado;
    private FirebaseAuth mAuth;
    private DatabaseReference clothesDb,photosDb,usersDb;
    private ImageView mAdelanteButton,mAtrasButton;
    private ImageView mFotoActual;
    private String idOwner,idClothes;
    private Button mEditar, mRechazar, mVerPerfil;
    private int indiceFotoActual, tamanoUrlImagenes;
    private ArrayList<String> urlImagenes;
    private ImageSlider mSlider;
    private ArrayList<SlideModel> imageList;
    private ChildEventListener childEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicacion);
        idOwner = getIntent().getExtras().getString("idOwner");
        idClothes = getIntent().getExtras().getString("idClothes");
        Log.d("entro","Owner "+idOwner);
        Log.d("entro","Clothes "+idClothes);
        tamanoUrlImagenes = 0;
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalle publicacion");
        }


        mEditar = (Button) findViewById(R.id.guardarPublicacionDetalle);
        mRechazar = (Button) findViewById(R.id.descartarPublicacionDetalle);
        mVerPerfil = (Button) findViewById(R.id.verPerfilBtnPublicacionDetalle);

        urlImagenes = new ArrayList<String>();

        mTitulo = (TextView) findViewById(R.id.tituloDetallePublicacion);
        mtipoPrenda = (TextView) findViewById(R.id.tipoPrendaDetalle);

        mVendedor = (TextView) findViewById(R.id.vendedorDetallePublicacion);
        mNombreVendedor = (TextView) findViewById(R.id.nombreVendedorDetallePublicacion);
        mPrecio = (TextView) findViewById(R.id.precioDetallePublicacion);
        mDescripcion = (TextView) findViewById(R.id.descripcionPrendaDetalle);
        mColor = (TextView) findViewById(R.id.colorPrendaDetalle);
        mTalla = (TextView) findViewById(R.id.tallaPrendaDetalle);
        mEstado = (TextView) findViewById(R.id.estadoPrendaDetalle);
        indiceFotoActual=0;
        mSlider = (ImageSlider) findViewById(R.id.fotoDetallePublicacion);
        imageList = new ArrayList<SlideModel>();
        obtenerDatosPublicacion();
        obtenerNombreDueño();
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
                usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(true);
                Intent intent = new Intent(detallePublicacion.this,PaginaPrincipal.class);
                startActivity(intent);
            }
        });
        mRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesRechazadas").child(idClothes).setValue(true);
                Intent intent = new Intent(detallePublicacion.this,PaginaPrincipal.class);
                startActivity(intent);
            }
        });
        mVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detallePublicacion.this,VerPerfilDeVendedor.class);
                intent.putExtra("idClothes",idClothes);
                intent.putExtra("idOwner",idOwner);
                startActivity(intent);
            }
        });

            //hacer boton derecha visible.
            //el onclick del boton y que se cambie la foto
  //      }

    }

    private void obtenerNombreDueño() {
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idOwner)){
                    mNombreVendedor.setText(dataSnapshot.child("nameUser").getValue().toString());
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


    private void obtenerDatosPublicacion() {

        clothesDb = FirebaseDatabase.getInstance().getReference().child("Users").child(idOwner).child("clothes");
        clothesDb = FirebaseDatabase.getInstance().getReference().child("Users").child(idOwner).child("clothes");
        childEventListener =clothesDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idClothes)){
                    Log.d("entro","entro a obtener datos");
                    mTitulo.setText(dataSnapshot.child("tituloPublicacion").getValue().toString());
                    mPrecio.setText("$"+dataSnapshot.child("ValorPrenda").getValue().toString());
                    mDescripcion.setText(dataSnapshot.child("DescripcionPrenda").getValue().toString());
                    mtipoPrenda.setText(dataSnapshot.child("TipoPrenda").getValue().toString());
                    mColor.setText(dataSnapshot.child("ColorPrenda").getValue().toString());
                    mTalla.setText(dataSnapshot.child("TallaPrenda").getValue().toString());
                    mEstado.setText(dataSnapshot.child("EstadoPrenda").getValue().toString());
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
        photosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(idOwner).child("clothes").child(idClothes).child("clothesPhotos");

        photosDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    urlImagenes.add(dataSnapshot.getValue().toString());
                    SlideModel aux = new SlideModel(dataSnapshot.getValue().toString(),ScaleTypes.FIT);
                    imageList.add(aux);
                    mSlider.setImageList(imageList,ScaleTypes.FIT);
                    if(indiceFotoActual==0) {
                        indiceFotoActual++;
                    }
                    tamanoUrlImagenes++;
                    clothesDb.removeEventListener(childEventListener);
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
        Intent myIntent = new Intent(getApplicationContext(), PaginaPrincipal.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}