package com.ranieriiuri.eclesial_arrecadacoes.dto;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.CategoriaPeca;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NovaPecaComRegistroDoacaoRequest {

    // 🔹 Dados da peça
    @NotBlank(message = "O nome da peça é obrigatório")
    private String nomePeca;

    @NotBlank(message = "A cor da peça é obrigatória")
    private String cor;

    @NotNull(message = "A categoria da peça é obrigatória")
    private CategoriaPeca categoria;

    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private int quantidade;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    private String observacoes;

    // 🔹 Doador existente (opcional)
    private String doadorId;

    // 🔹 Dados para novo doador (caso doadorId seja null ou vazio)
    @Size(min = 2, message = "O nome do doador deve ter pelo menos 2 caracteres")
    private String nomeDoador;

    private String contato;

    private String observacoesDoador;
}
