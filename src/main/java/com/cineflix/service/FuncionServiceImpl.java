package com.cineflix.service;

import com.cineflix.entity.Funcion;
import com.cineflix.repository.FuncionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionServiceImpl implements FuncionService {

    @Autowired
    private FuncionRepository funcionRepository;

    @Override
    public List<Funcion> obtenerFuncionesPorPelicula(int idPelicula) {
        return funcionRepository.findByPeliculaId(idPelicula);
    }
}
