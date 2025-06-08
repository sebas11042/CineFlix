package com.cineflix.controller;

import com.cineflix.dto.ReservaDTO;
import com.cineflix.entity.Funcion;
import com.cineflix.entity.TipoPrecio;
import com.cineflix.service.FuncionService;
import com.cineflix.service.TipoPrecioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/reserva")
@SessionAttributes("reserva")
public class ReservaController {

    @Autowired
    private FuncionService funcionService;

    @Autowired
    private TipoPrecioService tipoPrecioService;

    // Paso 1: Mostrar formulario de selección de boletos
    @GetMapping("/paso1/{idFuncion}")
    public String mostrarPaso1(@PathVariable Integer idFuncion, Model model) {
        Funcion funcion = funcionService.obtenerPorId(idFuncion);
        List<TipoPrecio> tiposPrecio = tipoPrecioService.obtenerTiposPorEdad(); // solo por edad, ignora formato

        ReservaDTO reserva = new ReservaDTO();
        reserva.setIdFuncion(funcion.getIdFuncion());
        reserva.setPelicula(funcion.getPelicula());
        reserva.setSala(funcion.getSala());
        reserva.setFecha(funcion.getFecha());
        reserva.setHora(funcion.getHora());

        model.addAttribute("reserva", reserva);
        model.addAttribute("tiposPrecio", tiposPrecio);
        return "reserva/paso1";
    }

    // Paso 1: Procesar boletos seleccionados y guardar en sesión
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
                        BigDecimal precioUnitario = tipo.getPrecio().add(BigDecimal.valueOf(450)); // cargo de servicio
                        totalFinal = totalFinal.add(precioUnitario.multiply(BigDecimal.valueOf(cantidad)));
                    }
                } catch (NumberFormatException e) {
                    // ignorar errores de parseo
                }
            }
        }

        if (totalBoletos == 0) {
            model.addAttribute("error", "Debes seleccionar al menos un boleto.");
            model.addAttribute("tiposPrecio", tipoPrecioService.obtenerTiposPorEdad());
            return "reserva/paso1";
        }

        reserva.setBoletosSeleccionados(boletosSeleccionados);
        reserva.setTotal(totalFinal);

        return "redirect:/reserva/paso2";
    }

    // Paso 2: aún no implementado, lo veremos después
}
