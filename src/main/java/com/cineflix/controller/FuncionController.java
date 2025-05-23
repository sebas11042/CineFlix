package com.cineflix.controller;

import com.cineflix.entity.Funcion;
import com.cineflix.repository.FuncionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funciones")
public class FuncionController {

    @Autowired
    private FuncionRepository funcionRepository;

    @GetMapping
    public List<Funcion> listarFunciones() {
        return funcionRepository.findAll();
    }

    @PostMapping
    public Funcion guardarFuncion(@RequestBody Funcion funcion) {
        return funcionRepository.save(funcion);
    }
}
