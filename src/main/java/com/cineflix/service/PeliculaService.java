package com.cineflix.service;

import com.cineflix.entity.Pelicula;
import java.util.List;

public interface PeliculaService {
    List<Pelicula> obtenerTodas();
    Pelicula obtenerPorId(Integer id);
}
