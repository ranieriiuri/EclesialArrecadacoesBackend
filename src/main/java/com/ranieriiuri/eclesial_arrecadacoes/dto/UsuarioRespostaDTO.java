package com.ranieriiuri.eclesial_arrecadacoes.dto;

import com.ranieriiuri.eclesial_arrecadacoes.dto.EnderecoRespostaDTO;
import com.ranieriiuri.eclesial_arrecadacoes.dto.IgrejaRespostaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRespostaDTO {
    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private String cargo;
    private EnderecoRespostaDTO endereco;
    private String fotoPerfil;
    private IgrejaRespostaDTO igreja;
}
