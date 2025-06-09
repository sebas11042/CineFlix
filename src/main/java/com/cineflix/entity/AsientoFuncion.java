package com.cineflix.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "AsientoFuncion")
public class AsientoFuncion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsientoFuncion;

    @ManyToOne
    @JoinColumn(name = "id_funcion")
    private Funcion funcion;

    @ManyToOne
    @JoinColumn(name = "id_asiento")
    private Asiento asiento;

    private boolean ocupado;

    // Getters y setters
}
