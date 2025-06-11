package com.cineflix.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ Import necesario

import com.cineflix.entity.Asiento;
import com.cineflix.repository.AsientoFuncionRepository;

import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AsientoFuncionService {

    @Autowired
    private AsientoFuncionRepository asientoFuncionRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional // ✅ Transacción necesaria para queries que modifican datos
    public void ocuparAsientos(Integer idFuncion, String asientosCSV) {
        Query query = entityManager.createNativeQuery("EXEC ocupar_asientos_funcion ?, ?");
        query.setParameter(1, idFuncion);
        query.setParameter(2, asientosCSV);
        query.executeUpdate();
    }
}
