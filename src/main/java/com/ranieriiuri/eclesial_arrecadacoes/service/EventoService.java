package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Evento;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Venda;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.EventoRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.VendaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.CriarEventoRequest;
import com.ranieriiuri.eclesial_arrecadacoes.dto.VendaResumoDTO;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final IgrejaRepository igrejaRepository;
    private final VendaRepository vendaRepository;

    public EventoService(EventoRepository eventoRepository, IgrejaRepository igrejaRepository, VendaRepository vendaRepository) {
        this.eventoRepository = eventoRepository;
        this.igrejaRepository = igrejaRepository;
        this.vendaRepository = vendaRepository;
    }

    public Evento criarEvento(CriarEventoRequest request) {
        UUID igrejaId = TenantContext.getCurrentTenant();
        if (igrejaId == null) {
            throw new IllegalStateException("Igreja não encontrada no contexto.");
        }

        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada"));

        Evento evento = new Evento();
        evento.setTipo(request.getTipo());  // String agora
        evento.setDescricao(request.getDescricao());
        evento.setStatus("planejando"); // default string
        evento.setCriadoEm(LocalDateTime.now());
        evento.setIgreja(igreja);

        return eventoRepository.save(evento);
    }

    public Evento buscarPorId(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com ID: " + id));
    }

    public Evento atualizarDataInicio(UUID eventoId, LocalDate novaDataInicio) {
        Evento evento = buscarPorId(eventoId);

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para modificar este evento.");
        }

        if (!"planejando".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Só é possível alterar a data de início se o evento estiver no status 'planejando'.");
        }

        if (novaDataInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A nova data de início não pode ser no passado.");
        }

        evento.setDataInicio(novaDataInicio);
        return eventoRepository.save(evento);
    }

    public Evento iniciarEvento(UUID eventoId) {
        Evento evento = buscarPorId(eventoId);

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para iniciar este evento.");
        }

        if (!"planejando".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Só é possível iniciar eventos com status 'planejando'.");
        }

        evento.setStatus("em andamento");
        evento.setDataInicio(LocalDate.now());
        return eventoRepository.save(evento);
    }

    public Evento finalizarEvento(UUID eventoId) {
        Evento evento = buscarPorId(eventoId);

        UUID igrejaId = TenantContext.getCurrentTenant();
        if (!evento.getIgreja().getId().equals(igrejaId)) {
            throw new SecurityException("Você não tem permissão para finalizar este evento.");
        }

        if (!"em andamento".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Só é possível finalizar eventos que estão 'em andamento'.");
        }

        evento.setStatus("finalizado");
        evento.setDataFim(LocalDate.now());
        return eventoRepository.save(evento);
    }

    public byte[] generatePdfReport(UUID igrejaId, UUID eventoId) throws Exception {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        List<Venda> vendas = vendaRepository.findByIgrejaIdAndEventoId(igrejaId, eventoId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12);

        document.add(new Paragraph("Relatório do Evento: " + (evento.getDescricao() != null ? evento.getDescricao() : evento.getTipo()), titleFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Tipo: " + evento.getTipo(), normalFont));
        document.add(new Paragraph("Data início: " + (evento.getDataInicio() != null ? evento.getDataInicio().toString() : "Não informada"), normalFont));
        document.add(new Paragraph("Data fim: " + (evento.getDataFim() != null ? evento.getDataFim().toString() : "Não informada"), normalFont));
        document.add(new Paragraph("Total de Vendas: " + vendas.size(), normalFont));

        double totalValor = vendas.stream()
                .mapToDouble(v -> v.getValorArrecadado().doubleValue())
                .sum();

        document.add(new Paragraph("Valor Total: R$ " + String.format("%.2f", totalValor), normalFont));
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("Peça");
        table.addCell("Comprador");
        table.addCell("Quantidade");
        table.addCell("Total Venda");

        for (Venda venda : vendas) {
            String pecaNome = venda.getPecaNome() != null ? venda.getPecaNome() : "-";
            String comprador = venda.getComprador() != null ? venda.getComprador() : "Anônimo";
            String quantidade = String.valueOf(venda.getQuantidade());
            String totalVenda = String.format("R$ %.2f", venda.getValorArrecadado());

            table.addCell(pecaNome);
            table.addCell(comprador);
            table.addCell(quantidade);
            table.addCell(totalVenda);
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }


    public List<Evento> listarEventosDaIgrejaAtual() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return eventoRepository.findAllByIgrejaId(igrejaId);
    }

    public void excluirEvento(UUID id) {
        Evento evento = buscarPorId(id);

        if ("finalizado".equalsIgnoreCase(evento.getStatus())) {
            throw new IllegalStateException("Não é possível excluir um evento já finalizado.");
        }

        eventoRepository.delete(evento);
    }
}
