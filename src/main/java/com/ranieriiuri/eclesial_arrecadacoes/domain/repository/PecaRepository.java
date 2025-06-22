package com.ranieriiuri.eclesial_arrecadacoes.domain.repository;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PecaRepository extends JpaRepository<Peca, UUID> {

    // Exemplo de método adicional útil para filtrar por igreja
    List<Peca> findAllByIgrejaId(UUID igrejaId);

    List<Peca> findByIgrejaIdAndNomeIgnoreCaseContaining(UUID igrejaId, String nome);

    List<Peca> findByIgrejaIdAndDisponivelTrue(UUID igrejaId);

    List<Peca> findByIgrejaIdAndCategoria(UUID igrejaId, String categoria);
}
