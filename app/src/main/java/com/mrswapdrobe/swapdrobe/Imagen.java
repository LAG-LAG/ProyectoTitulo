package com.mrswapdrobe.swapdrobe;
/*
esta clase contiene los elementos de cada imagen subida en addpublicaciones. estos elementos se añaden en addImagenesAdapter.
 */
import android.net.Uri;

public class Imagen {

    String imagename;
    Uri image;

    public Imagen() {
    }


    public Imagen(String imagename, Uri image) {
        this.imagename = imagename;
        this.image = image;
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}
