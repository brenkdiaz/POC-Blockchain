package com.example.blockchainremito.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Remito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private String empresaSolicitante;

    private String empresaProveedora;

    private String descripcionTrabajo;

    private String tecnicoAsignado;

    private String duracionEstimada;

    private String hash;


    //Nuevos atributos
    private String txHash;

    private Integer blockNumber;

    private Instant timestamp;


    @Override
    public String toString() {
        return fecha +
                empresaProveedora +
                descripcionTrabajo +
                duracionEstimada +
                empresaSolicitante;
    }

}