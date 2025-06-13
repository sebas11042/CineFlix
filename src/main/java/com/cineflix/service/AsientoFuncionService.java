package com.cineflix.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cineflix.entity.Asiento;
import com.cineflix.repository.AsientoFuncionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class AsientoFuncionService {

    @Autowired
    private AsientoFuncionRepository asientoFuncionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // ✅ Incluye ocupados confirmados + bloqueos temporales
    public List<Asiento> obtenerAsientosOcupadosPorFuncion(Integer idFuncion) {
        String sql = """
            SELECT DISTINCT a.id_asiento, a.fila, a.columna, a.tipo
            FROM Asiento a
            JOIN (
                SELECT id_asiento FROM AsientoFuncion WHERE id_funcion = ? AND ocupado = 1
                UNION
                SELECT id_asiento FROM ReservaTemporal WHERE id_funcion = ?
            ) bloqueados ON a.id_asiento = bloqueados.id_asiento
        """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, idFuncion);
        query.setParameter(2, idFuncion);

        List<Object[]> resultados = query.getResultList();

        return resultados.stream().map(r -> {
            Asiento asiento = new Asiento();
            asiento.setId_asiento((Integer) r[0]);
            asiento.setFila((char) r[1]);
            asiento.setColumna((Integer) r[2]);
            asiento.setTipo((String) r[3]);
            return asiento;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void ocuparAsientos(Integer idFuncion, String asientosCSV) {
        Query query = entityManager.createNativeQuery("EXEC ocupar_asientos_funcion ?, ?");
        query.setParameter(1, idFuncion);
        query.setParameter(2, asientosCSV);
        query.executeUpdate();
    }
}
