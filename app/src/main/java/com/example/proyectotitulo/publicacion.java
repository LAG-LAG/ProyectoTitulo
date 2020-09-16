package com.example.proyectotitulo;

public class publicacion {
    private String UserId;
    private String profileImageUrl;

    public publicacion(String userId, String profileImageUrl) {
        UserId = userId;
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
