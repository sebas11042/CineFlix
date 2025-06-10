package com.cineflix.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cineflix.entity.Asiento;
import com.cineflix.repository.AsientoFuncionRepository;

@Service
public class AsientoFuncionService {

    @Autowired
    private AsientoFuncionRepository asientoFuncionRepository;

    public List<Asiento> obtenerAsientosOcupadosPorFuncion(Integer idFuncion) {
        List<Object[]> resultados = asientoFuncionRepository.obtenerAsientosOcupados(idFuncion);

        return resultados.stream().map(r -> {
            Asiento a = new Asiento();
            a.setId_asiento((Integer) r[0]);
            a.setFila((char) r[1]);
            a.setColumna((Integer) r[2]);
            a.setTipo((String) r[3]);
            return a;
        }).collect(Collectors.toList());
    }
}
