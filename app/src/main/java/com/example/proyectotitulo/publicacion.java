package com.example.proyectotitulo;

public class publicacion {
    private String UserId;

    public publicacion(String userId) {
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
