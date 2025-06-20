package com.ranieriiuri.eclesial_arrecadacoes.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarVendaRequestDTO {

    @NotNull(message = "O ID da peça é obrigatório.")
    private UUID pecaId;

    @NotNull(message = "O ID do evento é obrigatório.")
    private UUID eventoId;

    @Positive(message = "A quantidade vendida deve ser positiva.")
    private int quantidadeVendida;

    @NotBlank(message = "O nome do comprador é obrigatório.")
    private String comprador;
}
