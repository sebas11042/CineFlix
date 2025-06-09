package com.cineflix.repository;

import com.cineflix.entity.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionRepository extends JpaRepository<Funcion, Integer> {
    List<Funcion> findByPelicula_IdPelicula(int idPelicula); // ✅ CORRECTO
    List<Funcion> findBySala_IdSala(int idSala); // ✅ CORRECTO
    
}
