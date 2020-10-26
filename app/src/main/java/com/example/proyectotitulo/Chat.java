package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    private ListView lvItems;
    private chatAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb,usersDbDos,chatsDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid, currentOwnerUid,idPrendaChat;
    private ArrayList<chats> listItems = new ArrayList<>();
    private ArrayList<String> idPrendas = new ArrayList<>();
    ChildEventListener childListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis Chats");
        }



        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        usersDbDos = FirebaseDatabase.getInstance().getReference().child("Users");
        chatsDb = FirebaseDatabase.getInstance().getReference().child("chat");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();

        obtenerPublicaciones();

        lvItems = (ListView) findViewById(R.id.lvMisChats);
        adapter = new chatAdapter(this, listItems);
        lvItems.setAdapter(adapter);

        if(listItems.isEmpty()){
            Toast.makeText(this, "no hay chats activos", Toast.LENGTH_SHORT).show();
        }

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ChatUserActivity.class);
                Bundle b = new Bundle();
                chats auxChat = (chats)lvItems.getAdapter().getItem(position);
                b.putString("chatId", auxChat.getIdClothes());
                chatsDb.removeEventListener(childListener);
                intent.putExtras(b);
                view.getContext().startActivity(intent);
                /*
                publicacion AuxPublicacion = (publicacion)lvItems.getAdapter().getItem(position);
                String idClothes = AuxPublicacion.getIdClothes();
                Intent intentDetalle = new Intent(Chat.this, MiPublicacionDetalle.class);
                intentDetalle.putExtra("idClothes",idClothes);
                intentDetalle.putExtra("idUser",mAuth.getCurrentUser().getUid());
                startActivity(intentDetalle);
                finish();*/
            }
        });


    }

    private void obtenerPublicaciones(){
        childListener = chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("idPrenda") && dataSnapshot.hasChild("idUserComprador") && dataSnapshot.hasChild("idUserVendedor")) {
                    if (dataSnapshot.child("idUserVendedor").getValue().toString().equals(currentUId) || dataSnapshot.child("idUserComprador").getValue().toString().equals(currentUId)) {
                    Log.d("probando", "Adentro" + dataSnapshot.getKey());

                    //String idNombreChatAux = "";
                    String vendedor;
                    if(!dataSnapshot.child("idUserComprador").getValue().toString().equals(currentUId)){
                        vendedor = "idUserComprador";
                    }
                    else{
                        vendedor = "idUserVendedor";
                    }

                    final String idNombreChat = dataSnapshot.child(vendedor).getValue().toString();
                    Log.d("gingigingi","idnombre "+idNombreChat);
                    idPrendaChat = dataSnapshot.child("idPrenda").getValue().toString();
                    final String idPrendaChatNuevo = idPrendaChat;
                    //idPrendas.add(idPrendaChat);
                    Log.d("probando", "id prenda chat " + idPrendaChat);
                    final String chatCurrentId = dataSnapshot.getKey();
                    usersDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dataSnapshot.exists() && dataSnapshot.hasChild("clothes")) {
                                String key = dataSnapshot.getKey();
                                currentOwnerUid = key;
                                clothesDb = usersDb.child(key).child("clothes");

                                clothesDb.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        //Log.d("probando","afuera producto "+dataSnapshot.getKey());
                                        //for(int i = 0 ; i< idPrendas.size();i++) {

                                        if (dataSnapshot.getKey().equals(idPrendaChatNuevo)) {
                                            Log.d("probando", "Adentro producto" + dataSnapshot.getKey());
                                            currentUId = mAuth.getCurrentUser().getUid();

                                            clothesCurrentUid = dataSnapshot.getKey();
                                            //String idVendedorOComprador = "por hacer";//aca hay que ver si el loco es vendedor o comprador en el chat para mostrar el nombre.
                                            String fotoPublicacion;
                                            if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                                fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                            } else {
                                                fotoPublicacion = "default";
                                            }
                                            final String fotoPublicacionFinal = fotoPublicacion;
                                            final String tituloPublicacion = dataSnapshot.child("tituloPublicacion").getValue().toString();
                                            Log.d("mueremuere","aqui");
                                            usersDbDos.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                    Log.d("mueremuere","aqui2");
                                                    if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idNombreChat)){
                                                        Log.d("mueremuere","aqui3");
                                                        final String nombreVendedor = "Usuario: "+dataSnapshot.child("nameUser").getValue().toString();
                                                        Log.d("mueremuere","aqui4");
                                                        //Log.d("mueremuere", "titulo: " + dataSnapshot.child("tituloPublicacion").getValue().toString() + " fotoPublicacion: " + fotoPublicacionFinal + "current chat: " + chatCurrentId);
                                                        //Log.d("mueremuere","nombre vendedor: "+nombreVendedor);
                                                        Log.d("mueremuere","aqui5");
                                                            chats item = new chats(tituloPublicacion, fotoPublicacionFinal, chatCurrentId, nombreVendedor);
                                                            listItems.add(item);
                                                            adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.

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
/*
                                            Log.d("probando", "titulo: " + dataSnapshot.child("tituloPublicacion").getValue().toString() + " fotoPublicacion: " + fotoPublicacion + "current chat: " + chatCurrentId);
                                            Log.d("gingigingi","nombre vendedor: "+nombreVendedor);
                                            chats item = new chats(dataSnapshot.child("tituloPublicacion").getValue().toString(), fotoPublicacion, chatCurrentId);
                                            listItems.add(item);
                                            adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.*/
                                        }
                                        //}
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


    //Crea el menu en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /*private ArrayList<publicacion> GetArrayItems(){
        ArrayList<publicacion> listItems = new ArrayList<>();
        listItems.add(new publicacion("wea"));
        listItems.add(new publicacion("wea"));
        listItems.add(new publicacion("wea"));

        return listItems;
    }*/

    //toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        chatsDb.removeEventListener(childListener);
        switch (item.getItemId()){
            case R.id.accountBtn:
                Intent intentAccount = new Intent(Chat.this, VerMiCuenta.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentChat = new Intent(Chat.this, PaginaPrincipal.class);
                startActivity(intentChat);

                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(Chat.this, AddPublicaciones.class);
                startActivity(intentAdd);
                finish();
                break;

            case R.id.cerrarSesionBtn:
                mAuth.signOut(); //desconecta
                //las lineas de abajo mandan de la ventana actual(mainactiviy) a la de chooseloginregistration que es la de antes de estar loguado.
                Intent intent = new Intent(Chat.this,Login.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}