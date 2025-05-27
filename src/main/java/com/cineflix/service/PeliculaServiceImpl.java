package com.cineflix.service;

import com.cineflix.entity.Pelicula;
import com.cineflix.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeliculaServiceImpl implements PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Override
    public List<Pelicula> obtenerTodas() {
        return peliculaRepository.findAll();
    }

    @Override
    public Pelicula obtenerPorId(Integer id) {
        return peliculaRepository.findById(id).orElse(null);
    }
}
