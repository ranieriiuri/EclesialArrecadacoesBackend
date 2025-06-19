package com.ranieriiuri.eclesial_arrecadacoes.domain.repository;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, UUID> {
    List<Doacao> findByIgrejaId(UUID igrejaId);
    List<Doacao> findByDoadorId(UUID doadorId);
    List<Doacao> findByPecaId(UUID pecaId);
}
