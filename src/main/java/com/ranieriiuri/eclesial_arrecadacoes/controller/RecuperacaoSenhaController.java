package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.dto.RedefinirSenhaRequest;
import com.ranieriiuri.eclesial_arrecadacoes.dto.SolicitarRecuperacaoSenhaRequest;
import com.ranieriiuri.eclesial_arrecadacoes.service.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recuperacao-senha")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(RecuperacaoSenhaService recuperacaoSenhaService) {
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    // 🔹 Iniciar processo (usuário informa o e-mail)
    @PostMapping("/solicitar")
    public ResponseEntity<Void> solicitar(@Valid @RequestBody SolicitarRecuperacaoSenhaRequest request) {
        recuperacaoSenhaService.solicitarRecuperacaoSenha(request.getEmail());
        return ResponseEntity.ok().build();
    }

    // 🔹 Redefinir senha com token
    @PostMapping("/redefinir")
    public ResponseEntity<Void> redefinir(@Valid @RequestBody RedefinirSenhaRequest request) {
        recuperacaoSenhaService.redefinirSenha(request.getToken(), request.getNovaSenha());
        return ResponseEntity.ok().build();
    }
}
