package com.cineflix.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.transaction.Transactional;

@Repository
public class PagoProcedureRepository {

    @PersistenceContext
    private EntityManager entityManager;
    
    public Integer registrarPago(BigDecimal montoTotal, String metodoPago, LocalDateTime fechaPago) {
        Query query = entityManager.createNativeQuery("EXEC registrar_pago ?, ?, ?");
        query.setParameter(1, montoTotal);
        query.setParameter(2, metodoPago);
        query.setParameter(3, fechaPago);

        Object result = query.getSingleResult();  // Recibe el SELECT id_pago
        if (result instanceof Object[]) {
            return (Integer) ((Object[]) result)[0];
        } else {
            return (Integer) result;
        }
    }

    @Transactional
    public void registrarBoletos(
            Integer idFuncion,
            Integer idUsuario,
            Integer idPago,
            Integer idTipoPrecio,
            String estado,
            String asientosCSV // Ejemplo: "5,6,7"
    ) {
        Query query = entityManager.createNativeQuery("EXEC registrar_boletos ?, ?, ?, ?, ?, ?");
        query.setParameter(1, idFuncion);
        query.setParameter(2, idUsuario);
        query.setParameter(3, idPago);
        query.setParameter(4, idTipoPrecio);
        query.setParameter(5, estado);
        query.setParameter(6, asientosCSV);
        query.executeUpdate();
    }
}
