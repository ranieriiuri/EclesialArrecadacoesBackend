package com.ranieriiuri.eclesial_arrecadacoes.domain.repository;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Venda;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RelatorioVendasDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VendaRepository extends JpaRepository<Venda, UUID> {

    // 🔸 Filtrar vendas por período
    List<Venda> findByIgrejaIdAndDataVendaBetween(UUID igrejaId, LocalDateTime inicio, LocalDateTime fim);

    // 🔸 Filtrar vendas por evento específico
    List<Venda> findByIgrejaIdAndEventoId(UUID igrejaId, UUID eventoId);

    // 🔸 Listar todas as vendas de uma igreja
    List<Venda> findByIgrejaId(UUID igrejaId);

    @Query("""
    SELECT new com.seu.pacote.RelatorioVendasDTO(COUNT(v), SUM(v.quantidade), SUM(v.valorTotal))
    FROM Venda v
    WHERE v.igreja.id = :igrejaId AND v.data BETWEEN :inicio AND :fim
""")
    RelatorioVendasDTO somarRelatorioPorPeriodo(UUID igrejaId, LocalDateTime inicio, LocalDateTime fim);

    @Query("""
    SELECT new com.seu.pacote.RelatorioVendasDTO(COUNT(v), SUM(v.quantidade), SUM(v.valorTotal))
    FROM Venda v
    WHERE v.igreja.id = :igrejaId
""")
    RelatorioVendasDTO somarRelatorioTotal(UUID igrejaId);

    @Query("""
    SELECT new com.seu.pacote.RelatorioVendasDTO(COUNT(v), SUM(v.quantidade), SUM(v.valorTotal))
    FROM Venda v
    WHERE v.igreja.id = :igrejaId AND v.evento.id = :eventoId
""")
    RelatorioVendasDTO somarRelatorioPorEvento(UUID igrejaId, UUID eventoId);

}
