package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IgrejaRequest {
    private String nome;
    private String cnpj;
    private String cidade;
    private String estado;
}
