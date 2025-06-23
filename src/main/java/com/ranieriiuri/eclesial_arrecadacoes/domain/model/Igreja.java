package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O nome da igreja é obrigatório")
    private String nome;

    @Column(length = 20, unique = true)
    private String cnpj;

    @Column(length = 60)
    private String cidade;

    @Column(length = 50)
    private String estado;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
