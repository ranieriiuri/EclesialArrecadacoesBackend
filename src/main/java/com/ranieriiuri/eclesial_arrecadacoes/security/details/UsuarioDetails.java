package com.ranieriiuri.eclesial_arrecadacoes.security.details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class UsuarioDetails implements UserDetails {
    private UUID id;
    private String nome; // ✅ novo campo
    private String email;
    private String senha;
    private UUID igrejaId;
    private String cargo;

    public UsuarioDetails(UUID id, String nome, String email, String senha, UUID igrejaId, String cargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.igrejaId = igrejaId;
        this.cargo = cargo;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIgrejaId() {
        return igrejaId;
    }

    public String getNome() { return nome; }

    public String getCargo() {
        return cargo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // ou retorne as permissões, se houver
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

