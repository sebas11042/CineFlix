package com.cineflix.controller;

import com.cineflix.entity.Pago;
import com.cineflix.repository.PagoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository; 


    @GetMapping
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    @PostMapping
    public Pago guardar(@RequestBody Pago pago) {
        return pagoRepository.save(pago);
    }
}
