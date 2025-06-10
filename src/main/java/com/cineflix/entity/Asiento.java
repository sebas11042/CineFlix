package com.cineflix.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Asiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_asiento;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @Column(nullable = false, length = 1)
    private char fila;

    @Column(nullable = false)
    private int columna;

    @Column(nullable = false, length = 20)
    private String tipo; // tradicional, preferente, ruedas
}
