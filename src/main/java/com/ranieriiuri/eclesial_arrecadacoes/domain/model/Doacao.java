package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "peca_id")
    private Peca peca;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doador_id")
    private Doador doador;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "data_doacao")
    private LocalDateTime dataDoacao;

    @ManyToOne
    @JoinColumn(name = "igreja_id")
    private Igreja igreja;
}
