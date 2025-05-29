package com.cineflix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cineflix.entity.Pelicula;
import com.cineflix.service.PeliculaService;

@RestController
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/estrenos")
    public List<Pelicula> getPeliculasEnEstreno() {
        return peliculaService.obtenerPeliculasEnEstreno();
    }

    @GetMapping("/otras")
    public List<Pelicula> getPeliculasNoEstrenoNiProximas() {
        return peliculaService.obtenerPeliculasNoEstrenoNiProximas();
    }
}

