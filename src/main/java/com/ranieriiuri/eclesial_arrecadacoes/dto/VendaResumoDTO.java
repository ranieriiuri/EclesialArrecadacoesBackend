package com.ranieriiuri.eclesial_arrecadacoes.dto;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.TipoEvento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record VendaResumoDTO(
        UUID id,
        String nomePeca,
        TipoEvento tipoEvento,
        String comprador,
        int quantidade,
        BigDecimal valorArrecadado,
        LocalDateTime dataVenda
) {}
