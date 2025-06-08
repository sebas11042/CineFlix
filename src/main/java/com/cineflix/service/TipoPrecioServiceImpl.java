package com.cineflix.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cineflix.entity.TipoPrecio;
import com.cineflix.repository.TipoPrecioRepository;

import java.util.*;

@Service
public class TipoPrecioServiceImpl implements TipoPrecioService {

    @Autowired
    private TipoPrecioRepository tipoPrecioRepository;

    @Override
    public List<TipoPrecio> obtenerTiposPorEdad() {
        List<TipoPrecio> todos = tipoPrecioRepository.findAll();
        Map<String, TipoPrecio> unicos = new LinkedHashMap<>();

        for (TipoPrecio tp : todos) {
            if (!unicos.containsKey(tp.getEdadTipo())) {
                unicos.put(tp.getEdadTipo(), tp); // uno por edad_tipo (ej: adulto, niño)
            }
        }

        return new ArrayList<>(unicos.values());
    }
        @Override
    public TipoPrecio obtenerPorId(Integer id) {
        return tipoPrecioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de precio no encontrado con ID: " + id));
    }
}
