package com.cineflix.repository;

import com.cineflix.entity.Asiento;
import com.cineflix.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsientoRepository extends JpaRepository<Asiento, Integer> {
    
    // Todos los asientos de una sala
    List<Asiento> findBySala(Sala sala);
}
