// DTO para representar o ranking de doadores
package com.ranieriiuri.eclesial_arrecadacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RankingDoadorDTO {
    private UUID doadorId;
    private String nome;
    private long totalDoacoes;
    private int totalPecas;
}
