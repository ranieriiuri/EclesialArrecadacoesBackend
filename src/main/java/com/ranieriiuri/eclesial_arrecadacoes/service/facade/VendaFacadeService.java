package com.ranieriiuri.eclesial_arrecadacoes.service.facade;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.*;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.VendaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import com.ranieriiuri.eclesial_arrecadacoes.service.EventoService;
import com.ranieriiuri.eclesial_arrecadacoes.service.PecaService;
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
     * Registra uma venda, atualiza o estoque da peça e persiste tudo.
     */
    @Transactional
    public Venda registrarVenda(UUID pecaId, UUID eventoId, int quantidadeVendida, String comprador) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) throw new IllegalStateException("Igreja não identificada.");

        Peca peca = pecaService.buscarPorId(pecaId);
        if (peca == null || !peca.getIgreja().getId().equals(igrejaId)) {
            throw new EntityNotFoundException("Peça não encontrada ou não pertence à igreja atual.");
        }

        if (peca.getQuantidade() < quantidadeVendida) {
            throw new IllegalArgumentException("Quantidade em estoque insuficiente.");
        }

        // Atualiza a quantidade da peça
        int novaQuantidade = peca.getQuantidade() - quantidadeVendida;
        pecaService.atualizarQuantidade(pecaId, novaQuantidade);

        Evento evento = eventoService.buscarPorId(eventoId);
        if (evento == null || !evento.getIgreja().getId().equals(igrejaId)) {
            throw new EntityNotFoundException("Evento não encontrado ou não pertence à igreja atual.");
        }

        BigDecimal valorTotal = peca.getPreco().multiply(BigDecimal.valueOf(quantidadeVendida));

        Venda venda = Venda.builder()
                .peca(peca)
                .evento(evento)
                .quantidade(quantidadeVendida)
                .comprador(comprador)
                .valorArrecadado(valorTotal)
                .igreja(peca.getIgreja())
                .dataVenda(LocalDateTime.now())
                .build();

        return vendaRepository.save(venda);
    }
}
