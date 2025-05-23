package com.cineflix.controller;

import com.cineflix.entity.Pelicula;
import com.cineflix.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @GetMapping
    public List<Pelicula> getPeliculas() {
        return peliculaRepository.findAll();
    }
}
