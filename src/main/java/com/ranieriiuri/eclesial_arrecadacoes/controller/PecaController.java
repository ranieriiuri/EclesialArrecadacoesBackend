package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.CategoriaPeca;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import com.ranieriiuri.eclesial_arrecadacoes.service.PecaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaService pecaService;

    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    // 🔹 Registrar nova peça (com doador incluso, se houver)
    @PostMapping
    public ResponseEntity<Peca> registrarPeca(@Valid @RequestBody Peca novaPeca) {
        return ResponseEntity.ok(pecaService.registrarNovaPeca(novaPeca));
    }

    // 🔹 Buscar peça por ID
    @GetMapping("/{id}")
    public ResponseEntity<Peca> buscarPecaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pecaService.buscarPorId(id));
    }

    // 🔹 Buscar por nome (com filtro por igreja)
    @GetMapping("/buscar")
    public ResponseEntity<List<Peca>> buscarPecaPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(pecaService.buscarPorNome(nome));
    }

    // 🔹 Atualizar dados da peça (exceto quantidade, que tem endpoint separado)
    @PutMapping("/{id}")
    public ResponseEntity<Peca> atualizarPeca(@PathVariable UUID id,
                                              @Valid @RequestBody Peca dadosAtualizados) {
        Peca pecaAtualizada = pecaService.atualizarPeca(id, dadosAtualizados);
        return ResponseEntity.ok(pecaAtualizada);
    }

    // 🔹 Atualizar quantidade (e disponibilidade, se necessário)
    @PatchMapping("/{id}/quantidade")
    public ResponseEntity<Peca> atualizarQuantidadePeca(@PathVariable UUID id, @RequestParam int novaQuantidade) {
        return ResponseEntity.ok(pecaService.atualizarQuantidade(id, novaQuantidade));
    }

    // 🔹 Buscar todas as peças da igreja atual
    @GetMapping
    public ResponseEntity<List<Peca>> listarTodas() {
        return ResponseEntity.ok(pecaService.listarTodas());
    }

    // 🔹 Buscar todas as peças disponíveis
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Peca>> listarPecasDisponiveis() {
        return ResponseEntity.ok(pecaService.listarDisponiveis());
    }

    // 🔹 Buscar peças por categoria
    @GetMapping("/categoria")
    public ResponseEntity<List<Peca>> listarPecasPorCategoria(@RequestParam CategoriaPeca categoria) {
        return ResponseEntity.ok(pecaService.listarPorCategoria(categoria));
    }

    // 🔹 Excluir uma peça
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirPeca(@PathVariable UUID id) {
        pecaService.excluirPeca(id);
        return ResponseEntity.ok("Peça excluída com sucesso.");
    }
}