package com.cineflix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cineflix.entity.Pelicula;
import com.cineflix.service.PeliculaService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PeliculaService peliculaService;

    public AdminController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping
    public String verIndexAdmin(Model model) {
        model.addAttribute("peliculasEstreno", peliculaService.obtenerPeliculasEnEstreno());
        model.addAttribute("peliculasOtras", peliculaService.obtenerPeliculasNoEstrenoNiProximas());
        model.addAttribute("peliculasProximas", peliculaService.obtenerPeliculasProximas());
        return "indexAdmin";
    }

    @GetMapping("/nuevaPelicula")
    public String nuevaPeliculas(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "NuevaPelicula";
    }

    @PostMapping("/guardarPelicula")
    public String guardarPelicula(Pelicula pelicula) {
        peliculaService.store(pelicula);
        return "redirect:/admin";
    }
}
