package com.cineflix.controller;

import com.cineflix.dto.ReporteVentasDTO;
import com.cineflix.service.ReporteService;
import com.cineflix.service.PDFReporteVentasGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/ventas")
    public List<ReporteVentasDTO> obtenerReporteVentas() {
        return reporteService.obtenerReporteVentas();
    }

    @GetMapping("/ventas/pdf")
    public ResponseEntity<byte[]> exportarReporteVentasPDF() {
        try {
            List<ReporteVentasDTO> reportes = reporteService.obtenerReporteVentas();
            byte[] pdfBytes = PDFReporteVentasGenerator.generarPDF(reportes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte_ventas.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
