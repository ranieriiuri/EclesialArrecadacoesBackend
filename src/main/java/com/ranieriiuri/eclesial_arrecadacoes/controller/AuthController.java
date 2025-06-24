package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.security.dto.AuthUserResponse;
import com.ranieriiuri.eclesial_arrecadacoes.service.AuthService;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.AuthRequest;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.AuthResponse;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.ranieriiuri.eclesial_arrecadacoes.security.details.UsuarioDetails;
/**
 * Controller responsável pela autenticação de usuários (login e registro).
 * Recebe e valida os dados de entrada e delega a lógica para o AuthService.
 */

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

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthUserResponse> me(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        AuthUserResponse response = new AuthUserResponse(
                usuarioDetails.getId(),
                usuarioDetails.getNome(),
                usuarioDetails.getEmail(),
                usuarioDetails.getIgrejaId()
        );
        return ResponseEntity.ok(response);
    }

}
