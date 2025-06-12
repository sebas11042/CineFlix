package com.cineflix.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class ReservaTemporalService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void bloquearAsientosTemporal(Integer idFuncion, String asientosCSV) {
        Query query = entityManager.createNativeQuery("EXEC bloquear_asientos_temporal ?, ?");
        query.setParameter(1, idFuncion);
        query.setParameter(2, asientosCSV);
        query.executeUpdate();
    }

    @Transactional
    public void liberarAsientosExpirados() {
        Query query = entityManager.createNativeQuery("EXEC liberar_reservas_expiradas");
        query.executeUpdate();
    }
}
