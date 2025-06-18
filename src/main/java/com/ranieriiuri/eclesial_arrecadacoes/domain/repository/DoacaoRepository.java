package com.ranieriiuri.eclesial_arrecadacoes.domain.repository;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoacaoRepository extends JpaRepository<Doacao, UUID> {
    List<Doacao> findAllByIgrejaId(UUID igrejaId);
}
