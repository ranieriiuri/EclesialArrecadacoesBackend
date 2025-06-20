package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.service.IgrejaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/igreja")
public class IgrejaController {

    private final IgrejaService igrejaService;

    public IgrejaController(IgrejaService igrejaService) {
        this.igrejaService = igrejaService;
    }

    // Atualiza a igreja atual (com base no TenantContext)
    @PutMapping("/atualizar")
    public ResponseEntity<Igreja> atualizarIgreja(@RequestBody Igreja nova) {
        Igreja atualizada = igrejaService.atualizarIgreja(nova);
        return ResponseEntity.ok(atualizada);
    }
}

