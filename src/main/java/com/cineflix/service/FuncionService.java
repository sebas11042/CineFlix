package com.cineflix.service;

import com.cineflix.dto.FuncionViewDTO;
import com.cineflix.entity.Funcion;

import java.util.List;

public interface FuncionService {
    List<FuncionViewDTO> obtenerFuncionesPorPelicula(int idPelicula);
        Funcion obtenerPorId(Integer idFuncion); // NUEVO MÉTODO

}
