package com.cineflix.repository;

import com.cineflix.entity.TipoPrecio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPrecioRepository extends JpaRepository<TipoPrecio, Integer> {
List<TipoPrecio> findByFormato(String formato);

}
