package com.example.caxidy.listaphp;

public class Pelicula {
    int idPelicula, valoracion;
    String nombre, sinopsis, categoria;

    public Pelicula(int idPelicula, int valoracion, String nombre, String sinopsis, String categoria) {
        this.idPelicula = idPelicula;
        this.valoracion = valoracion;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.categoria = categoria;
    }

    public Pelicula(int idPelicula) {
        this.idPelicula = idPelicula;
        this.valoracion = 1;
        this.nombre = "";
        this.sinopsis = "";
        this.categoria = "";
    }

    public Pelicula() {}

    public int getIdPelicula() {
        return idPelicula;
    }

    public int getValoracion() {
        return valoracion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
