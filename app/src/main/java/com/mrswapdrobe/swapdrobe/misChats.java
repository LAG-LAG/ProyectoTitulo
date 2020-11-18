/* Esta clase utiliza la vista activity_chat.xml. se encarga de obtener la lista de los chats activos del usuario.
Entrada: obtiene todos los chats del usuario que esta logueado mediante la funcion obtenerChats.
Salida: al presionar en uno de los chats, este envia el uid del chat seleccionado a la vista del chat (ChatUserActivity).
*/

package com.mrswapdrobe.swapdrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class misChats extends AppCompatActivity {
    private ListView lvItems;
    private chatAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb,usersDbDos,chatsDb;
    private DatabaseReference clothesDb;
    private String currentUId, clothesCurrentUid, currentOwnerUid,idPrendaChat;
    private TextView textView1,textView2;
    private ArrayList<Chat> listItems = new ArrayList<>();
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

        obtenerChats();

        lvItems = (ListView) findViewById(R.id.lvMisChats);
        adapter = new chatAdapter(this, listItems);
        lvItems.setAdapter(adapter);

        textView1 = (TextView) findViewById(R.id.textView1Misfavoritos);
        textView2 = (TextView) findViewById(R.id.textView2Misfavoritos);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ChatUserActivity.class);
                Bundle b = new Bundle();
                Chat auxChat = (Chat)lvItems.getAdapter().getItem(position);
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

    private void obtenerChats(){
        childListener = chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("idPrenda") && dataSnapshot.hasChild("idUserComprador") && dataSnapshot.hasChild("idUserVendedor")) {
                    if (dataSnapshot.child("idUserVendedor").getValue().toString().equals(currentUId) || dataSnapshot.child("idUserComprador").getValue().toString().equals(currentUId)) {

                    //String idNombreChatAux = "";
                    String vendedor;
                    if(!dataSnapshot.child("idUserComprador").getValue().toString().equals(currentUId)){
                        vendedor = "idUserComprador";
                    }
                    else{
                        vendedor = "idUserVendedor";
                    }

                    final String idNombreChat = dataSnapshot.child(vendedor).getValue().toString();
                    idPrendaChat = dataSnapshot.child("idPrenda").getValue().toString();
                    final String idPrendaChatNuevo = idPrendaChat;
                    //idPrendas.add(idPrendaChat);
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

                                        if (dataSnapshot.getKey().equals(idPrendaChatNuevo)) {
                                            currentUId = mAuth.getCurrentUser().getUid();

                                            clothesCurrentUid = dataSnapshot.getKey();
                                            String fotoPublicacion;
                                            if (dataSnapshot.child("clothesPhotos").hasChild("photoId1")) {
                                                fotoPublicacion = dataSnapshot.child("clothesPhotos").child("photoId1").getValue().toString();
                                            } else {
                                                fotoPublicacion = "default";
                                            }
                                            final String fotoPublicacionFinal = fotoPublicacion;
                                            final String tituloPublicacion = dataSnapshot.child("tituloPublicacion").getValue().toString();
                                            usersDbDos.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                    if(dataSnapshot.exists() && dataSnapshot.getKey().equals(idNombreChat)){
                                                        final String nombreVendedor = "Usuario: "+dataSnapshot.child("nameUser").getValue().toString();
                                                            Chat item = new Chat(tituloPublicacion, fotoPublicacionFinal, chatCurrentId, nombreVendedor);
                                                            listItems.add(item);
                                                            adapter.notifyDataSetChanged(); //esto se usa cad vez que se añade o se quita un elemetno del arraylist de los items.
                                                        TieneItems();
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

    private void TieneItems()
    {
        if(listItems.isEmpty()){
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
        }
        else{
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }
    }

    //Crea el menu en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {

    }
    //toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        chatsDb.removeEventListener(childListener);
        switch (item.getItemId()){
            case R.id.accountBtn:
                Intent intentAccount = new Intent(misChats.this, VerMiCuenta.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentChat = new Intent(misChats.this, PaginaPrincipal.class);
                startActivity(intentChat);

                finish();
                break;

            case R.id.addBtn:
                Intent intentAdd = new Intent(misChats.this, AddPublicaciones.class);
                startActivity(intentAdd);
                finish();
                break;

            case R.id.cerrarSesionBtn:
                mAuth.signOut(); //desconecta
                //las lineas de abajo mandan de la ventana actual(mainactiviy) a la de chooseloginregistration que es la de antes de estar loguado.
                Intent intent = new Intent(misChats.this,Login.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}