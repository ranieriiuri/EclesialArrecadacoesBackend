package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Evento;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.dto.CriarEventoRequest;
import com.ranieriiuri.eclesial_arrecadacoes.dto.CriarEventoResponse;
import com.ranieriiuri.eclesial_arrecadacoes.service.EventoService;
import com.ranieriiuri.eclesial_arrecadacoes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/events")
public class EventoController {

    private final EventoService eventoService;
    private final UsuarioService usuarioService;

    public EventoController(EventoService eventoService, UsuarioService usuarioService) {
        this.eventoService = eventoService;
        this.usuarioService = usuarioService;
    }

    // 🔹 Criar novo evento
    @PostMapping("/new")
    public ResponseEntity<CriarEventoResponse> criarEvento(@Valid @RequestBody CriarEventoRequest request) {
        Evento novo = eventoService.criarEvento(request);
        CriarEventoResponse response = mapToResponse(novo);
        return ResponseEntity.ok(response);
    }

    // Metodo interno para montar o dto de resposta do evento criado
    private CriarEventoResponse mapToResponse(Evento evento) {
        CriarEventoResponse.UsuarioResumo usuarioResumo = null;
        if (evento.getCriadoPor() != null) {
            usuarioResumo = CriarEventoResponse.UsuarioResumo.builder()
                    .id(evento.getCriadoPor().getId())
                    .nome(evento.getCriadoPor().getNome())
                    .build();
        }

        return CriarEventoResponse.builder()
                .id(evento.getId())
                .tipo(evento.getTipo())
                .descricao(evento.getDescricao())
                .dataInicio(evento.getDataInicio())
                .dataFim(evento.getDataFim())
                .status(evento.getStatus())
                .criadoPor(usuarioResumo)
                .criadoEm(evento.getCriadoEm())
                .build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscarPorId(@PathVariable UUID id) {
        Evento evento = eventoService.buscarPorId(id);
        return ResponseEntity.ok(evento);
    }

    // 🔹 Atualizar data de início de um evento específico
    @PatchMapping("/{id}/starting-date")
    public ResponseEntity<Evento> atualizarDataInicio(
            @PathVariable UUID id,
            @RequestParam("novaDataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate novaDataInicio) {
        Evento atualizado = eventoService.atualizarDataInicio(id, novaDataInicio);
        return ResponseEntity.ok(atualizado);
    }

    // 🔹 Iniciar evento (altera status para "em andamento")
    @PutMapping("/{id}/starting")
    public ResponseEntity<Evento> iniciarEvento(@PathVariable UUID id) {
        Evento evento = eventoService.iniciarEvento(id);
        return ResponseEntity.ok(evento);
    }

    // 🔹 Finalizar evento (altera status para "finalizado" e define dataFim)
    @PutMapping("/{id}/finishing")
    public ResponseEntity<Evento> finalizarEvento(@PathVariable UUID id) {
        Evento evento = eventoService.finalizarEvento(id);
        return ResponseEntity.ok(evento);
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> gerarRelatorioPdf(@PathVariable("id") UUID eventoId, Principal principal) {
        try {
            // Pega o email/login do usuário autenticado
            String email = principal.getName();

            // Busca usuário para obter igrejaId
            Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);

            UUID igrejaId = usuario.getIgreja().getId();

            // Gera o PDF
            byte[] pdfBytes = eventoService.generatePdfReport(igrejaId, eventoId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("relatorio-evento-" + eventoId + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            // Logue o erro aqui
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 🔹 Listar todos os eventos da igreja atual
    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        List<Evento> eventos = eventoService.listarEventosDaIgrejaAtual();
        return ResponseEntity.ok(eventos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirEvento(@PathVariable UUID id) {
        eventoService.excluirEvento(id);
        return ResponseEntity.ok("Evento excluído com sucesso.");
    }
}
