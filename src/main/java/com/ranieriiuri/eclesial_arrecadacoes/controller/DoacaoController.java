package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RankingDoadorDTO;
import com.ranieriiuri.eclesial_arrecadacoes.service.DoacaoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/donations")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    // 🔹 Registrar uma nova doação
    @PostMapping
    public ResponseEntity<Doacao> registrarDoacao(@Valid @RequestBody Doacao doacao) {
        Doacao salva = doacaoService.salvarDoacao(doacao);
        return ResponseEntity.ok(salva);
    }

    // 🔹 Listar todas as doações da igreja atual
    @GetMapping
    public ResponseEntity<List<Doacao>> listarTodasDoacoes() {
        return ResponseEntity.ok(doacaoService.listarPorIgrejaAtual());
    }

    // 🔹 Listar doações por doador
    @GetMapping("/doador/{doadorId}")
    public ResponseEntity<List<Doacao>> listarDoacoesPorDoador(@PathVariable UUID doadorId) {
        return ResponseEntity.ok(doacaoService.listarPorDoador(doadorId));
    }

    // 🔹 Buscar uma doação por ID
    @GetMapping("/{id}")
    public ResponseEntity<Doacao> buscarDoacaoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(doacaoService.buscarPorId(id));
    }

    // 🔹 Obter ranking de doadores da igreja atual
    @GetMapping("/ranking-total")
    public ResponseEntity<List<RankingDoadorDTO>> rankingDoadores() {
        List<RankingDoadorDTO> ranking = doacaoService.obterRankingDoadores();
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/donors/ranking/range")
    public ResponseEntity<List<RankingDoadorDTO>> obterRankingDoadores(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        try {
            List<RankingDoadorDTO> ranking = doacaoService.gerarRankingPorPeriodo(inicio, fim);
            return ResponseEntity.ok(ranking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // logar erro aqui
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
