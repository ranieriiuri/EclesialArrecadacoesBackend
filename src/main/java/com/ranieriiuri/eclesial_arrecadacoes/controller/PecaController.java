package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import com.ranieriiuri.eclesial_arrecadacoes.dto.NovaPecaComRegistroDoacaoRequest;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import com.ranieriiuri.eclesial_arrecadacoes.service.PecaService;
import com.ranieriiuri.eclesial_arrecadacoes.service.facade.CadastroPecaFacadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaService pecaService;
    private final CadastroPecaFacadeService cadastroPecaFacadeService;

    public PecaController(PecaService pecaService, CadastroPecaFacadeService cadastroPecaFacadeService) {
        this.pecaService = pecaService;
        this.cadastroPecaFacadeService = cadastroPecaFacadeService;
    }

    // Metodo utilizando um facade para cadastro da peca, doador e registro de doação automatico
    @PostMapping("/pecas-com-doacao")
    public ResponseEntity<Doacao> registrarPecaComDoacao(
            @Valid @RequestBody NovaPecaComRegistroDoacaoRequest request
    ) {
        Doacao doacao = cadastroPecaFacadeService.registrarPecaComDoacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(doacao);
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
    public ResponseEntity<List<Peca>> listarPecasPorCategoria(@RequestParam String categoria) {
        return ResponseEntity.ok(pecaService.listarPorCategoria(categoria));
    }

    // 🔹 Excluir uma peça
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirPeca(@PathVariable UUID id) {
        pecaService.excluirPeca(id);
        return ResponseEntity.ok("Peça excluída com sucesso.");
    }
}