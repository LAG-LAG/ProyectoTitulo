
package com.example.proyectotitulo;

/* Clase Chat correspondiente al contenido de cada chat de misChats. esta clase se vincula con chatAdapter.
chatAdapter despliega la informacion de estos chats en un recyclerView, mediante una lista de chats.
 */

public class Chat {
    private String tituloPublicacion;
    private String profileImageUrl;
    private String valorPublicacion;
    private String idClothes;
    private String nombreVendedor;

    public Chat(String tituloPublicacion, String profileImageUrl) {
        this.tituloPublicacion = tituloPublicacion;
    }

    public Chat(String tituloPublicacion, String profileImageUrl, String idClothes) {
        this.tituloPublicacion = tituloPublicacion;
        this.profileImageUrl = profileImageUrl;
        this.idClothes = idClothes;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public Chat(String tituloPublicacion, String profileImageUrl, String idClothes, String nombreVendedor) {
        this.tituloPublicacion = tituloPublicacion;
        this.profileImageUrl = profileImageUrl;
        this.idClothes = idClothes;
        this.nombreVendedor = nombreVendedor;
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
