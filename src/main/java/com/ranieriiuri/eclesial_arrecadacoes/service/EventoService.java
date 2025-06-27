package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Evento;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.EventoRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.CriarEventoRequest;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final IgrejaRepository igrejaRepository;

    public EventoService(EventoRepository eventoRepository, IgrejaRepository igrejaRepository) {
        this.eventoRepository = eventoRepository;
        this.igrejaRepository = igrejaRepository;
    }

    public Evento criarEvento(CriarEventoRequest request) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) {
            throw new IllegalStateException("Igreja não encontrada no contexto.");
        }

        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada"));

        Evento evento = new Evento();
        evento.setTipo(request.getTipo());  // String agora
        evento.setDescricao(request.getDescricao());
        evento.setStatus("planejando"); // default string
        evento.setCriadoEm(LocalDateTime.now());
        evento.setIgreja(igreja);

        return eventoRepository.save(evento);
    }

    public Evento buscarPorId(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com ID: " + id));
    }

    public Evento atualizarDataInicio(UUID eventoId, LocalDate novaDataInicio) {
        Evento evento = buscarPorId(eventoId);

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para modificar este evento.");
        }

        if (!"planejando".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Só é possível alterar a data de início se o evento estiver no status 'planejando'.");
        }

        if (novaDataInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A nova data de início não pode ser no passado.");
        }

        evento.setDataInicio(novaDataInicio);
        return eventoRepository.save(evento);
    }

    public Evento iniciarEvento(UUID eventoId) {
        Evento evento = buscarPorId(eventoId);

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para iniciar este evento.");
        }

        if (!"planejando".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Só é possível iniciar eventos com status 'planejando'.");
        }

        evento.setStatus("em andamento");
        evento.setDataInicio(LocalDate.now());
        return eventoRepository.save(evento);
    }

    public Evento finalizarEvento(UUID eventoId) {
        Evento evento = buscarPorId(eventoId);

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para finalizar este evento.");
        }

        if (!"em andamento".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Só é possível finalizar eventos que estão 'em andamento'.");
        }

        evento.setStatus("finalizado");
        evento.setDataFim(LocalDate.now());
        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventosDaIgrejaAtual() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return eventoRepository.findAllByIgrejaId(igrejaId);
    }

    public void excluirEvento(UUID id) {
        Evento evento = buscarPorId(id);

        if ("finalizado".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Não é possível excluir um evento já finalizado.");
        }

        eventoRepository.delete(evento);
    }
}
