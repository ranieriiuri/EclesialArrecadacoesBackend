package com.ranieriiuri.eclesial_arrecadacoes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarSenhaRequest {

    @NotBlank(message = "A senha atual é obrigatória.")
    private String senhaAtual;

    @NotBlank(message = "A nova senha é obrigatória.")
    private String novaSenha;

}
