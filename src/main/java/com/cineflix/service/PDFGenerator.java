package com.cineflix.service;

import com.cineflix.dto.ReservaDTO;
import com.cineflix.entity.Asiento;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFGenerator {

    public byte[] generarPDFReserva(ReservaDTO reserva, String nombre, String apellido, String correo, String metodoPago) {
        try {
            Document documento = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(documento, baos);

            documento.open();

            documento.add(new Paragraph("🎟️ Confirmación de Reserva - CineFlix"));
            documento.add(new Paragraph("Cliente: " + nombre + " " + apellido));
            documento.add(new Paragraph("Correo: " + correo));
            documento.add(new Paragraph("Película: " + reserva.getPelicula().getTitulo()));
            documento.add(new Paragraph("Sala: " + reserva.getSala().getNombre()));
            documento.add(new Paragraph("Función: " + reserva.getFecha() + " - " + reserva.getHora()));
            documento.add(new Paragraph("Método de pago: " + metodoPago));
            documento.add(new Paragraph("Boletos:"));

            for (Asiento asiento : reserva.getAsientosSeleccionados()) {
                documento.add(new Paragraph(" - Asiento: " + asiento.getFila() + asiento.getColumna()));
            }

            documento.add(new Paragraph("Total pagado: ₡" + reserva.getTotal()));

            documento.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
