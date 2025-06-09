package com.cineflix.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.cineflix.dto.ReservaDTO;
import com.cineflix.entity.Asiento;
import com.cineflix.entity.Funcion;
import com.cineflix.entity.TipoPrecio;
import com.cineflix.service.AsientoService;
import com.cineflix.service.FuncionService;
import com.cineflix.service.TipoPrecioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
                    // ignorar errores
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

    // Paso 2: Mostrar asientos de la sala
    @GetMapping("/paso2")
    public String mostrarPaso2(@ModelAttribute("reserva") ReservaDTO reserva, Model model) {
        // Obtener todos los asientos de la sala
        List<Asiento> asientosSala = asientoService.obtenerAsientosPorSala(reserva.getSala());
        asientosSala.sort(Comparator.comparing(Asiento::getFila).thenComparing(Asiento::getColumna));

        // Obtener los asientos ocupados para la función
        List<Asiento> asientosOcupados = asientoService.obtenerAsientosOcupados(reserva.getSala().getIdSala());

        int totalBoletos = reserva.getBoletosSeleccionados()
                .values().stream().mapToInt(Integer::intValue).sum();

        // Generar lista de letras para las filas
        List<String> letrasFilas = new java.util.ArrayList<>();
        for (int i = 0; i < reserva.getSala().getFilas(); i++) {
            letrasFilas.add(String.valueOf((char) ('A' + i)));
        }
        model.addAttribute("letrasFilas", letrasFilas);

        model.addAttribute("asientos", asientosSala); // Todos los asientos de la sala
        model.addAttribute("ocupados", asientosOcupados.stream()
                .map(Asiento::getId_asiento)
                .collect(Collectors.toList()));

        model.addAttribute("totalBoletos", totalBoletos);
        model.addAttribute("filas", reserva.getSala().getFilas());
        model.addAttribute("ocupadosIds",
                asientosOcupados.stream().map(Asiento::getId_asiento).collect(Collectors.toList()));
        return "reserva/paso2";
    }

    // Paso 2: Procesar selección de asientos
    @PostMapping("/paso2")
    public String procesarPaso2(@RequestParam("asientos") List<Integer> idsAsientosSeleccionados,
            @ModelAttribute("reserva") ReservaDTO reserva,
            Model model) {

        int totalBoletos = reserva.getBoletosSeleccionados()
                .values().stream().mapToInt(Integer::intValue).sum();

        if (idsAsientosSeleccionados.size() != totalBoletos) {
            model.addAttribute("error", "Debes seleccionar exactamente " + totalBoletos + " asientos.");
            List<Asiento> asientosSala = asientoService.obtenerAsientosPorSala(reserva.getSala());
            asientosSala.sort(Comparator.comparing(Asiento::getFila).thenComparing(Asiento::getColumna));
            model.addAttribute("asientos", asientosSala);
            model.addAttribute("ocupados", asientoService.obtenerAsientosOcupados(reserva.getSala().getIdSala()));
            model.addAttribute("totalBoletos", totalBoletos);
            return "reserva/paso2";
        }

        List<Asiento> seleccionados = idsAsientosSeleccionados.stream().map(id -> {
            Asiento a = new Asiento();
            a.setId_asiento(id);
            return a;
        }).collect(Collectors.toList());

        reserva.setAsientosSeleccionados(seleccionados);
        return "redirect:/reserva/paso3";
    }
}
