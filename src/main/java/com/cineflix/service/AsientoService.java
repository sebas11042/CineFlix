package com.cineflix.service;

import com.cineflix.entity.Asiento;
import com.cineflix.entity.Sala;

import java.util.List;

public interface AsientoService {
    List<Asiento> obtenerAsientosPorSala(Sala sala);
    List<Asiento> obtenerAsientosOcupadosPorFuncion(Integer idFuncion); // utiliza procedimiento almacenado
}
