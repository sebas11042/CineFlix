package com.cineflix.service;

import com.cineflix.dto.FuncionViewDTO;
import java.util.List;

public interface FuncionService {
    List<FuncionViewDTO> obtenerFuncionesPorPelicula(int idPelicula);
}
