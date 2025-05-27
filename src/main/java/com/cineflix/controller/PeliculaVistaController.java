package com.cineflix.controller;

import com.cineflix.entity.Pelicula;
import com.cineflix.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PeliculaVistaController {

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/")
    public String mostrarCartelera(Model model) {
        model.addAttribute("peliculas", peliculaService.obtenerTodas());
        return "index"; // renderiza templates/index.html
    }

    @GetMapping("/pelicula/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Pelicula pelicula = peliculaService.obtenerPorId(id);
        model.addAttribute("pelicula", pelicula);
        return "detalle-pelicula"; // aún por crear
    }
}
