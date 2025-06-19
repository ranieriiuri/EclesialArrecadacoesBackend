package com.ranieriiuri.eclesial_arrecadacoes.domain.repository;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoadorRepository extends JpaRepository<Doador, UUID> {
    List<Doador> findAllByIgrejaId(UUID igrejaId);
    boolean existsByNomeIgnoreCaseAndContatoIgnoreCase(String nome, String contato);
}
