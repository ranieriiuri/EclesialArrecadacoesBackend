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

    // Buscar peças disponíveis
    List<Peca> findByDisponivelTrue();

    // Buscar por nome (ignorando maiúsculas/minúsculas)
    List<Peca> findByNomeIgnoreCaseContaining(String nome);
}
