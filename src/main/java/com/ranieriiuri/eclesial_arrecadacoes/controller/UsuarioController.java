package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.security.jwt.JwtService;
import com.ranieriiuri.eclesial_arrecadacoes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    // 🔹 Buscar dados do usuário autenticado
    @GetMapping("/dados")
    public ResponseEntity<Usuario> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        Usuario usuario = usuarioService.findByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    // 🔹 Atualizar dados pessoais
    @PutMapping("/dados")
    public ResponseEntity<Usuario> updateCurrentUser(@RequestHeader("Authorization") String authHeader,
                                                     @Valid @RequestBody Usuario updatedData) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        Usuario updated = usuarioService.update(email, updatedData);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Atualizar foto de perfil
    @PutMapping("/dados/foto")
    public ResponseEntity<String> uploadProfilePhoto(@RequestHeader("Authorization") String authHeader,
                                                     @RequestParam("foto") MultipartFile foto) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        usuarioService.atualizarFoto(email, foto);
        return ResponseEntity.ok("Foto atualizada com sucesso.");
    }

    // 🔹 Deletar a conta do usuário autenticado
    @DeleteMapping("/dados")
    public ResponseEntity<String> deleteCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        usuarioService.deleteByEmail(email);
        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }
}
