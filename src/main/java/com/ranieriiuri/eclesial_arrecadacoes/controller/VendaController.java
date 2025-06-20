package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Venda;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RegistrarVendaRequestDTO;
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

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaFacadeService vendaFacadeService;
    private final VendaService vendaService;

    public VendaController(VendaFacadeService vendaFacadeService, VendaService vendaService) {
        this.vendaFacadeService = vendaFacadeService;
        this.vendaService = vendaService;
    }

    // 🔹 Registrar venda (via facade), anexando id da peça, do evento, logica de mudança na peça e etc...
    @PostMapping
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
    @GetMapping("/por-periodo")
    public ResponseEntity<List<Venda>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return ResponseEntity.ok(vendaService.listarPorPeriodo(inicio, fim));
    }

    // 🔹 Relatórios por evento
    @GetMapping("/por-evento/{eventoId}")
    public ResponseEntity<List<Venda>> listarPorEvento(@PathVariable UUID eventoId) {
        return ResponseEntity.ok(vendaService.listarPorEvento(eventoId));
    }

    // 🔹 Listar todas as vendas da igreja logada
    @GetMapping
    public ResponseEntity<List<VendaResumoDTO>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }
}
