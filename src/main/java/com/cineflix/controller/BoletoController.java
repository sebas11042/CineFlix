package com.cineflix.controller;

import com.cineflix.entity.Boleto;
import com.cineflix.repository.BoletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    @Autowired
    private BoletoRepository boletoRepository;

    // GET: listar todos los boletos
    @GetMapping
    public List<Boleto> listarBoletos() {
        return boletoRepository.findAll();
    }

    // POST: crear nuevo boleto
    @PostMapping
    public Boleto crearBoleto(@RequestBody Boleto boleto) {
        return boletoRepository.save(boleto);
    }
}
