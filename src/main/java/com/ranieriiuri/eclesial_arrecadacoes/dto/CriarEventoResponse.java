package com.ranieriiuri.eclesial_arrecadacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarEventoResponse {

    private UUID id;
    private String tipo;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;
    private UsuarioResumo criadoPor;
    private LocalDateTime criadoEm;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioResumo {
        private UUID id;
        private String nome;
    }
}
