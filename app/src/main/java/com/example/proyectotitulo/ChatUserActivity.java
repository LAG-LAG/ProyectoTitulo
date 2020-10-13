package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatUserActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private DatabaseReference chatsDb, userDb;
    private EditText mSendEditText;
    private int esComprador,chatBloqueado;
    private Button mSendButton;
    private String currentUserID, matchId, chatId;
    private String idVendedor, idComprador, idUserQueBloqueo;
    DatabaseReference mDatabaseUser, mDatabaseChat,mDatabaseMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        idUserQueBloqueo="1";
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chat");
        }
        //valorarOComprar = (MenuItem) findViewById(R.id.valorarPublicacionBtn);

        //valorarOComprar.setTitle("gungaginga");
        chatsDb = FirebaseDatabase.getInstance().getReference().child("chat");
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");

        chatId = getIntent().getExtras().getString("chatId");

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("probanding","1");
        //mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        Log.d("probanding","CHAT ID "+chatId);
        mDatabaseChat =  FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("messages");
        Log.d("probanding","2");
        //mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatId).child("messages");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);
        mDatabaseMessages = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);

        Log.d("probanding","3");
        //getChatId();
        obtenerIdVendedorEIdComprador();
        getChatMessages();
        Log.d("probanding","4");


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatUserActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new UserChatAdapter(getDataSetChat(), ChatUserActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void obtenerIdVendedorEIdComprador() {
        chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)){
                    idComprador = dataSnapshot.child("idUserComprador").getValue().toString();
                    idVendedor = dataSnapshot.child("idUserVendedor").getValue().toString();
                    if(dataSnapshot.hasChild("chatBloqueado")){
                        chatBloqueado=1;
                        idUserQueBloqueo = dataSnapshot.child("chatBloqueado").getValue().toString();
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


    private void sendMessage() {
        if(chatBloqueado!=1) {
            String sendMessageText = mSendEditText.getText().toString();

            if (!sendMessageText.isEmpty()) {
                DatabaseReference newMessageDb = mDatabaseMessages.child("messages").push();

                Map newMessage = new HashMap();
                newMessage.put("createdByUser", currentUserID);
                newMessage.put("text", sendMessageText);

                newMessageDb.setValue(newMessage);
            }
            mSendEditText.setText(null);
        }
        else{
            Toast.makeText(this, "No se puede enviar mensaje. Bloqueado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages() {
        Log.d("probanding","3.1");
        mDatabaseChat.addChildEventListener(new ChildEventListener() {//RECORREMOS LOS CHATS
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    Log.d("probanding","3.3");

                    String message = null;
                    String createdByUser = null;
                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }


    //Crea el menu en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);

        final MenuItem valorarOComprar = menu.findItem(R.id.valorarPublicacionBtn);
        final MenuItem Bloquear = menu.findItem(R.id.bloquearChatBtn);


        Log.d("pruebachat","1");
        chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("pruebachat","2");
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)){
                    if(!dataSnapshot.hasChild("chatBloqueado")){
                        Bloquear.setTitle("Bloquear");
                    }
                    else{
                        Bloquear.setTitle("Desbloquear");
                    }
                    Log.d("pruebachat","3");
                    if(dataSnapshot.child("idUserComprador").getValue().toString().equals(currentUserID)){ //significa que el usuario es comprador.
                        Log.d("pruebachat","4 es comprador");
                        esComprador=1;
                        valorarOComprar.setTitle("Valorar");
                        if(!dataSnapshot.hasChild("marcadaComoVendida")) {
                            DatabaseReference estadoPrenda = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("marcadaComoVendida");
                            estadoPrenda.setValue("0");
                        }
                    }
                    else{ //significa que el usuario es vendedor.
                        Log.d("pruebachat","5 es vendedor");
                        esComprador=0;
                        if(dataSnapshot.hasChild("marcadaComoVendida") && !dataSnapshot.hasChild("estadoFinalizado")) {
                            if(dataSnapshot.child("marcadaComoVendida").getValue().equals("1")){
                                valorarOComprar.setTitle("Marcar Como No Vendida");
                                Log.d("pruebachat","6");

                            }
                            else {
                                valorarOComprar.setTitle("Marcar Como No Vendida");
                                Log.d("pruebachat","7");
                            }
                        }
                        else if(dataSnapshot.hasChild("estadoFinalizado")){
                            valorarOComprar.setTitle("Vendida.");
                        }
                        else{
                            Log.d("pruebachat","8");
                            valorarOComprar.setTitle("Marcar Como No Vendida");
                            DatabaseReference estadoPrenda = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("marcadaComoVendida");
                            estadoPrenda.setValue("0");
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



        return true;
    }

    //Controla los botones del menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.valorarPublicacionBtn:
                if(esComprador==1){
                    chatsDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)) {
                                if (dataSnapshot.hasChild("marcadaComoVendida")) {
                                    if(dataSnapshot.child("marcadaComoVendida").getValue().toString().equals("1") && !dataSnapshot.hasChild("estadoFinalizado")){
                                        Intent intentValorar = new Intent(ChatUserActivity.this, Valorar.class);
                                        intentValorar.putExtra("chatId",chatId);
                                        startActivity(intentValorar);
                                        finish();                                    }
                                    else{
                                        if(dataSnapshot.hasChild("estadoFinalizado")){
                                            Toast.makeText(ChatUserActivity.this, "Publicación ya fue valorada.", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(ChatUserActivity.this, "El vendedor no ha marcado como vendida.", Toast.LENGTH_SHORT).show();
                                        }
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
                else{
                    if(item.getTitle().equals("Marcar Como No Vendida")){
                        item.setTitle("Marcar Como Vendida");
                        DatabaseReference estadoPrenda = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("marcadaComoVendida");
                        estadoPrenda.setValue("0");
                    }
                    else if (item.getTitle().equals("Marcar Como Vendida")){
                        item.setTitle("Marcar Como No Vendida");
                        DatabaseReference estadoPrenda = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("marcadaComoVendida");
                        estadoPrenda.setValue("1");
                    }
                    else{
                        Toast.makeText(ChatUserActivity.this, "Publicación ya fue vendida.", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.bloquearChatBtn:
                if(item.getTitle().equals("Bloquear")) {
                    if(chatBloqueado!=1){
                        Bloquear();
                        item.setTitle("Desbloquear");
                        //chatBloqueado=1;
                    }
                    else{
                        if(currentUserID.equals(idUserQueBloqueo)) {
                            Bloquear();
                            item.setTitle("Desbloquear");
                           // chatBloqueado=1;
                        }
                    }


                }
                else{
                    if(currentUserID.equals(idUserQueBloqueo)) {
                        Desbloquear();
                        item.setTitle("Bloquear");
                        //chatBloqueado=1;
                    }
                    else{
                        Toast.makeText(this, "No puede desbloquear, usted fue bloqueado.", Toast.LENGTH_SHORT).show();
                    }
                }



                /*Intent intentBloquear = new Intent(ChatUserActivity.this, PaginaPrincipal.class);
                startActivity(intentBloquear);
                finish();*/
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Desbloquear() {
        Toast.makeText(this, "Desbloquear", Toast.LENGTH_SHORT).show();
        chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)) {
                    if(esComprador == 1) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Bloqueados").child(dataSnapshot.child("idUserVendedor").getValue().toString()).removeValue();
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Bloqueados").child(dataSnapshot.child("idUserComprador").getValue().toString());
                    }
                }
                if(dataSnapshot.exists() && dataSnapshot.child("idUserComprador").getValue().toString().equals(idComprador) && dataSnapshot.child("idUserVendedor").getValue().toString().equals(idVendedor)){
                    FirebaseDatabase.getInstance().getReference().child("chat").child(dataSnapshot.getKey()).child("chatBloqueado").removeValue();
                    //bloquearChat.setValue("1");
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

    private void Bloquear() {
        Toast.makeText(this, "Bloquear", Toast.LENGTH_SHORT).show();
        chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)) {
                    if(esComprador == 1) {
                        DatabaseReference guardarBloqueados = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Bloqueados").child(dataSnapshot.child("idUserVendedor").getValue().toString());
                        guardarBloqueados.setValue("1");
                    }
                    else{
                        DatabaseReference guardarBloqueados = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Bloqueados").child(dataSnapshot.child("idUserComprador").getValue().toString());
                        guardarBloqueados.setValue("1");
                    }
                }
                if(dataSnapshot.exists() && dataSnapshot.child("idUserComprador").getValue().toString().equals(idComprador) && dataSnapshot.child("idUserVendedor").getValue().toString().equals(idVendedor)){
                    DatabaseReference bloquearChat = FirebaseDatabase.getInstance().getReference().child("chat").child(dataSnapshot.getKey()).child("chatBloqueado");
                    bloquearChat.setValue(currentUserID);
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