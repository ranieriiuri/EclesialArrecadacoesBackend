package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.DoadorRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.DoacaoRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.PecaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;
    private final IgrejaRepository igrejaRepository;
    private final DoadorRepository doadorRepository;
    private final DoacaoRepository doacaoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PecaService(PecaRepository pecaRepository,
                       IgrejaRepository igrejaRepository,
                       DoadorRepository doadorRepository, DoacaoRepository doacaoRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.pecaRepository = pecaRepository;
        this.igrejaRepository = igrejaRepository;
        this.doadorRepository = doadorRepository;
        this.doacaoRepository = doacaoRepository;
        this.eventPublisher = eventPublisher;
    }

    public Peca atualizarQuantidade(UUID pecaId, int novaQuantidade) {
        Peca peca = buscarPorId(pecaId);
        peca.setQuantidade(novaQuantidade);
        peca.setDisponivel(novaQuantidade > 0);
        return pecaRepository.save(peca);
    }

    public Peca atualizarPeca(UUID id, Peca dados) {
        Peca existente = buscarPorId(id);

        existente.setNome(dados.getNome());
        existente.setCor(dados.getCor());
        existente.setCategoria(dados.getCategoria());
        existente.setPreco(dados.getPreco());
        existente.setQuantidade(dados.getQuantidade());
        existente.setObservacoes(dados.getObservacoes());
        existente.setDisponivel(dados.getQuantidade() > 0);

        return pecaRepository.save(existente);
    }

    public Peca buscarPorId(UUID id) {
        return pecaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça não encontrada."));
    }

    public List<Peca> buscarPorNome(String nome) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return pecaRepository.findByIgrejaIdAndNomeIgnoreCaseContaining(igrejaId, nome);
    }

    public List<Peca> listarTodas() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return pecaRepository.findAllByIgrejaId(igrejaId);
    }

    public List<Peca> listarDisponiveis() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return pecaRepository.findByIgrejaIdAndDisponivelTrue(igrejaId);
    }

    public List<Peca> listarPorCategoria(String categoria) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return pecaRepository.findByIgrejaIdAndCategoriaIgnoreCase(igrejaId, categoria);
    }

    public void excluirPeca(UUID id) {
        Peca peca = buscarPorId(id);
        pecaRepository.delete(peca);
    }
}
