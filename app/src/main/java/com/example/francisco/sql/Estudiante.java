package com.example.francisco.sql;

public class Estudiante {
    private String nombre;
    private String domicilio;
    private String telefono;
    private int Grado;

    public Estudiante(String nombre, String domicilio, String telefono, int grado) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.telefono = telefono;
        Grado = grado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getGrado() {
        return Grado;
    }

    public void setGrado(int grado) {
        Grado = grado;
    }
}
