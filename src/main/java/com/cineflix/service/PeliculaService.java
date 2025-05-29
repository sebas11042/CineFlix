package com.cineflix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.cineflix.entity.Pelicula;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class PeliculaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Pelicula> obtenerPeliculasEnEstreno() {
        return jdbcTemplate.query("EXEC ObtenerPeliculasEnEstreno", new PeliculaRowMapper());
    }

    public List<Pelicula> obtenerPeliculasNoEstrenoNiProximas() {
        return jdbcTemplate.query("EXEC ObtenerPeliculasNoEstrenoNiProximas", new PeliculaRowMapper());
    }

      public List<Pelicula> obtenerPeliculasProximas() {
        return jdbcTemplate.query("EXEC ObtenerPeliculasProximas;", new PeliculaRowMapper());
    }


    private static class PeliculaRowMapper implements RowMapper<Pelicula> {
        @Override
        public Pelicula mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pelicula pelicula = new Pelicula();
            pelicula.setTitulo(rs.getString("titulo"));
            pelicula.setSinopsis(rs.getString("sinopsis"));
            pelicula.setDuracionMin(rs.getInt("duracion_min"));
            pelicula.setTrailerUrl(rs.getString("trailer_url"));
            pelicula.setImagenUrl(rs.getString("imagen_url"));
            pelicula.setFechaSalida(rs.getDate("fecha_salida").toLocalDate().atStartOfDay());
            return pelicula;
        }
    }
}
