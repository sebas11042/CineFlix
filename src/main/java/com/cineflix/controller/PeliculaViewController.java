package com.cineflix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cineflix.dto.FuncionViewDTO;
import com.cineflix.entity.Pelicula;
import com.cineflix.service.FuncionService;
import com.cineflix.service.PeliculaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PeliculaViewController {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
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
            List<FuncionViewDTO> funciones = funcionService.obtenerFuncionesPorPelicula(id);
            model.addAttribute("funciones", funciones); // 👈 sin Map
            return "DetallePelicula";
        }
        return "error/404";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "cancel";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
