package com.cineflix.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
      
}
