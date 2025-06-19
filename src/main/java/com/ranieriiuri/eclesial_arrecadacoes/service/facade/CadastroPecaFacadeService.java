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
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada"));

        // 🔹 Buscar ou criar doador
        Doador doador;
        if (request.getDoadorId() != null) {
            doador = doadorRepository.findById(request.getDoadorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doador não encontrado"));
        } else {
            doador = Doador.builder()
                    .nome(request.getNomeDoador())
                    .contato(request.getContato())
                    .observacoes(request.getObservacoesDoador())
                    .igreja(igreja)
                    .build();
            doador = doadorRepository.save(doador);
        }

        // 🔹 Criar peça
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

        // 🔹 Registrar doação
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
