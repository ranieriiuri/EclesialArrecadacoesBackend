package com.ranieriiuri.eclesial_arrecadacoes.domain.model;

import com.ranieriiuri.eclesial_arrecadacoes.security.details.UsuarioDetails;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "mfa_secreto")
    private String mfaSecreto;

    private String cargo;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "igreja_id", nullable = false)
    private Igreja igreja;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public void setIgrejaId(UUID currentTenant) {
    }

    public UUID getId() {
    }

    public String getEmail() {
    }

    public String getSenhaHash() {
    }

    public UsuarioDetails getIgreja() {
    }

    public String getCargo() {
        return null;
    }
}
