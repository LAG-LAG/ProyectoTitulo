package com.example.proyectotitulo;

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

import java.util.ArrayList;

public class MiPublicacionDetalle extends AppCompatActivity {

    private TextView mTitulo,mPrecio,mDescripcion,mColor,mTalla,mtipoPrenda;
    private FirebaseAuth mAuth;
    private DatabaseReference clothesDb,photosDb,usersDb;
    private ImageView mAdelanteButton,mAtrasButton;
    private ImageView mFotoActual;
    private String idOwner,idClothes;
    private Button mGuardar, mRechazar;
    private int indiceFotoActual, tamanoUrlImagenes;
    private ArrayList<String> urlImagenes;
    private ImageSlider mSlider;
    private ArrayList<SlideModel> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_publicacion_detalle);
        idOwner = getIntent().getExtras().getString("idUser");
        idClothes = getIntent().getExtras().getString("idClothes");

        tamanoUrlImagenes = 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Publicaciones");
        }
        //mAdelanteButton = (ImageView) findViewById(R.id.adelanteDetalleButton);
        //mAtrasButton = (ImageView) findViewById(R.id.atrasDetalleButton);

        mGuardar = (Button) findViewById(R.id.guardarPublicacionDetallePropia);
        mRechazar = (Button) findViewById(R.id.descartarPublicacionDetallePropia);
        //      mAdelanteButton.setVisibility(View.INVISIBLE);
//        mAtrasButton.setVisibility(View.INVISIBLE);

        urlImagenes = new ArrayList<String>();
        //mFotoActual = (ImageView) findViewById(R.id.fotoDetallePublicacion);

        mTitulo = (TextView) findViewById(R.id.tituloDetallePublicacionPropia);
        mtipoPrenda = (TextView) findViewById(R.id.tipoPrendaDetallePropia);

        mPrecio = (TextView) findViewById(R.id.precioDetallePublicacionPropia);
        mDescripcion = (TextView) findViewById(R.id.descripcionPrendaDetallePropia);
        mColor = (TextView) findViewById(R.id.colorPrendaDetallePropia);
        mTalla = (TextView) findViewById(R.id.tallaPrendaDetallePropia);
        indiceFotoActual=0;
        mSlider = (ImageSlider) findViewById(R.id.fotoDetallePublicacionPropia);
        imageList = new ArrayList<SlideModel>();
        Log.d("weaweawea","1si");
        obtenerDatosPublicacion();
        mAuth = FirebaseAuth.getInstance();



        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        //mAdelanteButton.setVisibility(View.INVISIBLE);

//        if(indiceFotoActual<tamañoUrlImagenes){
        //indiceFotoActual++;
        /*
            mAdelanteButton.setVisibility(View.VISIBLE);
            Log.d("weaweawea","xd "+tamanoUrlImagenes);
            mAdelanteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("weaweawea","tamaño "+tamanoUrlImagenes);
                    Log.d("weaweawea","indice "+indiceFotoActual);
                    if(tamanoUrlImagenes-1!=0) {
                        mostrarFoto(urlImagenes.get(indiceFotoActual));
                    }
                    if(indiceFotoActual>=tamanoUrlImagenes-1){
                        Log.d("weaweawea","uno");
                        indiceFotoActual=0;
                    }
                    else {
                        Log.d("weaweawea","dos");
                        indiceFotoActual++;
                    }
                }
            });
        mAtrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("weaweawea","indice "+indiceFotoActual);
                if(tamanoUrlImagenes-1!=0) {
                    mostrarFoto(urlImagenes.get(indiceFotoActual));
                }
                if(indiceFotoActual<=0){
                    Log.d("weaweawea","uno");
                    indiceFotoActual=tamanoUrlImagenes-1;
                }
                else {
                    Log.d("weaweawea","dos");
                    indiceFotoActual--;
                }
            }
        });
*/
        mSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }
        });
        mGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(true);
                Intent intent = new Intent(MiPublicacionDetalle.this,EditarPublicacion.class);
                intent.putExtra("idClothes",idClothes);
                startActivity(intent);
            }
        });
        mRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesRechazadas").child(idClothes).setValue(true);
                Intent intent = new Intent(MiPublicacionDetalle.this,PaginaPrincipal.class);
                startActivity(intent);
            }
        });

        //hacer boton derecha visible.
        //el onclick del boton y que se cambie la foto
        //      }

    }

    private void obtenerDatosPublicacion() {
        clothesDb = FirebaseDatabase.getInstance().getReference().child("Users").child(idOwner).child("clothes");
        clothesDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idClothes)){
                    mTitulo.setText(dataSnapshot.child("tituloPublicacion").getValue().toString());
                    mPrecio.setText("$"+dataSnapshot.child("ValorPrenda").getValue().toString());
                    mDescripcion.setText(dataSnapshot.child("DescripcionPrenda").getValue().toString());
                    mtipoPrenda.setText(dataSnapshot.child("TipoPrenda").getValue().toString());
                    mColor.setText(dataSnapshot.child("ColorPrenda").getValue().toString());
                    mTalla.setText(dataSnapshot.child("TallaPrenda").getValue().toString());
                    guardarUrlPhotos();
                    //SUBIR FOTOS.
                    if(dataSnapshot.hasChild("estaVendida")){
                        mGuardar.setVisibility(View.INVISIBLE);
                        mRechazar.setVisibility(View.INVISIBLE);
                    }
                    else{
                        mGuardar.setVisibility(View.VISIBLE);
                        mRechazar.setVisibility(View.VISIBLE);
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


    private void guardarUrlPhotos() {
        Log.d("weaweawea","xd");

        photosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(idOwner).child("clothes").child(idClothes).child("clothesPhotos");

        photosDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Log.d("weaweawea","datasnapshot exists");
                    urlImagenes.add(dataSnapshot.getValue().toString());
                    SlideModel aux = new SlideModel(dataSnapshot.getValue().toString(),ScaleTypes.FIT);
                    imageList.add(aux);
                    mSlider.setImageList(imageList,ScaleTypes.FIT);
                    if(indiceFotoActual==0) {
                        Log.d("weaweawea","indice");

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
        //Intent myIntent = new Intent(getApplicationContext(), MisPublicaciones.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}