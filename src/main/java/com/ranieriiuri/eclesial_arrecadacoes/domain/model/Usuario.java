package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @NotBlank
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;


    @Column(name = "mfa_secreto")
    private String mfaSecreto;

    private String cargo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @ManyToOne
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
}
