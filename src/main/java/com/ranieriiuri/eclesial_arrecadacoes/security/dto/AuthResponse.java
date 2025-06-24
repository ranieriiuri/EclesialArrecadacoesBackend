package com.ranieriiuri.eclesial_arrecadacoes.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private AuthUserResponse user;
}
