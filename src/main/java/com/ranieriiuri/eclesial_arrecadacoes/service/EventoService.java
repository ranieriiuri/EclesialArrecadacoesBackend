package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.StatusEvento;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Evento;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.EventoRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
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

    public Evento criarEvento(Evento evento) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) {
            throw new IllegalStateException("Igreja não encontrada no contexto.");
        }

        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada"));

        evento.setIgreja(igreja);
        evento.setStatus(StatusEvento.PLANEJANDO);
        evento.setCriadoEm(LocalDateTime.now());

        return eventoRepository.save(evento);
    }

    public Evento atualizarDataInicio(UUID eventoId, LocalDate novaDataInicio) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        // Verifica se pertence à igreja atual
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para modificar este evento.");
        }

        // Verifica se o status permite alteração
        if (evento.getStatus() != StatusEvento.PLANEJANDO) {
            throw new IllegalStateException("Só é possível alterar a data de início se o evento estiver no status 'PLANEJANDO'.");
        }

        // Verifica se a nova data de início é anterior à data atual do sistema
        if (novaDataInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A nova data de início não pode ser no passado.");
        }

        evento.setDataInicio(novaDataInicio);
        return eventoRepository.save(evento);
    }

    public Evento iniciarEvento(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para iniciar este evento.");
        }

        if (evento.getStatus() != StatusEvento.PLANEJANDO) {
            throw new IllegalStateException("Só é possível iniciar eventos com status 'PLANEJANDO'.");
        }

        evento.setStatus(StatusEvento.EM_ANDAMENTO);
        return eventoRepository.save(evento);
    }

    public Evento finalizarEvento(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para finalizar este evento.");
        }

        if (evento.getStatus() != StatusEvento.EM_ANDAMENTO) {
            throw new IllegalStateException("Só é possível finalizar eventos que estão 'EM_ANDAMENTO'.");
        }

        evento.setStatus(StatusEvento.FINALIZADO);
        evento.setDataFim(LocalDate.now());
        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventosDaIgrejaAtual() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return eventoRepository.findAllByIgrejaId(igrejaId);
    }
}
