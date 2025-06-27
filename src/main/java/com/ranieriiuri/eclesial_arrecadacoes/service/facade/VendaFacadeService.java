package com.ranieriiuri.eclesial_arrecadacoes.service.facade;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.*;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.VendaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.service.EventoService;
import com.ranieriiuri.eclesial_arrecadacoes.service.PecaService;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VendaFacadeService {

    private final VendaRepository vendaRepository;
    private final PecaService pecaService;
    private final EventoService eventoService;

    public VendaFacadeService(
            VendaRepository vendaRepository,
            PecaService pecaService,
            EventoService eventoService
    ) {
        this.vendaRepository = vendaRepository;
        this.pecaService = pecaService;
        this.eventoService = eventoService;
    }

    /**
     * Registra uma venda com base no nome da peça e ID do evento.
     */
    @Transactional
    public Venda registrarVenda(UUID pecaId, UUID eventoId, int quantidadeVendida, String comprador) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) throw new IllegalStateException("Igreja não identificada.");

        // Busca a peça por ID e verifica se pertence à igreja atual
        Peca peca = pecaService.buscarPorId(pecaId);
        if (!peca.getIgreja().getId().equals(igrejaId)) {
            throw new EntityNotFoundException("Peça não pertence à igreja atual.");
        }

        if (peca.getQuantidade() < quantidadeVendida) {
            throw new IllegalArgumentException("Quantidade em estoque insuficiente.");
        }

        // Atualiza quantidade
        int novaQuantidade = peca.getQuantidade() - quantidadeVendida;
        pecaService.atualizarQuantidade(pecaId, novaQuantidade);

        if (novaQuantidade == 0) {
            pecaService.marcarComoIndisponivel(pecaId);
        }

        Evento evento = eventoService.buscarPorId(eventoId);
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new EntityNotFoundException("Evento não pertence à igreja atual.");
        }

        BigDecimal valorTotal = peca.getPreco().multiply(BigDecimal.valueOf(quantidadeVendida));

        Venda venda = Venda.builder()
                .pecaNome(peca.getNome())
                .evento(evento)
                .quantidade(quantidadeVendida)
                .comprador(comprador != null && !comprador.isBlank() ? comprador : null)
                .valorArrecadado(valorTotal)
                .igreja(peca.getIgreja())
                .dataVenda(LocalDateTime.now())
                .build();

        return vendaRepository.save(venda);
    }
}