package com.example.caxidy.listaphp;

public class Genero {
    int idGenero;
    String nombre;

    public Genero(int idGenero, String nombre) {
        this.idGenero = idGenero;
        this.nombre = nombre;
    }

    public Genero(int idGenero) {
        this.idGenero = idGenero;
        this.nombre = "";
    }

    public Genero() {}

    public int getIdGenero() {
        return idGenero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
