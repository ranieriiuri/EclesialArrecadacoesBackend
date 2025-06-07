package com.ranieriiuri.eclesial_arrecadacoes.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private String cargo;
    private String cep;
    private String logradouro;
    private String cidade;
    private String estado;
}

