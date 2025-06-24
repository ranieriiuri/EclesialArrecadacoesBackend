package com.ranieriiuri.eclesial_arrecadacoes.security.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UsuarioDetails implements UserDetails {

    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private UUID igrejaId;

    // se precisar de campos adicionais, declare aqui

    // você precisa implementar os métodos abstratos do UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // implementar conforme sua lógica, pode retornar Collections.emptyList() se não usar roles
        return null;
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
