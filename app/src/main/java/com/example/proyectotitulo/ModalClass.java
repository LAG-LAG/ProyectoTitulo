package com.example.proyectotitulo;

import android.net.Uri;

public class ModalClass {

    String imagename;
    Uri image;

    public ModalClass() {
    }


    public ModalClass(String imagename, Uri image) {
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
