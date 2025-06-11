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

    @SuppressWarnings("unchecked")

    @Override
    public List<Asiento> obtenerAsientosOcupados(int idFuncion) {
        Query query = entityManager.createNativeQuery("EXEC ObtenerAsientosOcupadosPorFuncion :idFuncion", Asiento.class);
        query.setParameter("idFuncion", idFuncion);
        return query.getResultList();
    }
    @Override
    public Asiento obtenerPorId(Integer idAsiento) {
    return asientoRepository.findById(idAsiento).orElse(null);
    }
}


/*  
 CREATE OR ALTER PROCEDURE ObtenerAsientosOcupadosPorFuncion
    @idFuncion INT
AS
BEGIN
    SELECT a.id_asiento, a.fila, a.columna, a.tipo
    FROM AsientoFuncion af
    JOIN Asiento a ON af.id_asiento = a.id_asiento
    WHERE af.id_funcion = @idFuncion AND af.ocupado = 1
    ORDER BY a.fila, a.columna;
END;
 */