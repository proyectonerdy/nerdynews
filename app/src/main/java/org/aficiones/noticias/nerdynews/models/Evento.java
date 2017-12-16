package org.aficiones.noticias.nerdynews.models;

/**
 * Created by eloy on 7/11/17.
 */

public class Evento {

    private String titulo;
    private String resumen;
    private String fecha;
    private String lugar;
    private String imageUrl;
    private String coordGPS;

    public Evento(String titulo, String resumen, String fecha, String imageUrl, String lugar, String coordGPS) {
        this.titulo = titulo;
        this.resumen = resumen;
        this.fecha = fecha;
        this.lugar = lugar;
        this.imageUrl = imageUrl;
        this.coordGPS = coordGPS;
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

    public String getCoordGPS() {
        return coordGPS;
    }

    public void setCoordGPS(String coordGPS) {
        this.coordGPS = coordGPS;
    }
}
