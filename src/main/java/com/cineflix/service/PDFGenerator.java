package com.cineflix.service;

import com.cineflix.dto.ReservaDTO;
import com.cineflix.entity.Asiento;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class PDFGenerator {

    public byte[] generarPDFReserva(ReservaDTO reserva, String nombre, String apellido, String correo, String metodoPago) {
        try {
            if (reserva == null || reserva.getAsientosSeleccionados() == null || reserva.getAsientosSeleccionados().isEmpty()) {
                throw new IllegalArgumentException("Reserva inválida o sin asientos seleccionados.");
            }

            Document documento = new Document(PageSize.A4, 50, 50, 50, 50);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(documento, baos);
            documento.open();

            // Logo
            try {
                Image logo = Image.getInstance("src/main/resources/static/img/logo/LOGO.png");
                logo.scaleToFit(100, 60);
                logo.setAlignment(Image.LEFT);
                documento.add(logo);
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo cargar el logo de CineFlix.");
            }

            // Título
            Paragraph titulo = new Paragraph("🎟️ Confirmación de Reserva", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Paragraph("CineFlix", new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL)));
            documento.add(Chunk.NEWLINE);

            // Datos cliente
            Paragraph cliente = new Paragraph("Cliente: " + nombre + " " + apellido + 
                "\nCorreo: " + correo + 
                "\nMétodo de pago: " + metodoPago);
            cliente.setSpacingAfter(10);
            documento.add(cliente);

            // Poster película
            if (reserva.getPelicula() != null && reserva.getPelicula().getImagenUrl() != null && !reserva.getPelicula().getImagenUrl().isEmpty()) {
                try {
                    Image poster = Image.getInstance(new URL(reserva.getPelicula().getImagenUrl()));
                    poster.scaleToFit(150, 220);
                    poster.setAlignment(Image.ALIGN_CENTER);
                    documento.add(poster);
                } catch (Exception ex) {
                    System.out.println("⚠️ No se pudo cargar el poster de la película.");
                }
            }

            // Datos función
            if (reserva.getPelicula() != null && reserva.getSala() != null) {
                Paragraph datosFuncion = new Paragraph(
                    "Película: " + reserva.getPelicula().getTitulo() +
                    "\nSala: " + reserva.getSala().getNombre() +
                    "\nFecha y Hora: " + reserva.getFecha() + " - " + reserva.getHora()
                );
                datosFuncion.setSpacingBefore(10);
                datosFuncion.setSpacingAfter(10);
                documento.add(datosFuncion);
            }

            // Tabla de asientos
            List<Asiento> asientos = reserva.getAsientosSeleccionados();
            if (asientos != null && !asientos.isEmpty()) {
                PdfPTable tabla = new PdfPTable(2);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new int[]{1, 3});
                Font cabecera = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

                PdfPCell th1 = new PdfPCell(new Phrase("Asiento", cabecera));
                PdfPCell th2 = new PdfPCell(new Phrase("Fila - Columna", cabecera));
                th1.setBackgroundColor(BaseColor.DARK_GRAY);
                th2.setBackgroundColor(BaseColor.DARK_GRAY);
                tabla.addCell(th1);
                tabla.addCell(th2);

                for (Asiento a : asientos) {
                    tabla.addCell(String.valueOf(a.getId_asiento()));
                    tabla.addCell(a.getFila() + String.valueOf(a.getColumna()));
                }

                documento.add(tabla);
            }

            // Total pagado
            Paragraph total = new Paragraph("Total pagado: ₡" + reserva.getTotal(),
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
            total.setSpacingBefore(15);
            documento.add(total);

            // Código QR
            try {
                String qrContent = "https://cineflix.com/confirmacion/" + reserva.getIdReserva();
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 150, 150);
                BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ByteArrayOutputStream qrBaos = new ByteArrayOutputStream();
                ImageIO.write(qrImage, "png", qrBaos);
                Image qr = Image.getInstance(qrBaos.toByteArray());

                qr.setAlignment(Image.ALIGN_CENTER);
                qr.setSpacingBefore(20);
                documento.add(qr);

                Paragraph qrText = new Paragraph("Escanea este código al ingresar a la sala",
                        new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));
                qrText.setAlignment(Element.ALIGN_CENTER);
                documento.add(qrText);

            } catch (WriterException | IOException ex) {
                System.out.println("⚠️ Error generando el código QR.");
            }

            // Footer
            Paragraph footer = new Paragraph("Gracias por elegir CineFlix 🎬 ¡Disfruta tu película!",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(25);
            documento.add(footer);

            documento.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
