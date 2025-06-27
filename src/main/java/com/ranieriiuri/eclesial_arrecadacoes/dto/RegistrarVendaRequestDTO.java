package com.ranieriiuri.eclesial_arrecadacoes.dto;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarVendaRequestDTO {

    @NotNull(message = "O id da peça é obrigatório.")
    private UUID pecaId;

    @NotNull(message = "O ID do evento é obrigatório.")
    private UUID eventoId;

    @Positive(message = "A quantidade vendida deve ser positiva.")
    private int quantidadeVendida;

    // Campo opcional agora
    private String comprador;
}
