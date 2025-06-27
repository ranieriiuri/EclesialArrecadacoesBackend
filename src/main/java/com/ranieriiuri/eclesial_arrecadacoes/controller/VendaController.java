package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Venda;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RegistrarVendaRequestDTO;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RelatorioVendasDTO;
import com.ranieriiuri.eclesial_arrecadacoes.dto.VendaResumoDTO;
import com.ranieriiuri.eclesial_arrecadacoes.service.VendaService;
import com.ranieriiuri.eclesial_arrecadacoes.service.facade.VendaFacadeService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/sales")
public class VendaController {

    private final VendaFacadeService vendaFacadeService;
    private final VendaService vendaService;

    public VendaController(VendaFacadeService vendaFacadeService, VendaService vendaService) {
        this.vendaFacadeService = vendaFacadeService;
        this.vendaService = vendaService;
    }

    // 🔹 Registrar venda (via facade), anexando id da peça, do evento, logica de mudança na peça e etc...
    @PostMapping("/new")
    public ResponseEntity<Venda> registrarVenda(@Valid @RequestBody RegistrarVendaRequestDTO request) {
        Venda venda = vendaFacadeService.registrarVenda(
                request.getPecaId(),
                request.getEventoId(),
                request.getQuantidadeVendida(),
                request.getComprador()
        );
        return ResponseEntity.ok(venda);
    }

    // 🔹 Relatórios por período
    @GetMapping("/per-range")
    public ResponseEntity<List<Venda>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return ResponseEntity.ok(vendaService.listarPorPeriodo(inicio, fim));
    }

    // 🔹 Relatórios por evento
    @GetMapping("/event/{eventoId}")
    public ResponseEntity<List<Venda>> listarPorEvento(@PathVariable UUID eventoId) {
        return ResponseEntity.ok(vendaService.listarPorEvento(eventoId));
    }

    // 🔹 Listar todas as vendas da igreja logada
    @GetMapping
    public ResponseEntity<List<VendaResumoDTO>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    //  Geradores de relatórios:

    @GetMapping("/reports/range")
    public ResponseEntity<RelatorioVendasDTO> relatorioPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return ResponseEntity.ok(vendaService.gerarRelatorioPorPeriodo(inicio, fim));
    }

    @GetMapping("/reports")
    public ResponseEntity<RelatorioVendasDTO> relatorioTotal() {
        return ResponseEntity.ok(vendaService.gerarRelatorioTotal());
    }

    @GetMapping("/reports/per-event/{eventId}")
    public ResponseEntity<RelatorioVendasDTO> relatorioPorEvento(@PathVariable UUID eventoId) {
        return ResponseEntity.ok(vendaService.gerarRelatorioPorEvento(eventoId));
    }

}