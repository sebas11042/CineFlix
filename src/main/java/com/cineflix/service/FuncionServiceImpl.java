package com.cineflix.service;

import com.cineflix.dto.FuncionViewDTO;
import com.cineflix.entity.Funcion;
import com.cineflix.repository.FuncionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionServiceImpl implements FuncionService {

    @Autowired
    private FuncionRepository funcionRepository;

    @Override
    public List<FuncionViewDTO> obtenerFuncionesPorPelicula(int idPelicula) {
        List<Funcion> funciones = funcionRepository.findByPelicula_IdPelicula(idPelicula);

        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

        return funciones.stream().map(funcion -> {
            FuncionViewDTO dto = new FuncionViewDTO();
            dto.setIdFuncion(funcion.getIdFuncion());
            dto.setSala(funcion.getSala().getNombre());
            dto.setFormato(funcion.getFormato());
            dto.setIdioma(funcion.getIdioma());
            dto.setFecha(funcion.getFecha().format(formatterFecha));
            dto.setHora(funcion.getHora().format(formatterHora));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Funcion obtenerPorId(Integer idFuncion) {
        return funcionRepository.findById(idFuncion)
                .orElseThrow(() -> new RuntimeException("Función no encontrada con ID: " + idFuncion));
    }

}
