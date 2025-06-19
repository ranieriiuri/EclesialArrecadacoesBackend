package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.CategoriaPeca;
import com.ranieriiuri.eclesial_arrecadacoes.domain.event.model.PecaCriadaEvent;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doador;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.DoadorRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.PecaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;
    private final IgrejaRepository igrejaRepository;
    private final DoadorRepository doadorRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PecaService(PecaRepository pecaRepository,
                       IgrejaRepository igrejaRepository,
                       DoadorRepository doadorRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.pecaRepository = pecaRepository;
        this.igrejaRepository = igrejaRepository;
        this.doadorRepository = doadorRepository;
        this.eventPublisher = eventPublisher;
    }

    public Peca registrarNovaPeca(Peca novaPeca) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) throw new IllegalStateException("Igreja não identificada.");

        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada"));

        novaPeca.setIgreja(igreja);
        novaPeca.setCriadoEm(LocalDateTime.now());
        novaPeca.setDisponivel(novaPeca.getQuantidade() > 0);

        // 🔹 Criar doador junto com a peça, se fornecido sem ID
        if (novaPeca.getDoador() != null) {
            Doador doador = novaPeca.getDoador();
            doador.setIgreja(igreja); // associa à igreja atual
            Doador salvo = doadorRepository.save(doador);
            novaPeca.setDoador(salvo);
        }

        Peca salva = pecaRepository.save(novaPeca);
        eventPublisher.publishEvent(new PecaCriadaEvent(this, salva));
        return salva;
    }

    public Peca atualizarQuantidade(UUID pecaId, int novaQuantidade) {
        Peca peca = buscarPorId(pecaId);
        peca.setQuantidade(novaQuantidade);
        peca.setDisponivel(novaQuantidade > 0);
        return pecaRepository.save(peca);
    }

    public void excluirPeca(UUID id) {
        Peca peca = buscarPorId(id);
        pecaRepository.delete(peca);
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

        if (dados.getDoador() != null) {
            Doador doador = dados.getDoador();
            doador.setIgreja(existente.getIgreja()); // garante a associação
            Doador salvo = doadorRepository.save(doador);
            existente.setDoador(salvo);
        } else {
            existente.setDoador(null);
        }

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

    public List<Peca> listarPorCategoria(CategoriaPeca categoria) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return pecaRepository.findByIgrejaIdAndCategoria(igrejaId, categoria);
    }
}
