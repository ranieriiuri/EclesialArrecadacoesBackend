package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Venda;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.VendaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.RelatorioVendasDTO;
import com.ranieriiuri.eclesial_arrecadacoes.dto.VendaResumoDTO;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    // 🔹 Relatório por período
    public List<Venda> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return vendaRepository.findByIgrejaIdAndDataVendaBetween(igrejaId, inicio, fim);
    }

    // 🔹 Relatório por evento
    public List<Venda> listarPorEvento(UUID eventoId) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return vendaRepository.findByIgrejaIdAndEventoId(igrejaId, eventoId);
    }

    // 🔹 Listar todas as vendas da conta
    public List<VendaResumoDTO> listarTodas() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        List<Venda> vendas = vendaRepository.findByIgrejaId(igrejaId);

        return vendas.stream().map(venda -> new VendaResumoDTO(
                venda.getId(),
                venda.getPeca().getNome(),
                venda.getEvento().getTipo(),
                venda.getComprador(),
                venda.getQuantidade(),
                venda.getValorArrecadado(),
                venda.getDataVenda()
        )).toList();
    }

    public RelatorioVendasDTO gerarRelatorioPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return vendaRepository.somarRelatorioPorPeriodo(igrejaId, inicio, fim);
    }

    public RelatorioVendasDTO gerarRelatorioTotal() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return vendaRepository.somarRelatorioTotal(igrejaId);
    }

    public RelatorioVendasDTO gerarRelatorioPorEvento(UUID eventoId) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return vendaRepository.somarRelatorioPorEvento(igrejaId, eventoId);
    }
}
