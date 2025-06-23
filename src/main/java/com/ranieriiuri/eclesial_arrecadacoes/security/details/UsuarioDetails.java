package com.ranieriiuri.eclesial_arrecadacoes.security.details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class UsuarioDetails implements UserDetails {

    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private String cargo;
    private String fotoPerfil;
    private UUID igrejaId;
    private String igrejaNome;

    public UsuarioDetails(
            UUID id,
            String nome,
            String email,
            String senha,
            String cargo,
            String fotoPerfil,
            UUID igrejaId,
            String igrejaNome
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.fotoPerfil = fotoPerfil;
        this.igrejaId = igrejaId;
        this.igrejaNome = igrejaNome;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCargo() {
        return cargo;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public UUID getIgrejaId() {
        return igrejaId;
    }

    public String getIgrejaNome() {
        return igrejaNome;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    // Obrigatório pelo contrato da interface UserDetails
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // ou retornar permissões reais
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
