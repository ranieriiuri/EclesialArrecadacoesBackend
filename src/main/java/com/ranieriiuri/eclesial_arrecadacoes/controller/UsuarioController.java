package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.dto.AlterarSenhaRequest;
import com.ranieriiuri.eclesial_arrecadacoes.security.jwt.JwtService;
import com.ranieriiuri.eclesial_arrecadacoes.service.CloudinaryService;
import com.ranieriiuri.eclesial_arrecadacoes.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;

    public UsuarioController(UsuarioService usuarioService,
                             JwtService jwtService,
                             CloudinaryService cloudinaryService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
        this.cloudinaryService = cloudinaryService;
    }

    // 🔹 Buscar dados do usuário autenticado
    @GetMapping("/dados")
    public ResponseEntity<Usuario> buscarUsuarioPorEmail(@RequestHeader("Authorization") String authHeader) {
        String email = extractEmailFromHeader(authHeader);
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    // 🔹 Buscar usuário por ID (uso técnico ou administrativo)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable UUID id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Atualizar dados pessoais
    @PutMapping("/dados")
    public ResponseEntity<Usuario> atualizarUsuarioLogado(@RequestHeader("Authorization") String authHeader,
                                                          @Valid @RequestBody Usuario updatedData) {
        String email = extractEmailFromHeader(authHeader);
        Usuario updated = usuarioService.atualizarUsuarioLogado(email, updatedData);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Alterar senha
    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody AlterarSenhaRequest request) {
        usuarioService.alterarSenhaUsuarioLogado(request);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Atualizar foto de perfil
    @PutMapping("/dados/foto")
    public ResponseEntity<String> atualizarFotoPerfil(@RequestHeader("Authorization") String authHeader,
                                                      @RequestParam("foto") MultipartFile foto) {
        String email = extractEmailFromHeader(authHeader);
        String urlFoto = cloudinaryService.uploadImage(foto);
        usuarioService.atualizarFotoPerfil(email, urlFoto);
        return ResponseEntity.ok(urlFoto);
    }

    // 🔹 Listar usuários da igreja (tenant atual)
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios(); // ✅ Filtrando por tenant internamente
        return ResponseEntity.ok(usuarios);
    }

    // 🔹 Excluir conta do usuário autenticado
    @DeleteMapping("/deletar")
    public ResponseEntity<String> excluirUsuarioLogado(@RequestHeader("Authorization") String authHeader) {
        String email = extractEmailFromHeader(authHeader);
        usuarioService.excluirUsuarioLogado(email);
        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }

    // 🔒 Utilitário privado para extrair e-mail do token JWT
    private String extractEmailFromHeader(String authHeader) {
        return jwtService.extractEmail(authHeader.replace("Bearer ", ""));
    }
}
