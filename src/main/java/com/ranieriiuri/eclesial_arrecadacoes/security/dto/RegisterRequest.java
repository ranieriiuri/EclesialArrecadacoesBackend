package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    // Campo opcional, sem validação obrigatória
    private String cargo;

    @Valid
    @NotNull(message = "Endereço é obrigatório")
    private EnderecoRequest endereco;

    @Valid
    @NotNull(message = "Igreja é obrigatória")
    private IgrejaRequest igreja;
}
