package com.example.proyectotitulo;

public class publicacion {
    private String tituloPublicacion;
    private String profileImageUrl;
    private String valorPublicacion;
    private String idClothes;

    public publicacion(String tituloPublicacion, String profileImageUrl, String valorPublicacion, String idClothes) {
        this.tituloPublicacion = tituloPublicacion;
        this.profileImageUrl = profileImageUrl;
        this.valorPublicacion = valorPublicacion;
        this.idClothes = idClothes;
    }

    public publicacion(String tituloPublicacion, String profileImageUrl, String idClothes) {
            this.tituloPublicacion = tituloPublicacion;
            this.profileImageUrl = profileImageUrl;
            this.idClothes = idClothes;
    }

    public String getValorPublicacion() {
        return valorPublicacion;
    }

    public void setValorPublicacion(String valorPublicacion) {
        this.valorPublicacion = valorPublicacion;
    }

    public String getProfileImageUrl () {
            return profileImageUrl;
    }

    public void setProfileImageUrl (String profileImageUrl){
            this.profileImageUrl = profileImageUrl;
        }

    public String getIdClothes () {
                return idClothes;
            }

    public void setIdClothes (String idClothes){
                    this.idClothes = idClothes;
                }

    public String getTituloPublicacion () {
                    return tituloPublicacion;
                }

    public void setTituloPublicacion (String tituloPublicacion){
                    this.tituloPublicacion = tituloPublicacion;
                }
}



