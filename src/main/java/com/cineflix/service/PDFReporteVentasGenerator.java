package com.cineflix.service;

import com.cineflix.dto.ReporteVentasDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
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
            // Por si no encuentra el logo
            System.err.println("⚠️ No se pudo cargar el logo: " + e.getMessage());
        }

        // Título
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Ventas por Película y Fecha", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Tabla
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3, 2, 2, 2, 2});
        table.setSpacingBefore(10f);

        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(0, 102, 204);

        String[] headers = {"Película", "Fecha", "Asientos Vendidos", "Recaudado", "Total del Día"};
        for (String h : headers) {
            PdfPCell header = new PdfPCell(new Phrase(h, headFont));
            header.setBackgroundColor(headerColor);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(8f);
            table.addCell(header);
        }

        for (ReporteVentasDTO r : reportes) {
            table.addCell(new PdfPCell(new Phrase(r.getPelicula())));
            table.addCell(new PdfPCell(new Phrase(r.getFecha().toString())));
            table.addCell(new PdfPCell(new Phrase(r.getAsientosVendidos().toString())));
            table.addCell(new PdfPCell(new Phrase("₡" + r.getRecaudadoPorPelicula())));
            table.addCell(new PdfPCell(new Phrase("₡" + r.getTotalDelDia())));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }
}
