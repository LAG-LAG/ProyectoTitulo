package com.example.proyectotitulo;

public class publicacion {
    private String tituloPublicacion;
    private String profileImageUrl;
    private String valorPublicacion;

    public publicacion(String tituloPublicacion, String profileImageUrl) {
        this.tituloPublicacion = tituloPublicacion;
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getTituloPublicacion() {
        return tituloPublicacion;
    }

    public void setTituloPublicacion(String tituloPublicacion) {
        this.tituloPublicacion = tituloPublicacion;
    }
}
