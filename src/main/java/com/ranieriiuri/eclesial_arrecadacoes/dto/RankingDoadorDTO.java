package com.ranieriiuri.eclesial_arrecadacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingDoadorDTO {
    private String nome;
    private long totalDoacoes;
    private int totalPecas;
}
