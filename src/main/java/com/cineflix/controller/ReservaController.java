package com.cineflix.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.math.BigDecimal;

import com.cineflix.dto.ReservaDTO;
import com.cineflix.entity.Asiento;
import com.cineflix.entity.Funcion;
import com.cineflix.entity.TipoPrecio;
import com.cineflix.service.AsientoFuncionService;
import com.cineflix.service.AsientoService;
import com.cineflix.service.FuncionService;
import com.cineflix.service.PagoService;
import com.cineflix.service.ReservaTemporalService;
import com.cineflix.service.TipoPrecioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/reserva")
@SessionAttributes("reserva")
public class ReservaController {

    @Autowired
    private FuncionService funcionService;

    @Autowired
    private TipoPrecioService tipoPrecioService;

    @Autowired
    private AsientoService asientoService;

    @Autowired
    private AsientoFuncionService asientoFuncionService;

    @Autowired
    private ReservaTemporalService reservaTemporalService;

    @Autowired
    private PagoService pagoService;

    // Paso 1: Mostrar formulario de selección de boletos
    @GetMapping("/paso1/{idFuncion}")
    public String mostrarPaso1(@PathVariable Integer idFuncion, Model model) {
        Funcion funcion = funcionService.obtenerPorId(idFuncion);
        List<TipoPrecio> tiposPrecio = tipoPrecioService.obtenerTiposPorEdadYFormato(funcion.getFormato());

        ReservaDTO reserva = new ReservaDTO();
        reserva.setIdFuncion(funcion.getIdFuncion());
        reserva.setPelicula(funcion.getPelicula());
        reserva.setSala(funcion.getSala());
        reserva.setFecha(funcion.getFecha());
        reserva.setHora(funcion.getHora());
        reserva.setFormato(funcion.getFormato());

        model.addAttribute("reserva", reserva);
        model.addAttribute("tiposPrecio", tiposPrecio);
        return "reserva/paso1";
    }

    // Paso 1: Procesar boletos seleccionados
    @PostMapping("/paso1")
    public String procesarPaso1(@RequestParam Map<String, String> params,
                                 @ModelAttribute("reserva") ReservaDTO reserva,
                                 Model model) {

        Map<Integer, Integer> boletosSeleccionados = new HashMap<>();
        int totalBoletos = 0;
        BigDecimal totalFinal = BigDecimal.ZERO;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("boletos[")) {
                try {
                    Integer idTipoPrecio = Integer.parseInt(entry.getKey().replace("boletos[", "").replace("]", ""));
                    Integer cantidad = Integer.parseInt(entry.getValue());

                    if (cantidad > 0) {
                        boletosSeleccionados.put(idTipoPrecio, cantidad);
                        totalBoletos += cantidad;

                        TipoPrecio tipo = tipoPrecioService.obtenerPorId(idTipoPrecio);
                        BigDecimal precioUnitario = tipo.getPrecio().add(BigDecimal.valueOf(450));
                        totalFinal = totalFinal.add(precioUnitario.multiply(BigDecimal.valueOf(cantidad)));
                    }
                } catch (NumberFormatException e) {
                    // ignorar errores de conversión
                }
            }
        }

        if (totalBoletos == 0) {
            model.addAttribute("error", "Debes seleccionar al menos un boleto.");
            model.addAttribute("tiposPrecio", tipoPrecioService.obtenerTiposPorEdadYFormato(reserva.getFormato()));
            return "reserva/paso1";
        }

        if (totalBoletos > 10) {
            model.addAttribute("error", "No puedes seleccionar más de 10 boletos por transacción.");
            model.addAttribute("tiposPrecio", tipoPrecioService.obtenerTiposPorEdadYFormato(reserva.getFormato()));
            return "reserva/paso1";
        }

        reserva.setBoletosSeleccionados(boletosSeleccionados);
        reserva.setTotal(totalFinal);
        return "redirect:/reserva/paso2";
    }

    // Paso 2: Mostrar formulario de asientos
    @GetMapping("/paso2")
    public String mostrarPaso2(@ModelAttribute("reserva") ReservaDTO reserva, Model model) {

        // ✅ Limpiar asientos expirados antes de mostrar
        reservaTemporalService.liberarAsientosExpirados();

        List<Asiento> asientosSala = asientoService.obtenerAsientosPorSala(reserva.getSala());
        asientosSala.sort(Comparator.comparing(Asiento::getFila).thenComparing(Asiento::getColumna));

        List<Asiento> asientosOcupados = asientoFuncionService.obtenerAsientosOcupadosPorFuncion(reserva.getIdFuncion());
        int totalBoletos = reserva.getBoletosSeleccionados().values().stream().mapToInt(Integer::intValue).sum();

        List<String> letrasFilas = new ArrayList<>();
        for (int i = 0; i < reserva.getSala().getFilas(); i++) {
            letrasFilas.add(String.valueOf((char) ('A' + i)));
        }

        model.addAttribute("letrasFilas", letrasFilas);
        model.addAttribute("asientos", asientosSala);
        model.addAttribute("ocupados", asientosOcupados.stream().map(Asiento::getId_asiento).collect(Collectors.toList()));
        model.addAttribute("totalBoletos", totalBoletos);

        return "reserva/paso2";
    }

    // Paso 2: Procesar selección de asientos
    @PostMapping("/paso2")
    public String procesarPaso2(@RequestParam("asientos") List<Integer> idsAsientosSeleccionados,
                                @ModelAttribute("reserva") ReservaDTO reserva,
                                Model model) {

        int totalBoletos = reserva.getBoletosSeleccionados().values().stream().mapToInt(Integer::intValue).sum();

        if (idsAsientosSeleccionados.size() != totalBoletos) {
            model.addAttribute("error", "Debes seleccionar exactamente " + totalBoletos + " asientos.");
            List<Asiento> asientosSala = asientoService.obtenerAsientosPorSala(reserva.getSala());
            asientosSala.sort(Comparator.comparing(Asiento::getFila).thenComparing(Asiento::getColumna));
            model.addAttribute("asientos", asientosSala);
            model.addAttribute("ocupados", asientoFuncionService
                    .obtenerAsientosOcupadosPorFuncion(reserva.getIdFuncion())
                    .stream().map(Asiento::getId_asiento).collect(Collectors.toList()));
            model.addAttribute("totalBoletos", totalBoletos);
            return "reserva/paso2";
        }

        List<Asiento> seleccionados = idsAsientosSeleccionados.stream()
                .map(id -> asientoService.obtenerPorId(id))
                .collect(Collectors.toList());

        reserva.setAsientosSeleccionados(seleccionados);

        // ✅ Bloqueo temporal en BD
        String asientosCSV = seleccionados.stream()
                .map(a -> String.valueOf(a.getId_asiento()))
                .reduce((a, b) -> a + "," + b)
                .orElse("");
        reservaTemporalService.bloquearAsientosTemporal(reserva.getIdFuncion(), asientosCSV);

        return "redirect:/reserva/paso3";
    }

    // Paso 3: Mostrar formulario de pago
    @GetMapping("/paso3")
    public String mostrarPaso3(@ModelAttribute("reserva") ReservaDTO reserva, Model model) {
        if (reserva == null || reserva.getAsientosSeleccionados() == null || reserva.getAsientosSeleccionados().isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("reserva", reserva);
        return "reserva/paso3";
    }

    // Paso 3: Confirmar y generar PDF en línea
    @PostMapping("/confirmar")
    public ResponseEntity<byte[]> confirmarReserva(@ModelAttribute("reserva") ReservaDTO reserva,
                                                   @RequestParam String nombre,
                                                   @RequestParam String apellido,
                                                   @RequestParam String correo,
                                                   @RequestParam String metodoPago,
                                                   SessionStatus sessionStatus) {
        try {
            reserva.setMetodoPago(metodoPago);

            // Procesar pago y obtener PDF
            byte[] pdf = pagoService.procesarPago(reserva, nombre, apellido, correo, metodoPago);

            // Marcar asientos como ocupados
            String asientosCSV = reserva.getAsientosSeleccionados().stream()
                    .map(a -> String.valueOf(a.getId_asiento()))
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
            asientoFuncionService.ocuparAsientos(reserva.getIdFuncion(), asientosCSV);

            // ✅ Liberar asientos bloqueados temporalmente tras confirmar
            reservaTemporalService.liberarAsientosReservados(reserva.getIdFuncion(), asientosCSV);

            // Finalizar sesión
            sessionStatus.setComplete();

            // PDF de confirmación en línea
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=confirmacion_reserva.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
