package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String nome;
    private String email;
    private String cargo;
    private String fotoPerfil;
    private UUID igrejaId;
    private String igrejaNome;
}
