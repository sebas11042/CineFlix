package com.cineflix.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cineflix.entity.Funcion;
import com.cineflix.entity.Pelicula;
import com.cineflix.service.FuncionService;
import com.cineflix.service.PeliculaService;

@Controller
public class PeliculaViewController {

    @Autowired
    private PeliculaService peliculaService;
    private FuncionService funcionService;

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
            // 👉 Aquí puedes agregar también las funciones disponibles:
            List<Funcion> funciones = funcionService.obtenerFuncionesPorPelicula(id); // ✅ CORRECTO
            model.addAttribute("funciones", funciones);
            return "DetallePelicula";
        }
        return "error/404";
    }

}
