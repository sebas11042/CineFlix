package com.cineflix.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReporteVentasDTO {
    private String pelicula;
    private LocalDate fecha;
    private Integer asientosVendidos;
    private BigDecimal recaudadoPorPelicula;
    private BigDecimal totalDelDia;

    public ReporteVentasDTO(String pelicula, LocalDate fecha, Integer asientosVendidos,
                            BigDecimal recaudadoPorPelicula, BigDecimal totalDelDia) {
        this.pelicula = pelicula;
        this.fecha = fecha;
        this.asientosVendidos = asientosVendidos;
        this.recaudadoPorPelicula = recaudadoPorPelicula;
        this.totalDelDia = totalDelDia;
    }

    // Getters
    public String getPelicula() { return pelicula; }
    public LocalDate getFecha() { return fecha; }
    public Integer getAsientosVendidos() { return asientosVendidos; }
    public BigDecimal getRecaudadoPorPelicula() { return recaudadoPorPelicula; }
    public BigDecimal getTotalDelDia() { return totalDelDia; }
}
