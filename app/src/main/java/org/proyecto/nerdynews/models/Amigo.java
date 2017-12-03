package org.proyecto.nerdynews.models;

/**
 * Created by MyC on 22-11-2017.
 */

public class Amigo {

    public String nombre;
    public String apellido;
    public int edad;
    public String foto;
    public String sexo;
    public String intereses;
    public String nro_intereses;
    public int id;


    public String getNro_intereses() {
        return nro_intereses;
    }

    public void setNro_intereses(String nro_intereses) {
        this.nro_intereses = nro_intereses;
    }

    public Amigo(String nombre, String apellido, int edad, String foto, String sexo, int id) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.foto = foto;
        this.sexo = sexo;
        this.id = id;


    }

    public String getNombre() {
        return nombre;
    }

    public String getIntereses() {
        return intereses;
    }

    public void setIntereses(String intereses) {
        this.intereses = intereses;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id ) {
        this.id = id;
    }
}
