package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "igrejas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Igreja {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private String cnpj;
    private String cidade;
    private String estado;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}
