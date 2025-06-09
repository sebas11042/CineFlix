package com.cineflix.service;

import com.cineflix.entity.Asiento;
import com.cineflix.entity.Sala;
import com.cineflix.repository.AsientoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsientoServiceImpl implements AsientoService {

    @Autowired
    private AsientoRepository asientoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Asiento> obtenerAsientosPorSala(Sala sala) {
        return asientoRepository.findBySala(sala);
    }

    @Override
    public List<Asiento> obtenerAsientosOcupadosPorFuncion(Integer idFuncion) {
        Query query = entityManager.createNativeQuery("EXEC ObtenerAsientosReservadosPorFuncion :idFuncion", Asiento.class);
        query.setParameter("idFuncion", idFuncion);
        return query.getResultList();
    }
}
