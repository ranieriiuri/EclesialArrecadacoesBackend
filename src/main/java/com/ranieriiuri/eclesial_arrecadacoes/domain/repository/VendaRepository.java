package com.ranieriiuri.eclesial_arrecadacoes.domain.repository;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
