package com.cineflix.service;

import com.cineflix.dto.ReporteVentasDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ReporteVentasDTO> obtenerReporteVentas() {
        Query query = entityManager.createNativeQuery("EXEC reporte_ventas_por_pelicula_y_fecha");

        List<Object[]> resultados = query.getResultList();
        List<ReporteVentasDTO> reportes = new ArrayList<>();

        for (Object[] fila : resultados) {
            String pelicula = (String) fila[0];
            LocalDate fecha = ((Date) fila[1]).toLocalDate();
            Integer asientosVendidos = ((Number) fila[2]).intValue();
            BigDecimal recaudadoPorPelicula = (BigDecimal) fila[3];
            BigDecimal totalDelDia = (BigDecimal) fila[4];

            reportes.add(new ReporteVentasDTO(
                    pelicula, fecha, asientosVendidos,
                    recaudadoPorPelicula, totalDelDia));
        }

        return reportes;
    }
}
