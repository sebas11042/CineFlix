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

    @Autowired
    private EmailService emailService;

    public void procesarPago(ReservaDTO reserva, String nombre, String apellido, String correo, String metodoPago) {

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
                "activo", // estado del boleto
                asientosCSV
        );

        // Paso 4: Generar el PDF
        byte[] pdf = pdfGenerator.generarPDFReserva(reserva, nombre, apellido, correo, metodoPago);

        // Paso 5: Enviar el PDF al correo del cliente
        emailService.enviarCorreoConPDF(correo, "Confirmación de reserva CineFlix", pdf);
    }
}
