package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doador;
import com.ranieriiuri.eclesial_arrecadacoes.service.DoadorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doadores")
public class DoadorController {

    private final DoadorService doadorService;

    public DoadorController(DoadorService doadorService) {
        this.doadorService = doadorService;
    }

    // 🔹 Criar novo doador manualmente
    @PostMapping
    public ResponseEntity<Doador> criarDoador(@Valid @RequestBody Doador doador) {
        return ResponseEntity.ok(doadorService.criarDoador(doador));
    }

    // 🔹 Listar todos os doadores da igreja atual
    @GetMapping
    public ResponseEntity<List<Doador>> listarDoadores() {
        return ResponseEntity.ok(doadorService.listarDoadoresDaIgrejaAtual());
    }

    // 🔹 Atualizar dados do doador
    @PutMapping("/{id}")
    public ResponseEntity<Doador> atualizarDoador(@PathVariable UUID id, @Valid @RequestBody Doador doador) {
        return ResponseEntity.ok(doadorService.atualizarDoador(id, doador));
    }

    // 🔹 Excluir doador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDoador(@PathVariable UUID id) {
        doadorService.excluirDoador(id);
        return ResponseEntity.noContent().build();
    }
}
