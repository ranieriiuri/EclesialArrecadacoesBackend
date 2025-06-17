package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String nome;
    private String email;
    private String senha;
    private String cargo;
    private Long igrejaId;
    private Long enderecoId;
}
