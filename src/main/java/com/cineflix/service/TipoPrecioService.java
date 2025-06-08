package com.cineflix.service;
import com.cineflix.entity.*;
import java.util.List;

public interface TipoPrecioService {
    List<TipoPrecio> obtenerTiposPorEdad();
    TipoPrecio obtenerPorId(Integer id); // 👈 nuevo método requerido

}