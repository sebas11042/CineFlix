package com.cineflix.controller;

import com.cineflix.entity.Sala;
import com.cineflix.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    @GetMapping
    public List<Sala> listarSalas() {
        return salaRepository.findAll();
    }

    @PostMapping
    public Sala guardarSala(@RequestBody Sala sala) {
        return salaRepository.save(sala);
    }
}
