package org.proyecto.nerdynews.models;

/**
 * Created by eloy on 7/11/17.
 */

public class Evento {

    private String titulo;
    private String resumen;
    private String fecha;
    private String lugar;
    private String imageUrl;

    public Evento(String titulo, String resumen, String fecha, String imageUrl, String lugar) {
        this.titulo = titulo;
        this.resumen = resumen;
        this.fecha = fecha;
        this.lugar = lugar;
        this.imageUrl = imageUrl;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
