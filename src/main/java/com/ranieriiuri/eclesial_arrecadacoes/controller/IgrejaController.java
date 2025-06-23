package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.dto.IgrejaUpdateRequest;
import com.ranieriiuri.eclesial_arrecadacoes.service.IgrejaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/igreja")
public class IgrejaController {

    private final IgrejaService igrejaService;

    public IgrejaController(IgrejaService igrejaService) {
        this.igrejaService = igrejaService;
    }

    // Atualiza a igreja atual (com base no TenantContext)
    public ResponseEntity<Igreja> atualizarIgreja(@Valid @RequestBody IgrejaUpdateRequest request) {
        Igreja atualizada = igrejaService.atualizarIgreja(request);
        return ResponseEntity.ok(atualizada);
    }
}

