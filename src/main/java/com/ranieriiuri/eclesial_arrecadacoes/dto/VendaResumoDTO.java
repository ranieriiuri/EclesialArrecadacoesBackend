package com.ranieriiuri.eclesial_arrecadacoes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record VendaResumoDTO(
        UUID id,
        String pecaNome,
        UUID eventoId,
        String comprador,
        int quantidade,
        BigDecimal valorArrecadado,
        LocalDateTime dataVenda
) {}
