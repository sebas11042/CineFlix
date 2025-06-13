package com.cineflix.service;

import com.cineflix.dto.ReporteVentasDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class PDFReporteVentasGenerator {

    public static byte[] generarPDF(List<ReporteVentasDTO> reportes) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Logo
        try {
            Image logo = Image.getInstance("src/main/resources/static/img/logo/LOGO.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.ALIGN_LEFT);
            document.add(logo);
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar el logo: " + e.getMessage());
        }

        // Título
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Ventas por Película y Fecha", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Agrupar por fecha
        Map<String, List<ReporteVentasDTO>> agrupadoPorFecha = new TreeMap<>(Collections.reverseOrder());
        for (ReporteVentasDTO dto : reportes) {
            agrupadoPorFecha.computeIfAbsent(dto.getFecha().toString(), k -> new ArrayList<>()).add(dto);
        }

        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
        Font fechaFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(30, 60, 90));

        BaseColor headerColor = new BaseColor(30, 60, 90);       // Azul petróleo
        BaseColor totalColor = new BaseColor(20, 45, 80);        // Azul profundo
        BaseColor fechaBackground = new BaseColor(220, 230, 250); // Azul claro grisáceo

        for (Map.Entry<String, List<ReporteVentasDTO>> entry : agrupadoPorFecha.entrySet()) {
            String fecha = entry.getKey();
            List<ReporteVentasDTO> lista = entry.getValue();

            // Subtítulo de fecha (como tabla de una sola celda para estilo)
            PdfPCell fechaCell = new PdfPCell(new Phrase("\uD83D\uDCC5 Fecha: " + fecha, fechaFont));
            fechaCell.setColspan(3);
            fechaCell.setBackgroundColor(fechaBackground);
            fechaCell.setPadding(8f);
            fechaCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fechaCell.setBorder(Rectangle.NO_BORDER);

            PdfPTable fechaTable = new PdfPTable(1);
            fechaTable.setWidthPercentage(100f);
            fechaTable.addCell(fechaCell);
            document.add(fechaTable);

            // Tabla de datos para la fecha actual
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{4, 2, 2});

            String[] headers = {"Película", "Asientos Vendidos", "Recaudado"};
            for (String h : headers) {
                PdfPCell header = new PdfPCell(new Phrase(h, headFont));
                header.setBackgroundColor(headerColor);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setPadding(6f);
                table.addCell(header);
            }

            int totalAsientos = 0;
            BigDecimal totalRecaudado = BigDecimal.ZERO;

            for (ReporteVentasDTO r : lista) {
                table.addCell(new PdfPCell(new Phrase(r.getPelicula(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(r.getAsientosVendidos()), dataFont)));
                table.addCell(new PdfPCell(new Phrase("₡" + r.getRecaudadoPorPelicula().toPlainString(), dataFont)));

                totalAsientos += r.getAsientosVendidos();
                totalRecaudado = totalRecaudado.add(r.getRecaudadoPorPelicula());
            }

            PdfPCell totalCell1 = new PdfPCell(new Phrase("Total del Día", headFont));
            totalCell1.setBackgroundColor(totalColor);
            totalCell1.setPadding(6f);

            PdfPCell totalCell2 = new PdfPCell(new Phrase(String.valueOf(totalAsientos), headFont));
            totalCell2.setBackgroundColor(totalColor);
            totalCell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell totalCell3 = new PdfPCell(new Phrase("₡" + totalRecaudado.toPlainString(), headFont));
            totalCell3.setBackgroundColor(totalColor);
            totalCell3.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(totalCell1);
            table.addCell(totalCell2);
            table.addCell(totalCell3);

            document.add(table);
        }

        document.close();
        return out.toByteArray();
    }
}
