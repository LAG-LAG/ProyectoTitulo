package com.mrswapdrobe.swapdrobe;
/*
Esta clase se encarga de mostrar los datos de la publicacion en la vista activity_mi_publicacion_detalle.xml.
entrada: recibe el id de la prenda y del usuario que publico la prenda y obtiene los datos de esa prenda.
salida: permite editar la publicacion o eliminarla.
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MiPublicacionDetalle extends AppCompatActivity {

    private TextView mTitulo,mPrecio,mDescripcion,mColor,mTalla,mtipoPrenda,mEstado,mTextViewTrato,mTextViewEstado,mTextViewPuntualidad;
    private LinearLayout mLinearLayoutValoracion;
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
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private int verPerfilVendedor = 0;
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
        //mRechazar = (Button) findViewById(R.id.descartarPublicacionDetallePropia);

        if(getIntent().getExtras().getString("verPerfilVendedor")!=null){
            Log.d("verVendedor","gungaginga");
            verPerfilVendedor = 1;
            mGuardar.setText("Guardar");
            //mRechazar.setVisibility(View.INVISIBLE);
        }
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
        mEstado = (TextView) findViewById(R.id.estadoPrendaDetallePropia);

        mLinearLayoutValoracion = (LinearLayout) findViewById(R.id.linearLayoutValoracion);
        mTextViewTrato = (TextView) findViewById(R.id.TvValoracionTrato);
        mTextViewEstado = (TextView) findViewById(R.id.TvValoracionEstado);
        mTextViewPuntualidad = (TextView) findViewById(R.id.TvValoracionPuntualidad);

        indiceFotoActual=0;

        //mSlider = (ImageSlider) findViewById(R.id.fotoDetallePublicacionPropia);
        //imageList = new ArrayList<SlideModel>();

        viewPager = (ViewPager) findViewById(R.id.fotoDetallePublicacionPropia);
        adapter = new ViewPagerAdapter(MiPublicacionDetalle.this, urlImagenes);
        viewPager.setAdapter(adapter);


        obtenerDatosPublicacion();
        mAuth = FirebaseAuth.getInstance();



        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd

        //mAdelanteButton.setVisibility(View.INVISIBLE);

//        if(indiceFotoActual<tamañoUrlImagenes){
        //indiceFotoActual++;
        /*
            mAdelanteButton.setVisibility(View.VISIBLE);
            mAdelanteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tamanoUrlImagenes-1!=0) {
                        mostrarFoto(urlImagenes.get(indiceFotoActual));
                    }
                    if(indiceFotoActual>=tamanoUrlImagenes-1){
                        indiceFotoActual=0;
                    }
                    else {
                        indiceFotoActual++;
                    }
                }
            });
        mAtrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tamanoUrlImagenes-1!=0) {
                    mostrarFoto(urlImagenes.get(indiceFotoActual));
                }
                if(indiceFotoActual<=0){
                    indiceFotoActual=tamanoUrlImagenes-1;
                }
                else {
                    indiceFotoActual--;
                }
            }
        });
*/
        /*
        mSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }
        });

         */

        mGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(true);
                if(verPerfilVendedor==0) {
                    Intent intent = new Intent(MiPublicacionDetalle.this, EditarPublicacion.class);
                    intent.putExtra("idClothes", idClothes);
                    startActivity(intent);
                }
                else{
                    usersDb.child(mAuth.getCurrentUser().getUid()).child("connections").child("publicacionesGuardadas").child(idClothes).setValue(true);
                    Toast.makeText(MiPublicacionDetalle.this, "Publicacion Guardada", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(MiPublicacionDetalle.this,MiPublicacionDetalle.class);
                    //startActivity(intent);
                }
            }
        });
        /*mRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MiPublicacionDetalle.this)
                        //.setTitle("")
                        .setMessage("¿Esta seguro de remover la publicación?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference estadoPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("clothes").child(idClothes).child("vendidaTemporal");
                                estadoPrenda.setValue("1");
                                Toast.makeText(MiPublicacionDetalle.this, "Publicacion marcada como vendida", Toast.LENGTH_SHORT).show();

                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });*/
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
                    mEstado.setText(dataSnapshot.child("EstadoPrenda").getValue().toString());
                    guardarUrlPhotos();
                    //SUBIR FOTOS.
                    if(dataSnapshot.hasChild("estaVendida")){
                        mGuardar.setVisibility(View.INVISIBLE);
                        //mRechazar.setVisibility(View.INVISIBLE);
                        mLinearLayoutValoracion.setVisibility(View.VISIBLE);

                        if(dataSnapshot.hasChild("valoracionTrato") ){
                            if(dataSnapshot.child("valoracionTrato") != null){
                                mTextViewTrato.setText(dataSnapshot.child("valoracionTrato").getValue().toString());
                            }
                            if(dataSnapshot.child("valoracionEstado") != null){
                                mTextViewEstado.setText(dataSnapshot.child("valoracionEstado").getValue().toString());
                            }
                            if(dataSnapshot.child("valoracionPuntualidad") != null){
                                mTextViewPuntualidad.setText(dataSnapshot.child("valoracionPuntualidad").getValue().toString());
                            }
                        }
                        else{
                            mTextViewTrato.setText("S/V");
                            mTextViewEstado.setText("S/V");
                            mTextViewPuntualidad.setText("S/V");
                        }
                    }
                    else if(dataSnapshot.hasChild("vendidaTemporal")){
                        if(dataSnapshot.child("vendidaTemporal").getValue().toString().equals("1")){
                            mGuardar.setVisibility(View.INVISIBLE);
                            //mRechazar.setVisibility(View.INVISIBLE);
                            mLinearLayoutValoracion.setVisibility(View.VISIBLE);

                            if(dataSnapshot.hasChild("valoracionTrato") ){
                                if(dataSnapshot.child("valoracionTrato") != null){
                                    mTextViewTrato.setText(dataSnapshot.child("valoracionTrato").getValue().toString());
                                }
                                if(dataSnapshot.child("valoracionEstado") != null){
                                    mTextViewEstado.setText(dataSnapshot.child("valoracionEstado").getValue().toString());
                                }
                                if(dataSnapshot.child("valoracionPuntualidad") != null){
                                    mTextViewPuntualidad.setText(dataSnapshot.child("valoracionPuntualidad").getValue().toString());
                                }
                            }
                            else{
                                mTextViewTrato.setText("S/V");
                                mTextViewEstado.setText("S/V");
                                mTextViewPuntualidad.setText("S/V");
                            }
                        }
                    }
                    else{
                        mLinearLayoutValoracion.setVisibility(View.INVISIBLE);
                        mGuardar.setVisibility(View.VISIBLE);
                        //mRechazar.setVisibility(View.VISIBLE);
                        if(verPerfilVendedor==1){
                            //mRechazar.setVisibility(View.INVISIBLE);
                        }
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

        photosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(idOwner).child("clothes").child(idClothes).child("clothesPhotos");

        photosDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    urlImagenes.add(dataSnapshot.getValue().toString());
                   //SlideModel aux = new SlideModel(dataSnapshot.getValue().toString(),ScaleTypes.FIT);
                    //imageList.add(aux);
                    //mSlider.setImageList(imageList,ScaleTypes.FIT);
                    adapter.notifyDataSetChanged();
                    if(indiceFotoActual==0) {

                        //   mostrarFoto(urlImagenes.get(0));
                        indiceFotoActual++;
                    }
                    tamanoUrlImagenes++;
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

    @Override
    public void onBackPressed()
    {

    }
    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        //Intent myIntent = new Intent(getApplicationContext(), MisPublicaciones.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}