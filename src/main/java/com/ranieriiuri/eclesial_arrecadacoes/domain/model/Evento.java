package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.StatusEvento;
import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.TipoEvento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.TipoEvento;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", columnDefinition = "tipo_evento", nullable = false)
    private TipoEvento tipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEvento status;

    @ManyToOne
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
}
