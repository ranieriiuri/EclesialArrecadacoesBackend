package com.ranieriiuri.eclesial_arrecadacoes.service.facade;

import com.ranieriiuri.eclesial_arrecadacoes.domain.event.model.PecaCriadaEvent;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.*;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.*;
import com.ranieriiuri.eclesial_arrecadacoes.dto.NovaPecaComRegistroDoacaoRequest;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CadastroPecaFacadeService {

    private final DoadorRepository doadorRepository;
    private final PecaRepository pecaRepository;
    private final DoacaoRepository doacaoRepository;
    private final IgrejaRepository igrejaRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CadastroPecaFacadeService(DoadorRepository doadorRepository,
                                     PecaRepository pecaRepository,
                                     DoacaoRepository doacaoRepository,
                                     IgrejaRepository igrejaRepository,
                                     ApplicationEventPublisher eventPublisher) {
        this.doadorRepository = doadorRepository;
        this.pecaRepository = pecaRepository;
        this.doacaoRepository = doacaoRepository;
        this.igrejaRepository = igrejaRepository;
        this.eventPublisher = eventPublisher;
    }

    public Doacao registrarPecaComDoacao(NovaPecaComRegistroDoacaoRequest request) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) throw new IllegalStateException("Igreja não identificada.");

        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada."));

        // 🔹 Buscar doador existente por ID, se informado corretamente
        Doador doador;
        UUID doadorUUID = null;
        if (request.getDoadorId() != null && !request.getDoadorId().isBlank()) {
            try {
                doadorUUID = UUID.fromString(request.getDoadorId());
                doador = doadorRepository.findById(doadorUUID)
                        .orElseThrow(() -> new EntityNotFoundException("Doador não encontrado com o ID informado."));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("ID do doador inválido.");
            }
        } else {
            // 🔹 Criar novo doador
            if (request.getNomeDoador() == null || request.getNomeDoador().isBlank()) {
                throw new IllegalArgumentException("Nome do doador é obrigatório quando o ID não é informado.");
            }

            boolean doadorExiste = doadorRepository.existsByNomeIgnoreCaseAndContatoIgnoreCase(
                    request.getNomeDoador(), request.getContato() != null ? request.getContato() : ""
            );
            if (doadorExiste) {
                throw new IllegalArgumentException("Já existe um doador com o mesmo nome e contato.");
            }

            doador = Doador.builder()
                    .nome(request.getNomeDoador())
                    .contato(request.getContato())
                    .observacoes(request.getObservacoesDoador())
                    .igreja(igreja)
                    .build();

            doador = doadorRepository.save(doador);
        }

        // 🔹 Criar e salvar peça
        Peca peca = Peca.builder()
                .nome(request.getNomePeca())
                .cor(request.getCor())
                .categoria(request.getCategoria())
                .quantidade(request.getQuantidade())
                .preco(request.getPreco())
                .observacoes(request.getObservacoes())
                .disponivel(request.getQuantidade() > 0)
                .igreja(igreja)
                .criadoEm(LocalDateTime.now())
                .build();

        peca = pecaRepository.save(peca);
        eventPublisher.publishEvent(new PecaCriadaEvent(this, peca));

        // 🔹 Criar e salvar doação
        Doacao doacao = Doacao.builder()
                .peca(peca)
                .doador(doador)
                .quantidade(request.getQuantidade())
                .dataDoacao(LocalDateTime.now())
                .igreja(igreja)
                .build();

        return doacaoRepository.save(doacao);
    }
}
