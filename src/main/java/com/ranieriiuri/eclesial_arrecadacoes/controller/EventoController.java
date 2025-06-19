package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Evento;
import com.ranieriiuri.eclesial_arrecadacoes.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    // 🔹 Criar novo evento
    @PostMapping
    public ResponseEntity<Evento> criarEvento(@Valid @RequestBody Evento evento) {
        Evento novo = eventoService.criarEvento(evento);
        return ResponseEntity.ok(novo);
    }

    // 🔹 Atualizar data de início de um evento específico
    @PatchMapping("/{id}/data-inicio")
    public ResponseEntity<Evento> atualizarDataInicio(
            @PathVariable UUID id,
            @RequestParam("novaDataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate novaDataInicio) {
        Evento atualizado = eventoService.atualizarDataInicio(id, novaDataInicio);
        return ResponseEntity.ok(atualizado);
    }

    // 🔹 Iniciar evento (altera status para EM_ANDAMENTO)
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Evento> iniciarEvento(@PathVariable UUID id) {
        Evento evento = eventoService.iniciarEvento(id);
        return ResponseEntity.ok(evento);
    }

    // 🔹 Finalizar evento (altera status para FINALIZADO e define data_fim)
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Evento> finalizarEvento(@PathVariable UUID id) {
        Evento evento = eventoService.finalizarEvento(id);
        return ResponseEntity.ok(evento);
    }

    // 🔹 Listar todos os eventos da igreja atual (com base no tenant)
    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        List<Evento> eventos = eventoService.listarEventosDaIgrejaAtual();
        return ResponseEntity.ok(eventos);
    }
}
