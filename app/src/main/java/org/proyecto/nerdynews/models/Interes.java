package org.proyecto.nerdynews.models;


/**
 * Created by Ana on 1/11/17.
 */

public class Interes {

    private String titulo;
    private String resumen;
    private String imageUrl;
    private String imageFavorito;
    private int esFavorito;

    public Interes(String titulo, String resumen, String imageUrl, String imageFavorito, int esFavorito) {
        this.titulo = titulo;
        this.resumen = resumen;
        this.imageUrl = imageUrl;
        this.imageFavorito = imageFavorito;
        this.esFavorito = esFavorito;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageFavorito() {
        return imageFavorito;
    }

    public void setImageFavorito(String imageFavorito) {
        this.imageFavorito = imageFavorito;
    }

    public int getEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(int esFavorito) {
        this.esFavorito = esFavorito;
    }



}
