package com.ranieriiuri.eclesial_arrecadacoes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NovaPecaComRegistroDoacaoRequest {

    @NotBlank(message = "O nome da peça é obrigatório")
    private String nome;

    // Se cor pode ser opcional, remova o NotBlank
    private String cor;

    @NotBlank(message = "A categoria da peça é obrigatória")
    private String categoria;

    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private int quantidade;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.00", inclusive = true, message = "O preço não pode ser negativo")
    private BigDecimal preco;

    private String observacoes;

    private String doadorId; // se UUID, troque para UUID doadorId;

    @Size(min = 2, message = "O nome do doador deve ter pelo menos 2 caracteres")
    private String nomeDoador;

    private String contato;

    private String observacoesDoador;
}
