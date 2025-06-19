package com.ranieriiuri.eclesial_arrecadacoes.dto;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.CategoriaPeca;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class NovaPecaComRegistroDoacaoRequest {
    // Dados da peça
    private String nomePeca;
    private String cor;
    private CategoriaPeca categoria;
    private int quantidade;
    private BigDecimal preco;
    private String observacoes;

    // Doador existente (opcional)
    private UUID doadorId;

    // Dados para novo doador (caso doadorId seja null)
    private String nomeDoador;
    private String contato;
    private String observacoesDoador;
}
