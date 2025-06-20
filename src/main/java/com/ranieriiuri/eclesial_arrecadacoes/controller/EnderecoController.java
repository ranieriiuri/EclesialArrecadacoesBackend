package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Endereco;
import com.ranieriiuri.eclesial_arrecadacoes.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    // 🔄 Atualizar endereço
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable UUID id, @Valid @RequestBody Endereco novoEndereco) {
        Endereco atualizado = enderecoService.atualizarEndereco(id, novoEndereco);
        return ResponseEntity.ok(atualizado);
    }
}
