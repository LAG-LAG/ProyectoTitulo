package com.mrswapdrobe.swapdrobe;

public class publicacion {
    private String idOwner;
    private String tituloPublicacion;
    private String profileImageUrl;
    private String valorPublicacion;
    private String idClothes;
    private String comentario;
    private float valoracion;

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

    public publicacion(String tituloPublicacion, String profileImageUrl, String idClothes, String keydos, String s) {
            this.tituloPublicacion = tituloPublicacion;
            this.profileImageUrl = profileImageUrl;
            this.idClothes = idClothes;
            this.idOwner = keydos;
    }

    public publicacion(String tituloPublicacion, String profileImageUrl, String idClothes, String keydos, String comentario, float valoracion) {
        this.tituloPublicacion = tituloPublicacion;
        this.profileImageUrl = profileImageUrl;
        this.idClothes = idClothes;
        this.idOwner = keydos;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }
}



