package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import com.ranieriiuri.eclesial_arrecadacoes.service.DoacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    // 🔹 Listar todas as doações da igreja atual
    @GetMapping
    public ResponseEntity<List<Doacao>> listarPorIgrejaAtual() {
        return ResponseEntity.ok(doacaoService.listarPorIgrejaAtual());
    }

    // 🔹 Listar doações por doador
    @GetMapping("/doador/{doadorId}")
    public ResponseEntity<List<Doacao>> listarPorDoador(@PathVariable UUID doadorId) {
        return ResponseEntity.ok(doacaoService.listarPorDoador(doadorId));
    }

    // 🔹 Listar doações por peça
    @GetMapping("/peca/{pecaId}")
    public ResponseEntity<List<Doacao>> listarPorPeca(@PathVariable UUID pecaId) {
        return ResponseEntity.ok(doacaoService.listarPorPeca(pecaId));
    }

    // 🔹 Buscar uma doação por ID
    @GetMapping("/{id}")
    public ResponseEntity<Doacao> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(doacaoService.buscarPorId(id));
    }
}
