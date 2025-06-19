package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.DoacaoRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RankingDoadorDTO;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;

    public DoacaoService(DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }

    public Doacao salvarDoacao(Doacao doacao) {
        return doacaoRepository.save(doacao);
    }

    public List<Doacao> listarPorIgrejaAtual() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return doacaoRepository.findByIgrejaId(igrejaId);
    }

    public List<Doacao> listarPorDoador(UUID doadorId) {
        return doacaoRepository.findByDoadorId(doadorId);
    }

    public List<Doacao> listarPorPeca(UUID pecaId) {
        return doacaoRepository.findByPecaId(pecaId);
    }

    public Doacao buscarPorId(UUID id) {
        return doacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doação não encontrada"));
    }

    public List<RankingDoadorDTO> obterRankingDoadores() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return doacaoRepository.obterRankingDoadores(igrejaId);
    }
}
