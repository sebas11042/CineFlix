package com.cineflix.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Pelicula")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private Integer idPelicula;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "sinopsis")
    private String sinopsis;

    @Column(name = "duracion_min")
    private Integer duracionMin;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "fecha_salida", updatable = false)
    private java.time.LocalDateTime fechaSalida;

    // Getters y setters
    public Integer getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Integer idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Integer getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(Integer duracionMin) {
        this.duracionMin = duracionMin;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public java.time.LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(java.time.LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
