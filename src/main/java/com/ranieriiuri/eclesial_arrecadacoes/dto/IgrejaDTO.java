package com.ranieriiuri.eclesial_arrecadacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IgrejaDTO {
    private UUID id;
    private String nome;
    private String cnpj;
    private String cidade;
    private String estado;
}
