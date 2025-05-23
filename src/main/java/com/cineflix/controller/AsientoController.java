package com.cineflix.controller;

import com.cineflix.entity.Asiento;
import com.cineflix.repository.AsientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asientos")
public class AsientoController {

    @Autowired
    private AsientoRepository asientoRepository;

    @GetMapping
    public List<Asiento> listarTodos() {
        return asientoRepository.findAll();
    }

    @PostMapping
    public Asiento guardar(@RequestBody Asiento asiento) {
        return asientoRepository.save(asiento);
    }
}
