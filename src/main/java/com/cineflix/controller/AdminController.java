package com.cineflix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cineflix.entity.Pelicula;
import com.cineflix.service.PeliculaService;

@Controller
@RequestMapping("/")
public class AdminController {
    private final PeliculaService peliculaService;

    public AdminController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping("/admin")
    public String verIndexAdmin(Model model) {
        model.addAttribute("peliculasEstreno", peliculaService.obtenerPeliculasEnEstreno());
        model.addAttribute("peliculasOtras", peliculaService.obtenerPeliculasNoEstrenoNiProximas());
        model.addAttribute("peliculasProximas", peliculaService.obtenerPeliculasProximas());
        return "indexAdmin";
    }

    @GetMapping("/admin/nuevaPelicula")
    public String nuevaPeliculas(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "NuevaPelicula";
    }

}
