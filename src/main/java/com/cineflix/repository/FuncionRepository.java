package com.cineflix.repository;

import com.cineflix.entity.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FuncionRepository extends JpaRepository<Funcion, Integer> {

    // 💡 Este es el método que te falta:
    List<Funcion> findByPeliculaId(int idPelicula);
}
