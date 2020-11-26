package com.mrswapdrobe.swapdrobe;
/*
Esta clase se encarga de mostrar los datos de la publicacion en la vista activity_mis_favoritos_detalle.xml.
entrada: recibe el id de la prenda y del usuario que publico la prenda y obtiene los datos de esa prenda.
salida: permite ir al detalle del vendedor o iniciar un chat con este usuario.
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MisFavoritosDetalle extends AppCompatActivity {

    private TextView mTitulo,mPrecio,mDescripcion,mColor,mTalla,mtipoPrenda,mEstado,mNombreVendedor;
    private FirebaseAuth mAuth;
    private DatabaseReference clothesDb,photosDb,usersDb,chatsDb;
    private String idUser,idClothes;
    private String currentUId,vendedorUID,idOwner;
    private String currentOwnerUid;
    private Button mEditar, mRechazar, mVerPerfil;
    private ImageView mFotoActual;
    private int indiceFotoActual, tamanoUrlImagenes;
    private ArrayList<String> urlImagenes;
    private ImageSlider mSlider;
    private ArrayList<SlideModel> imageList;
    private boolean logica;
    private DatabaseReference usersDbDos,bloqueadosDb;
    private boolean bloqueado;
    ChildEventListener childListener;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_favoritos_detalle);
        //idOwner = getIntent().getExtras().getString("idUser");
        bloqueado = false;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();
        idClothes = getIntent().getExtras().getString("idClothes");
        if(getIntent().getExtras().getString("idOwner")!=null) {
            idOwner = getIntent().getExtras().getString("idOwner");
        }
        else{
            idOwner = "";
        }
        tamanoUrlImagenes = 0;

        //toolbar
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
        mEstado = (TextView) findViewById(R.id.estadoPrendaDetalleFavoritosDetalle);
        mVerPerfil = (Button) findViewById(R.id.verPerfilBtnFavoritosDetalle);
        mNombreVendedor = (TextView) findViewById(R.id.nombreVendedorFavoritosDetalle);

        indiceFotoActual=0;
        //mSlider = (ImageSlider) findViewById(R.id.fotoFavoritosDetalle);
        imageList = new ArrayList<SlideModel>();
        obtenerDatosPublicacion();
        obtenerNombreDueño();
        mAuth = FirebaseAuth.getInstance();
        logica = false;
        existeChat();
        estaBloqueado();


        viewPager = (ViewPager) findViewById(R.id.fotoFavoritosDetalle);
        adapter = new ViewPagerAdapter(MisFavoritosDetalle.this, urlImagenes);
        viewPager.setAdapter(adapter);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        /*mSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }
        });


         */
        mEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(logica==false && bloqueado==false){
                    String id = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                    DatabaseReference currentVendedor = FirebaseDatabase.getInstance().getReference().child("chat").child(id).child("idUserVendedor"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
                    DatabaseReference currentComprador = FirebaseDatabase.getInstance().getReference().child("chat").child(id).child("idUserComprador"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
                    DatabaseReference currentPrenda = FirebaseDatabase.getInstance().getReference().child("chat").child(id).child("idPrenda"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
                    DatabaseReference messages = FirebaseDatabase.getInstance().getReference().child("chat").child(id).child("messages");
                    //FirebaseDatabase.getInstance().getReference().child("chat").child(id).child("marcadaComoVendida").setValue("0");
                    //FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("connections").child("publicacionesGuardadas").child(idClothes).setValue("Guardado");


                    currentVendedor.setValue(vendedorUID);
                    currentComprador.setValue(currentUId);
                    currentPrenda.setValue(idClothes);
                    messages.setValue(true);
                    logica=true;
                    bloqueado = true;
                    Toast.makeText(MisFavoritosDetalle.this, "Chat Creado.", Toast.LENGTH_SHORT).show();
                }
                else if(bloqueado==true && logica == false){
                    Toast.makeText(MisFavoritosDetalle.this, "No se puede crear chat: Usuario Bloqueado.", Toast.LENGTH_SHORT).show();
                }
                else if(bloqueado==false && logica == true){
                    Toast.makeText(MisFavoritosDetalle.this, "No se puede crear chat: Chat ya existente.", Toast.LENGTH_SHORT).show();
                }
                //intent que te mande a chat
            }
        });
        mRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MisFavoritosDetalle.this,VerPerfilDeVendedor.class);
                intent.putExtra("idClothes",idClothes);
                intent.putExtra("idOwner",idOwner);
                startActivity(intent);
            }
        });

        //hacer boton derecha visible.
        //el onclick del boton y que se cambie la foto
        //      }

    }

    private void estaBloqueado() {
        bloqueado = false;

        usersDbDos = FirebaseDatabase.getInstance().getReference().child("Users");

        usersDbDos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId) && dataSnapshot.hasChild("Bloqueados")) {
                    bloqueadosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId).child("Bloqueados");
                    bloqueadosDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idOwner)){
                                bloqueado=true;
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

    private void existeChat() {
        logica = false;
        chatsDb = FirebaseDatabase.getInstance().getReference().child("chat");
        childListener = chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("idUserComprador") && dataSnapshot.hasChild("idPrenda")) {
                    if (dataSnapshot.child("idUserComprador").getValue().toString().equals(currentUId) && dataSnapshot.child("idPrenda").getValue().toString().equals(idClothes)) {
                        logica = true;
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

    private void obtenerDatosPublicacion() {
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && !dataSnapshot.getKey().equals(currentUId)) {
                    String key = dataSnapshot.getKey();
                    currentOwnerUid = key;
                    final String vendedorId = key;
                    clothesDb = usersDb.child(key).child("clothes");
                    Log.d("MFD", "primero");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dataSnapshot.exists() && dataSnapshot.getKey().equals(idClothes)) {
                                vendedorUID = vendedorId;
                                Log.d("MFD", dataSnapshot.child("tituloPublicacion").getValue().toString() + dataSnapshot.child("ValorPrenda").getValue().toString() + dataSnapshot.child("DescripcionPrenda").getValue().toString() + dataSnapshot.child("TipoPrenda").getValue().toString() + dataSnapshot.child("ColorPrenda").getValue().toString() + dataSnapshot.child("TallaPrenda").getValue().toString());
                                mTitulo.setText(dataSnapshot.child("tituloPublicacion").getValue().toString());
                                mPrecio.setText("$" + dataSnapshot.child("ValorPrenda").getValue().toString());
                                mDescripcion.setText(dataSnapshot.child("DescripcionPrenda").getValue().toString());
                                mtipoPrenda.setText(dataSnapshot.child("TipoPrenda").getValue().toString());
                                mColor.setText(dataSnapshot.child("ColorPrenda").getValue().toString());
                                mTalla.setText(dataSnapshot.child("TallaPrenda").getValue().toString());
                                mEstado.setText(dataSnapshot.child("EstadoPrenda").getValue().toString());
                                guardarUrlPhotos(vendedorId);
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
                //Picasso.get().setLoggingEnabled(true);
                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mFotoActual);
                //Picasso.get().load(R.mipmap.ic_launcher).fit().centerCrop().into(mFotoActual);
                break;
            default:
                //Picasso.get().setLoggingEnabled(true);
                Glide.with(getApplication()).load(urlFotoActual).into(mFotoActual);
                //Picasso.get().load(urlFotoActual).fit().centerCrop().into(mFotoActual);
                break;

        }
        //Picasso.get().setLoggingEnabled(true);
        Glide.with(getApplication()).load(urlFotoActual).into(mFotoActual);
        //Picasso.get().load(urlFotoActual).fit().centerCrop().into(mFotoActual);
    }


    private void guardarUrlPhotos(String vendedorId) {
        photosDb = FirebaseDatabase.getInstance().getReference().child("Users").child(vendedorId).child("clothes").child(idClothes).child("clothesPhotos");

        photosDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    urlImagenes.add(dataSnapshot.getValue().toString());
                   // SlideModel aux = new SlideModel(dataSnapshot.getValue().toString(), ScaleTypes.FIT);
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

    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        //Intent myIntent = new Intent(getApplicationContext(), MisFavoritos.class);
        //startActivityForResult(myIntent, 0);
        chatsDb.removeEventListener(childListener);
        finish();
        return true;
    }

}