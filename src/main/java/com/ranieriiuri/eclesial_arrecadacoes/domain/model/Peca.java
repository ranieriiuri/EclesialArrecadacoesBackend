package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ranieriiuri.eclesial_arrecadacoes.domain.enums.CategoriaPeca;


@Entity
@Table(name = "pecas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Peca {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "O nome da peça é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 50)
    private String cor;

    @NotNull(message = "A categoria é obrigatória.")
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", columnDefinition = "categoria_peca", nullable = false)
    private CategoriaPeca categoria;

    @Column(nullable = false)
    private int quantidade;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.00", inclusive = true, message = "O preço não pode ser negativo.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "doador_id")
    private Doador doador;

    @Column(nullable = false)
    private boolean disponivel = true;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @NotNull(message = "A igreja é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}
