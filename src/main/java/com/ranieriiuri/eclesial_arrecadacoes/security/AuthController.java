package com.ranieriiuri.eclesial_arrecadacoes.security;

import com.ranieriiuri.eclesial_arrecadacoes.security.dto.AuthRequest;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.AuthResponse;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Classe da camada de controle, q recebe a req de login e retorna o token
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

}
