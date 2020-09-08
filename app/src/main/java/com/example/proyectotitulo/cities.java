package com.example.proyectotitulo;

import java.util.List;

public class cities {
    List<String> comunas;
    String region;
    String numero;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getComunas() {
        return comunas;
    }

    public void setComunas(List<String> comunas) {
        this.comunas = comunas;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "cities{" +
                "region='" + region + '\'' +
                ", numero=" + numero +
                ", comunas=" + comunas +
                '}';
    }
}
