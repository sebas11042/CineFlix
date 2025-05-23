package com.cineflix.controller;

import com.cineflix.entity.TipoPrecio;
import com.cineflix.repository.TipoPrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-precio")
public class TipoPrecioController {

    @Autowired
    private TipoPrecioRepository tipoPrecioRepository;

    // GET: listar todos los tipos de precio
    @GetMapping
    public List<TipoPrecio> listarTodos() {
        return tipoPrecioRepository.findAll();
    }

    // POST: agregar un nuevo tipo de precio
    @PostMapping
    public TipoPrecio guardar(@RequestBody TipoPrecio tipoPrecio) {
        return tipoPrecioRepository.save(tipoPrecio);
    }
}
