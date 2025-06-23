package com.ranieriiuri.eclesial_arrecadacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioVendasDTO {
    private long totalVendas;
    private int totalPecasVendidas;
    private BigDecimal valorTotalArrecadado;
}

