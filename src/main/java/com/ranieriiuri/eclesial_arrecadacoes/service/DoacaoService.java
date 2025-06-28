package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.DoacaoRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RankingDoadorDTO;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


import java.time.LocalDateTime;
import java.util.List;

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

    public Doacao buscarPorId(UUID id) {
        return doacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doação não encontrada"));
    }

    public List<RankingDoadorDTO> obterRankingDoadores() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return doacaoRepository.obterRankingDoadores(igrejaId);
    }

    public List<RankingDoadorDTO> gerarRankingPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        UUID igrejaId = TenantContext.getCurrentTenant();

        // Busca doações da igreja no período
        List<Doacao> doacoes = doacaoRepository.findByIgrejaIdAndDataDoacaoBetween(igrejaId, inicio, fim);

        // Agrupa doações por nome do doador, ignorando caixa
        Map<String, List<Doacao>> agrupadoPorNome = doacoes.stream()
                .collect(Collectors.groupingBy(d -> d.getDoador().getNome().toLowerCase()));

        // Monta lista de RankingDoadorDTO
        List<RankingDoadorDTO> ranking = new ArrayList<>();
        for (var entry : agrupadoPorNome.entrySet()) {
            String nome = entry.getKey();
            List<Doacao> doacoesDoDoador = entry.getValue();

            int totalPecas = doacoesDoDoador.stream()
                    .mapToInt(Doacao::getQuantidade)
                    .sum();

            // Se tiver valor, some aqui. Senão, pode remover ou ajustar no DTO
            int totalValor = 0; // Ajuste se tiver valor associado às doações

            ranking.add(new RankingDoadorDTO(nome, totalPecas, totalValor));
        }

        // Ordena do maior para o menor
        ranking.sort(Comparator.comparingInt(RankingDoadorDTO::getTotalPecas).reversed());

        return ranking;
    }


}
