package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Doador;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.DoadorRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoadorService {

    private final DoadorRepository doadorRepository;
    private final IgrejaRepository igrejaRepository;

    public DoadorService(DoadorRepository doadorRepository, IgrejaRepository igrejaRepository) {
        this.doadorRepository = doadorRepository;
        this.igrejaRepository = igrejaRepository;
    }

    public Doador criarDoador(Doador doador) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada"));
        doador.setIgreja(igreja);

        boolean doadorExiste = doadorRepository.existsByNomeIgnoreCaseAndContatoIgnoreCase(
                doador.getNome(), doador.getContato() != null ? doador.getContato() : "");

        if (doadorExiste) {
            throw new IllegalArgumentException("Já existe um doador com o mesmo nome e contato.");
        }

        return doadorRepository.save(doador);
    }

    public List<Doador> listarDoadoresDaIgrejaAtual() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return doadorRepository.findAllByIgrejaId(igrejaId);
    }

    public Doador atualizarDoador(UUID id, Doador dadosAtualizados) {
        Doador doador = doadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doador não encontrado"));

        doador.setNome(dadosAtualizados.getNome());
        doador.setContato(dadosAtualizados.getContato());
        doador.setObservacoes(dadosAtualizados.getObservacoes());

        return doadorRepository.save(doador);
    }

    public void excluirDoador(UUID id) {
        if (!doadorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doador não encontrado");
        }
        doadorRepository.deleteById(id);
    }
}