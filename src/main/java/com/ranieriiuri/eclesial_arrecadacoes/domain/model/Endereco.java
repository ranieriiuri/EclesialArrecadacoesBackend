package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(length = 20)
    private String cep;

    @NotBlank(message = "A rua é obrigatória.")
    @Column(length = 100)
    private String logradouro;

    @NotBlank(message = "O número (ou S/N) é obrigatório.")
    @Column(length = 10)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    @Column(length = 100)
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    @Column(length = 100)
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    @Column(length = 50)
    private String estado;

    @NotBlank(message = "O país é obrigatório (por padrão, Brasil).")
    @Column(length = 50)
    @Builder.Default
    private String pais = "Brasil";
}
