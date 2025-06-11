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

        // Paso 2: Generar el CSV de asientos (ej: "5,8,12")
        String asientosCSV = reserva.getAsientosSeleccionados().stream()
                .map(a -> String.valueOf(a.getId_asiento()))
                .reduce((a, b) -> a + "," + b)
                .orElse("");

// Paso 3: Registrar todos los boletos de una vez mediante el procedimiento
pagoProcedureRepository.registrarBoletos(
    reserva.getIdFuncion(),
    1, // idUsuario temporal (simulado por ahora)
    idPago,
    1, // idTipoPrecio temporal
    "confirmado", // ✅ este valor debe coincidir con los permitidos en la tabla Boleto
    asientosCSV
);


        // Paso 4: Generar el PDF y retornarlo
        return pdfGenerator.generarPDFReserva(reserva, nombre, apellido, correo, metodoPago);
    }
}
