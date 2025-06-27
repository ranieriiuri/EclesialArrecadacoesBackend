package com.ranieriiuri.eclesial_arrecadacoes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriarEventoRequest {

    @NotBlank
    private String tipo;

    private String descricao;
}
