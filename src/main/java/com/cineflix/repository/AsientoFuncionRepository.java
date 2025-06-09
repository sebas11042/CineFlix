package com.cineflix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cineflix.entity.AsientoFuncion;

public interface AsientoFuncionRepository extends JpaRepository<AsientoFuncion, Integer> {

    @Query(value = "EXEC ObtenerAsientosOcupadosPorFuncion :idFuncion", nativeQuery = true)
    List<Object[]> obtenerAsientosOcupados(@Param("idFuncion") Integer idFuncion);
}
