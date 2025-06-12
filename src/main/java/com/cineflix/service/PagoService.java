package com.cineflix.service;

import com.cineflix.dto.ReservaDTO;
import com.cineflix.entity.Asiento;
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

        // Paso 1: Registrar el pago y obtener el ID
        Integer idPago = pagoProcedureRepository.registrarPago(
                reserva.getTotal(),
                metodoPago,
                LocalDateTime.now()
        );

        // Validación por seguridad
        if (idPago == null) {
            throw new RuntimeException("❌ No se pudo registrar el pago. Verificá el monto o el trigger.");
        }

        System.out.println("✅ idPago generado correctamente: " + idPago);

        // Asignar ese ID como idReserva al DTO
        reserva.setIdReserva(idPago);

        // Paso 2: Generar el CSV de asientos (ej: "5,8,12")
        String asientosCSV = reserva.getAsientosSeleccionados().stream()
                .map(a -> String.valueOf(a.getId_asiento()))
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        if (asientosCSV.isEmpty()) {
            throw new RuntimeException("❌ No se seleccionaron asientos para registrar.");
        }

        // Paso 3: Registrar los boletos
        pagoProcedureRepository.registrarBoletos(
            reserva.getIdFuncion(),
            1, // idUsuario simulado
            idPago,
            1, // TODO: Reemplazar con el ID real de TipoPrecio
            "confirmado",
            asientosCSV
        );

        // Paso 4: Generar y retornar PDF
        return pdfGenerator.generarPDFReserva(reserva, nombre, apellido, correo, metodoPago);
    }
}
