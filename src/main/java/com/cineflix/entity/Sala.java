package com.cineflix.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Sala")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala") 
    private int idSala;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false)
    private int filas;

    @Column(nullable = false)
    private int columnas;
}
