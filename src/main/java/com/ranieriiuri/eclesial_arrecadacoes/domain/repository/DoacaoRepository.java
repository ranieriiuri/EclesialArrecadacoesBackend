package com.ranieriiuri.eclesial_arrecadacoes.domain.repository;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RankingDoadorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, UUID> {
    List<Doacao> findByIgrejaId(UUID igrejaId);
    List<Doacao> findByDoadorId(UUID doadorId);
    List<Doacao> findByIgrejaIdAndDataDoacaoBetween(UUID igrejaId, LocalDateTime inicio, LocalDateTime fim);

    @Query(value = """
        SELECT 
            d.doador_id AS doadorId,
            doa.nome AS nomeDoador,
            COUNT(d.id) AS totalDoacoes,
            SUM(d.quantidade) AS totalPecasDoadas
        FROM doacoes d
        JOIN doadores doa ON d.doador_id = doa.id
        WHERE d.igreja_id = :igrejaId
        GROUP BY d.doador_id, doa.nome
        ORDER BY totalPecasDoadas DESC
        """, nativeQuery = true)
    List<RankingDoadorDTO> obterRankingDoadores(UUID igrejaId);

    @Query(value = """
    SELECT 
        d.doador_id AS doadorId,
        doa.nome AS nomeDoador,
        COUNT(d.id) AS totalDoacoes,
        SUM(d.quantidade) AS totalPecasDoadas
    FROM doacoes d
    JOIN doadores doa ON d.doador_id = doa.id
    WHERE d.igreja_id = :igrejaId
      AND d.data BETWEEN :dataInicio AND :dataFim
    GROUP BY d.doador_id, doa.nome
    ORDER BY totalPecasDoadas DESC
    """, nativeQuery = true)
    List<RankingDoadorDTO> obterRankingDoadoresPorPeriodo(UUID igrejaId, LocalDateTime dataInicio, LocalDateTime dataFim);

}
