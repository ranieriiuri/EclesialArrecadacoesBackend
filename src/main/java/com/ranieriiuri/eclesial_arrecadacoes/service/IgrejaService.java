package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.IgrejaUpdateRequest;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IgrejaService {

    private final IgrejaRepository igrejaRepository;

    public IgrejaService(IgrejaRepository igrejaRepository) {
        this.igrejaRepository = igrejaRepository;
    }

    public Igreja atualizarIgreja(@Valid IgrejaUpdateRequest nova) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) {
            throw new IllegalStateException("Igreja não identificada no contexto.");
        }

        Igreja existente = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada"));

        // Atualiza apenas os campos permitidos
        existente.setNome(nova.getNome());
        existente.setCnpj(nova.getCnpj());
        existente.setCidade(nova.getCidade());
        existente.setEstado(nova.getEstado());

        return igrejaRepository.save(existente);
    }
}
