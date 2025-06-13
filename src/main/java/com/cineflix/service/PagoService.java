package com.cineflix.service;

import com.cineflix.dto.ReservaDTO;
import com.cineflix.repository.PagoProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PagoService {

    @Autowired
    private PagoProcedureRepository pagoProcedureRepository;

    @Autowired
    private PDFGenerator pdfGenerator;

    public byte[] procesarPago(ReservaDTO reserva, String nombre, String apellido, String correo, String metodoPago) {
        try {
            // ✅ Validación básica
            if (reserva == null) {
                throw new RuntimeException("❌ La reserva es nula. No se puede procesar el pago.");
            }

            if (reserva.getTotal() == null || reserva.getTotal().doubleValue() <= 0) {
                throw new RuntimeException("❌ El total a pagar es inválido o no definido.");
            }

            if (reserva.getAsientosSeleccionados() == null || reserva.getAsientosSeleccionados().isEmpty()) {
                throw new RuntimeException("❌ No hay asientos seleccionados para procesar el pago.");
            }

            // ✅ Paso 1: Registrar el pago
            Integer idPago = pagoProcedureRepository.registrarPago(
                    reserva.getTotal(),
                    metodoPago,
                    LocalDateTime.now()
            );

            if (idPago == null) {
                throw new RuntimeException("❌ registrarPago no devolvió ningún ID.");
            }

            System.out.println("✅ idPago generado correctamente: " + idPago);
            reserva.setIdReserva(idPago);

            // ✅ Paso 2: Generar CSV de asientos
            String asientosCSV = reserva.getAsientosSeleccionados().stream()
                    .map(a -> String.valueOf(a.getId_asiento()))
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");

            if (asientosCSV.isEmpty()) {
                throw new RuntimeException("❌ Error generando CSV de asientos.");
            }

            System.out.println("🎟️ Asientos CSV: " + asientosCSV);

            // ✅ Paso 3: Registrar los boletos
            pagoProcedureRepository.registrarBoletos(
                reserva.getIdFuncion(),
                1, // ID de usuario simulado
                idPago,
                1, // ID de tipo precio simulado (deberías ajustar si es necesario)
                "confirmado",
                asientosCSV
            );

            System.out.println("🎟️ Boletos registrados correctamente para idPago " + idPago);

            // ✅ Paso 4: Generar PDF
            return pdfGenerator.generarPDFReserva(reserva, nombre, apellido, correo, metodoPago);

        } catch (Exception e) {
            // 🔴 Captura cualquier error, incluyendo los SQL
            System.err.println("⛔ Error al procesar el pago:");
            e.printStackTrace();

            // Se lanza la excepción para que el controlador la detecte
            throw new RuntimeException("⛔ Error procesando el pago: " + e.getMessage(), e);
        }
    }
}
