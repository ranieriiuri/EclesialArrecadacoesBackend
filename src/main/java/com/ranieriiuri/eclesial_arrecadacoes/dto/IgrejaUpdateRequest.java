package com.ranieriiuri.eclesial_arrecadacoes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IgrejaUpdateRequest {
    @NotBlank
    private String nome;

    private String cnpj;

    private String cidade;
    private String estado;

    // getters e setters
}
