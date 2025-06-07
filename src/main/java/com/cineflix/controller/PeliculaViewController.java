package com.cineflix.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cineflix.entity.Pelicula;
import com.cineflix.service.PeliculaService;

@Controller
public class PeliculaViewController {

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/")
    public String verPeliculasEnIndex(Model model) {
        model.addAttribute("peliculasEstreno", peliculaService.obtenerPeliculasEnEstreno());
        model.addAttribute("peliculasOtras", peliculaService.obtenerPeliculasNoEstrenoNiProximas());
        model.addAttribute("peliculasProximas", peliculaService.obtenerPeliculasProximas());
        return "index";
    }
    @GetMapping("/pelicula/{id}")
    public String verPeliculas(@PathVariable int id, Model model) {
        Pelicula pelicula = peliculaService.findById(id);
        if (pelicula != null) {
            model.addAttribute("pelicula", pelicula);
            return "DetallePelicula";
        }
        // Return a 404 error page if pelicula is not found
        return "error/404";
    }
}
