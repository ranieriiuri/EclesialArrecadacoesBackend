package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.event.model.PecaCriadaEvent;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.PecaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PecaService(PecaRepository pecaRepository, ApplicationEventPublisher eventPublisher) {
        this.pecaRepository = pecaRepository;
        this.eventPublisher = eventPublisher;
    }

    public Peca registrarNovaPeca(Peca novaPeca) {
        Peca pecaSalva = pecaRepository.save(novaPeca);
        eventPublisher.publishEvent(new PecaCriadaEvent(this, pecaSalva));
        return pecaSalva;
    }
}

