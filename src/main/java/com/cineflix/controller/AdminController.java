package com.cineflix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/editarPelicula/{id}")
    public String editarPelicula(Integer id, Model model) {
        Pelicula pelicula = peliculaService.findById(id);
        if (pelicula != null) {
            model.addAttribute("pelicula", pelicula);
            return "EditarPelicula";
        }
        return "redirect:/EditarPelicula";
    }

    @PostMapping("/actualizarPelicula")
    public String actualizarPelicula(Pelicula pelicula) {
        peliculaService.store(pelicula);
        return "redirect:/admin";
    }

    @PostMapping("/eliminarPelicula/{id}")
    public String eliminarPelicula(@PathVariable int id) {
        peliculaService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String idPelicula(@PathVariable int id, Model model) {
        Pelicula pelicula = peliculaService.findById(id);
        if (pelicula != null) {
            model.addAttribute("pelicula", pelicula);
            return "PeliculaDetalle";
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Pelicula pelicula = peliculaService.findById(id);
        if (pelicula != null) {
            model.addAttribute("pelicula", pelicula);
            return "EditarPelicula"; // Asegúrate de que editarPelicula.html existe en src/main/resources/templates
        } else {
            return "redirect:/"; // Redirigir a la página principal si no se encuentra la película
        }
    }

    @PostMapping("/update")
    public String update(Pelicula pelicula) {
        peliculaService.store(pelicula);
        return "redirect:/admin";
    }
}
