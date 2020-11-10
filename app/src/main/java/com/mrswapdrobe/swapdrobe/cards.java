
package com.mrswapdrobe.swapdrobe;

/* Clase Card correspondiente al contenido de cada card de PaginaPrincipal. esta clase se vincula con arrayAdaptor.
arrayAdaptor despliega la informacion de estos cards en una view , mediante una lista de cards que se añaden a ese view y generan las tarjetas.
 */

public class cards {
    private String clothesId;
    private String name;
    private String profileImageUrl;
    private String ownerId;
    private double puntuacionGeneral;
    private String precio;

    public cards (String clothesId, String name, String profileImageUrl,String ownerId){
        this.clothesId = clothesId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.ownerId = ownerId;
    }
    public cards (String clothesId, String name, String profileImageUrl,String ownerId,double puntuacionGeneral){
        this.clothesId = clothesId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.ownerId = ownerId;
        this.puntuacionGeneral = puntuacionGeneral;
    }
    public cards (String clothesId, String name, String profileImageUrl,String ownerId,double puntuacionGeneral,String precio){
        this.clothesId = clothesId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.ownerId = ownerId;
        this.puntuacionGeneral = puntuacionGeneral;
        this.precio = precio;
    }

    public String getClothesId(){
        return clothesId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setClothesId(String clothesId){
        this.clothesId = clothesId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public double getPuntuacionGeneral() {
        return puntuacionGeneral;
    }

    public void setPuntuacionGeneral(double puntuacionGeneral) {
        this.puntuacionGeneral = puntuacionGeneral;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}

