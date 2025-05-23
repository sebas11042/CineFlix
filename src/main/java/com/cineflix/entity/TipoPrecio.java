package com.cineflix.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TipoPrecio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoPrecio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_tipo_precio;

    @Column(name = "edad_tipo", nullable = false, length = 10)
    private String edadTipo;

    @Column(nullable = false, length = 10)
    private String formato;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

}
