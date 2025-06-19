package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "doacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doacao {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotNull(message = "A peça é obrigatória")
    @ManyToOne(optional = false)
    @JoinColumn(name = "peca_id", nullable = false)
    private Peca peca;

    @NotNull(message = "O doador é obrigatório")
    @ManyToOne(optional = false)
    @JoinColumn(name = "doador_id", nullable = false)
    private Doador doador;

    @Min(value = 0, message = "A quantidade não pode ser negativa")
    @Column(nullable = false)
    private int quantidade;

    @Column(name = "data_doacao")
    private LocalDateTime dataDoacao;

    @NotNull(message = "A igreja é obrigatória")
    @ManyToOne(optional = false)
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;
}
