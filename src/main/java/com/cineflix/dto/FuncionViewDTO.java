package com.cineflix.dto;

public class FuncionViewDTO {
    private String sala;
    private String formato;
    private String idioma;
    private String fecha;
    private String hora;
    private int idFuncion;

    // Getters y setters
    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }
    public String getFormato() {
        return formato;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
    public String getIdioma() {
        return idioma;
    }
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public int getIdFuncion() {
        return idFuncion;
    }
    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }
}
