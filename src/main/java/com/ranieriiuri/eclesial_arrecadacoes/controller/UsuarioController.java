package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.dto.AlterarSenhaRequest;
import com.ranieriiuri.eclesial_arrecadacoes.dto.UsuarioDTO;
import com.ranieriiuri.eclesial_arrecadacoes.security.jwt.JwtService;
import com.ranieriiuri.eclesial_arrecadacoes.service.CloudinaryService;
import com.ranieriiuri.eclesial_arrecadacoes.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
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

    // 🔹 Buscar usuário por ID (uso técnico ou administrativo)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable UUID id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Alterar senha
    @PutMapping("/change-password")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody AlterarSenhaRequest request) {
        usuarioService.alterarSenhaUsuarioLogado(request);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Atualizar foto de perfil
    @PutMapping("/me/photo")
    public ResponseEntity<String> atualizarFotoPerfil(@RequestHeader("Authorization") String authHeader,
                                                      @RequestParam("foto") MultipartFile foto) {
        String email = extractEmailFromHeader(authHeader);
        String urlFoto = cloudinaryService.uploadImage(foto);
        usuarioService.atualizarFotoPerfil(email, urlFoto);
        return ResponseEntity.ok(urlFoto);
    }

    // 🔹 Listar usuários da igreja (tenant atual)
    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios(); // ✅ Filtrando por tenant internamente
        return ResponseEntity.ok(usuarios);
    }

    // 🔒 Utilitário privado para extrair e-mail do token JWT
    private String extractEmailFromHeader(String authHeader) {
        return jwtService.extractEmail(authHeader.replace("Bearer ", ""));
    }

    // Endpoints para acesso para montagem do usuario type no frontend e atualização
    @GetMapping("/me/data")
    public ResponseEntity<UsuarioDTO> buscarUsuarioCompleto(@RequestHeader("Authorization") String authHeader) {
        String email = extractEmailFromHeader(authHeader);
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        UsuarioDTO dto = usuarioService.toDTO(usuario); // ou usuarioMapper.toDTO(...)
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me/data")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid UsuarioDTO dadosAtualizados) {

        String email = extractEmailFromHeader(authHeader);
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(email, dadosAtualizados);
        return ResponseEntity.ok(usuarioService.toDTO(usuarioAtualizado));
    }

    // 🔹 Excluir conta do usuário autenticado
    @DeleteMapping("/delete")
    public ResponseEntity<String> excluirUsuario(@RequestHeader("Authorization") String authHeader) {
        String email = extractEmailFromHeader(authHeader);
        usuarioService.excluirUsuario(email);
        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }
}
