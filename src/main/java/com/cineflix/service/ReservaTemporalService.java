package com.cineflix.service;

import java.util.List;

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
        Query query = entityManager.createNativeQuery("EXEC bloquear_asientos_temporalmente ?, ?");
        query.setParameter(1, idFuncion);
        query.setParameter(2, asientosCSV);
        query.executeUpdate();
    }

    @Transactional
    public void liberarAsientosExpirados() {
        Query query = entityManager.createNativeQuery("EXEC liberar_reservas_expiradas");
        query.executeUpdate();
    }

    @Transactional
    public void liberarAsientosReservados(Integer idFuncion, String asientosCSV) {
        Query query = entityManager.createNativeQuery("EXEC liberar_reserva_usuario ?, ?");
        query.setParameter(1, idFuncion);
        query.setParameter(2, asientosCSV);
        query.executeUpdate();
    }

    public List<Integer> obtenerIdsAsientosBloqueadosTemporalmente(Integer idFuncion) {
    Query query = entityManager.createNativeQuery("EXEC obtener_asientos_temporalmente_bloqueados :idFuncion");
    query.setParameter("idFuncion", idFuncion);
    return query.getResultList();
}


}
