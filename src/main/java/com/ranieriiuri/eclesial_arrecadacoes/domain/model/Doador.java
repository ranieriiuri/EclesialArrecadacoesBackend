package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "doadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doador {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "O nome do doador é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String contato;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;
}
