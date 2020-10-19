package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class VerPerfilDeVendedor extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private DatabaseReference usersDb;
    private DatabaseReference clothesDb;
    private misPublicacionAdapter adapter;
    private ImageView mProfileImage;
    private TextView mTextViewNombre;
    private TextView mTextViewComuna;
    private TextView mTextViewRegion;
    private TextView mTextViewPuntacion;
    private String profileImageUrl, nombreUsuario, regionAnterior, comunaAnterior;
    private String idOwner,idClothes;
    private int existeFotoPerfil;
    private ListView lvItems;
    private ArrayList<publicacion> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil_de_vendedor);
        idOwner = getIntent().getExtras().getString("idOwner");
        idClothes = getIntent().getExtras().getString("idClothes");

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Perfil de vendedor");
        }

        mProfileImage = (ImageView) findViewById(R.id.ImagenPerfilUrlVendedor);
        mTextViewNombre = (TextView) findViewById(R.id.TextViewNombreVendedor);
        mTextViewComuna = (TextView) findViewById(R.id.TextViewComunaVendedor);
        mTextViewRegion = (TextView) findViewById(R.id.TextViewRegionVendedor);
        mTextViewPuntacion = (TextView) findViewById(R.id.TextViewPuntacionVendedor);
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(idOwner);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        getUserInfo();

        obtenerPublicaciones();

        lvItems = (ListView) findViewById(R.id.lvPublicacionesVendidasVendedor);
        adapter = new misPublicacionAdapter(this, listItems,"PERFILVENDEDOR");
        lvItems.setAdapter(adapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publicacion AuxPublicacion = (publicacion)lvItems.getAdapter().getItem(position);
                String idClothes = AuxPublicacion.getIdClothes();
                Intent intentDetalle = new Intent(VerPerfilDeVendedor.this, MiPublicacionDetalle.class);
                intentDetalle.putExtra("idClothes",idClothes);
                intentDetalle.putExtra("idUser",idOwner); //mAuth.getCurrentUser().getUid());
                intentDetalle.putExtra("verPerfilVendedor","1");
                startActivity(intentDetalle);
                //finish();
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
                        mTextViewNombre.setText(nombreUsuario);
                    }

                    if (map.get("region") != null) {
                        regionAnterior = map.get("region").toString();
                        comunaAnterior = map.get("comuna").toString();
                        mTextViewRegion.setText(regionAnterior);
                        mTextViewComuna.setText(comunaAnterior);
                    }

                    if(map.get("puntuacionGeneral") != null){
                        if(map.get("puntuacionGeneral").toString().equals("-1")){
                            mTextViewPuntacion.setText("S/V");
                        }
                        else{
                            mTextViewPuntacion.setText(map.get("puntuacionGeneral").toString());
                        }
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obtenerPublicaciones(){
        Log.d("vervendedor", "entra 1");
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("clothes") && dataSnapshot.getKey().equals(idOwner)){
                    final String key = dataSnapshot.getKey();
                    final String currentOwnerUid = key;
                    clothesDb = usersDb.child(key).child("clothes");
                    clothesDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists()) {
                                final String clothesCurrentUid = dataSnapshot.getKey();
                                final String fotoPublicacion;
                                if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                    fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                } else {
                                    fotoPublicacion = "default";
                                }
                                if (dataSnapshot.hasChild("ValorPrenda") && dataSnapshot.hasChild("tituloPublicacion") && dataSnapshot.hasChild("DescripcionPrenda") && dataSnapshot.hasChild("clothesPhotos") && !dataSnapshot.hasChild("estaVendida")) {
                                    final publicacion item = new publicacion(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, "$" + dataSnapshot.child("ValorPrenda").getValue().toString(), clothesCurrentUid);
                                    listItems.add(item);
                                    adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
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

    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }


}