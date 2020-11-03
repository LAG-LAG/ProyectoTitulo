package com.example.proyectotitulo;
/*
esta clase corresponde a la conversacion entre dos usuarios. su vista correpsonde a activity_user_chat.xml
entrada: recibe los uid de ambos usuarios, ve cual es el usuario correspondiente al dispositivo actual y lo asigna en una hacia el lado derecho. luego carga los mensajes. ve si esta bloqueado y asigna los permisos correspondientes.
salida: envia los chats (revisando si no esta bloqueado). bloquea al usuario, dejandolo como bloqueado en la base de datos. si el usuario es vendedor este puede marcar la publicacion como vendida y si el usuario es comprador y la publicaicon
esta marcada como vendida este puede valorar esta publicacion, pasando a la clase Valorar.
 */

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatUserActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private ChildEventListener childEvent;
    private DatabaseReference chatsDb, userDb;
    private EditText mSendEditText;
    private int esComprador,chatBloqueado,chatBloqueadoMsj;
    private Button mSendButton;
    private String currentUserID, matchId, chatId;
    private String idVendedor, idComprador, idUserQueBloqueo;
    DatabaseReference mDatabaseUser, mDatabaseChat,mDatabaseMessages,usersDb;
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
        //mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseChat =  FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("messages");
        //mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatId).child("messages");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);
        mDatabaseMessages = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);

        //getChatId();
        obtenerIdVendedorEIdComprador();
        getChatMessages();


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
                        chatBloqueadoMsj = 1;
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
        if(chatBloqueadoMsj!=1) {
            String sendMessageText = mSendEditText.getText().toString();

            if (!sendMessageText.isEmpty()) {
                DatabaseReference newMessageDb = mDatabaseMessages.child("messages").push();

                Map newMessage = new HashMap();
                newMessage.put("createdByUser", currentUserID);
                newMessage.put("text", sendMessageText);
                newMessage.put("hora", new Date().getTime());
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
        mDatabaseChat.addChildEventListener(new ChildEventListener() {//RECORREMOS LOS CHATS
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){

                    String message = null;
                    String createdByUser = null;
                    long hora = 0;
                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(dataSnapshot.child("hora").getValue()!=null){
                        hora = Long.parseLong(dataSnapshot.child("hora").getValue().toString());

                    }
                    if(message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
                        final String finalMessage = message;
                        final Boolean finalCurrentUserBoolean = currentUserBoolean;
                        final String finalCreatedByUser = createdByUser;
                        final long finalHora = hora;
                        childEvent = usersDb.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(finalCreatedByUser)){

                                    if(dataSnapshot.hasChild("nameUser")) {
                                        final String nombre = dataSnapshot.child("nameUser").getValue().toString();
                                        misChatObject newMessage = new misChatObject(finalMessage, finalCurrentUserBoolean, finalCreatedByUser, finalHora,nombre);
                                        resultsChat.add(newMessage);
                                        mChatAdapter.notifyDataSetChanged();
                                        usersDb.removeEventListener(childEvent);

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


    private ArrayList<misChatObject> resultsChat = new ArrayList<misChatObject>();
    private List<misChatObject> getDataSetChat() {
        return resultsChat;
    }


    //Crea el menu en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);

        final MenuItem valorarOComprar = menu.findItem(R.id.valorarPublicacionBtn);
        final MenuItem Bloquear = menu.findItem(R.id.bloquearChatBtn);


        chatsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)){
                    if(!dataSnapshot.hasChild("chatBloqueado")){
                        Bloquear.setTitle("Bloquear");
                    }
                    else{
                        Bloquear.setTitle("Desbloquear");
                    }
                    if(dataSnapshot.child("idUserComprador").getValue().toString().equals(currentUserID)){ //significa que el usuario es comprador.
                        esComprador=1;
                        valorarOComprar.setTitle("Valorar");
                        if(!dataSnapshot.hasChild("marcadaComoVendida")) {
                            DatabaseReference estadoPrenda = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("marcadaComoVendida");
                            estadoPrenda.setValue("0");
                        }
                        else if(dataSnapshot.hasChild("estadoFinalizado")){
                            valorarOComprar.setTitle("Vendida.");
                        }
                    }
                    else{ //significa que el usuario es vendedor.
                        esComprador=0;
                        if(dataSnapshot.hasChild("marcadaComoVendida") && !dataSnapshot.hasChild("estadoFinalizado")) {
                            if(dataSnapshot.child("marcadaComoVendida").getValue().equals("1")){
                                valorarOComprar.setTitle("Marcar Como No Vendida");

                            }
                            else {
                                valorarOComprar.setTitle("Marcar Como Vendida");
                            }
                        }
                        else if(dataSnapshot.hasChild("estadoFinalizado")){
                            valorarOComprar.setTitle("Vendida.");
                        }
                        else{
                            valorarOComprar.setTitle("Marcar Como Vendida");
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
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
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
                                        finish();
                                    }
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
                        chatBloqueadoMsj = 1;
                        chatBloqueado=1;
                    }
                    else{
                        if(currentUserID.equals(idUserQueBloqueo)) {
                            Bloquear();
                            item.setTitle("Desbloquear");
                            chatBloqueadoMsj = 1;chatBloqueado=1;
                        }
                    }


                }
                else{


                    chatsDb.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.exists() && dataSnapshot.getKey().equals(chatId)){
                                idComprador = dataSnapshot.child("idUserComprador").getValue().toString();
                                idVendedor = dataSnapshot.child("idUserVendedor").getValue().toString();
                                if(dataSnapshot.hasChild("chatBloqueado")){
                                    chatBloqueado=1;
                                    chatBloqueadoMsj=1;
                                    final String idUserQueBloquea = dataSnapshot.child("chatBloqueado").getValue().toString();
                                    if(currentUserID.equals(idUserQueBloquea)) {
                                        Desbloquear();
                                        item.setTitle("Bloquear");
                                        chatBloqueadoMsj=0;
                                        chatBloqueado=0;
                                    }
                                    else{
                                        Toast.makeText(ChatUserActivity.this, "No puede desbloquear, usted fue bloqueado.", Toast.LENGTH_SHORT).show();
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
                        FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("Bloqueados").child(currentUserID).removeValue();
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Bloqueados").child(idComprador).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(idComprador).child("Bloqueados").child(currentUserID).removeValue();

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
                        DatabaseReference guardarBloqueadosEnOtroUser = FirebaseDatabase.getInstance().getReference().child("Users").child(idVendedor).child("Bloqueados").child(currentUserID);
                        guardarBloqueadosEnOtroUser.setValue("1");
                    }
                    else{
                        DatabaseReference guardarBloqueados = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Bloqueados").child(idComprador);
                        guardarBloqueados.setValue("1");
                        DatabaseReference guardarBloqueadosEnOtroUserdos = FirebaseDatabase.getInstance().getReference().child("Users").child(idComprador).child("Bloqueados").child(currentUserID);
                        guardarBloqueadosEnOtroUserdos.setValue("1");
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