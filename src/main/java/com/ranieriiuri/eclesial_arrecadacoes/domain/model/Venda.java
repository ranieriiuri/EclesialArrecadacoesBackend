package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "peca_nome")
    private String pecaNome;


    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Positive(message = "A quantidade deve ser maior que zero.")
    @Column(nullable = false)
    private int quantidade;

    @Size(max = 100)
    private String comprador; // Pode ser opcional

    @NotNull(message = "O valor arrecadado é obrigatório.")
    @DecimalMin(value = "0.00", inclusive = false, message = "O valor arrecadado deve ser positivo.")
    @Column(name = "valor_arrecadado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorArrecadado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;
}
