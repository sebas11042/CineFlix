package com.cineflix.service;

import com.cineflix.entity.TipoPrecio;
import java.util.List;

public interface TipoPrecioService {
    List<TipoPrecio> obtenerTiposPorEdad(); // aún puede usarse para otros casos generales
    List<TipoPrecio> obtenerTiposPorEdadYFormato(String formato); // 🔹 método nuevo filtrado por formato
    TipoPrecio obtenerPorId(Integer id);
}
