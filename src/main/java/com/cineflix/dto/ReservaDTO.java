package com.cineflix.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.cineflix.entity.Asiento;
import com.cineflix.entity.Pelicula;
import com.cineflix.entity.Sala;

public class ReservaDTO {
    private Integer idFuncion;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime hora;

    private String formato; // ✅ NUEVO: 2D, 3D, etc.

    // Boletos seleccionados: idTipoPrecio → cantidad
    private Map<Integer, Integer> boletosSeleccionados;

    // Asientos seleccionados en paso 2
    private List<Asiento> asientosSeleccionados;

    // Total con IVA y cargos
    private BigDecimal total;

    private String metodoPago; // tarjeta o PayPal

    // === GETTERS Y SETTERS ===

    public Integer getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(Integer idFuncion) {
        this.idFuncion = idFuncion;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Map<Integer, Integer> getBoletosSeleccionados() {
        return boletosSeleccionados;
    }

    public void setBoletosSeleccionados(Map<Integer, Integer> boletosSeleccionados) {
        this.boletosSeleccionados = boletosSeleccionados;
    }

    public List<Asiento> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public void setAsientosSeleccionados(List<Asiento> asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
