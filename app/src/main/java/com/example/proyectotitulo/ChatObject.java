
package com.example.proyectotitulo;

/* Esta clase corresponde a cada objeto de la vista misChats, cada uno de estos elementos se añade al arrayAdapterr de la clase "chats".


 */
import com.google.firebase.database.DatabaseReference;

public class ChatObject {
    private String message,UserId,nombre;
    private long hora;
    private Boolean currentUser;
    private DatabaseReference usersDb;
    public ChatObject(String message, Boolean currentUser){
        this.message = message;
        this.currentUser = currentUser;
    }
    public ChatObject(String message, Boolean currentUser,String UserId, long hora, String nombre){
        this.message = message;
        this.currentUser = currentUser;
        this.UserId = UserId;
        this.hora = hora;
        this.nombre = nombre;
    }


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }

    public long getHora() {
        return hora;
    }


}
