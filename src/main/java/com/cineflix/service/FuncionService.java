package com.cineflix.service;

import com.cineflix.entity.Funcion;
import java.util.List;

public interface FuncionService {
    List<Funcion> obtenerFuncionesPorPelicula(int idPelicula);
}
