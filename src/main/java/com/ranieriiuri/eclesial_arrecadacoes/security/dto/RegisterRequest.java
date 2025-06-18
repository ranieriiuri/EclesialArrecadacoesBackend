package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String nome;
    private String email;
    private String senha;
    private String cargo;
    private EnderecoRequest endereco;
    private IgrejaRequest igreja; // Esse modelo é pq o endereco e a igreja são criados ao criar o usuario
}
