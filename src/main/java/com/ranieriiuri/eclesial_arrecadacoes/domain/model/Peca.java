package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;

@Entity
public class Peca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String categoria;
    private int quantidade;

    @ManyToOne
    private Evento evento;

    private String observacoes;

    // Getters, Setters, Construtores
}
