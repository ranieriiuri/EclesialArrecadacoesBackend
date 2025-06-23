package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String nome;
    private String email;
    private UUID igrejaId;
    private String cargo;

    public UserResponse(UUID id, String nome, String email, UUID igrejaId, String cargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.igrejaId = igrejaId;
        this.cargo = cargo;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public UUID getIgrejaId() {
        return igrejaId;
    }

    public String getCargo() {
        return cargo;
    }
}
