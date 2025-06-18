package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IgrejaRequest {

    @NotBlank(message = "Nome da igreja é obrigatório")
    private String nome;

    private String cnpj; // opcional, pois algumas igrejas podem não ter

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    private String estado;

}
