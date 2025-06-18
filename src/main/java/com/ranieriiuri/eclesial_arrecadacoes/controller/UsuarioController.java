package com.ranieriiuri.eclesial_arrecadacoes.controller;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.security.jwt.JwtService;
import com.ranieriiuri.eclesial_arrecadacoes.service.CloudinaryService;
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
    private final CloudinaryService cloudinaryService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService, CloudinaryService cloudinaryService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
        this.cloudinaryService = cloudinaryService;
    }

    // 🔹 Buscar dados do usuário autenticado
    @GetMapping("/dados")
    public ResponseEntity<Usuario> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    // 🔹 Atualizar dados pessoais
    @PutMapping("/dados")
    public ResponseEntity<Usuario> updateCurrentUser(@RequestHeader("Authorization") String authHeader,
                                                     @Valid @RequestBody Usuario updatedData) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        Usuario updated = usuarioService.atualizarUsuarioLogado(email, updatedData);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Atualizar foto de perfil
    @PutMapping("/dados/foto")
    public ResponseEntity<String> uploadProfilePhoto(@RequestHeader("Authorization") String authHeader,
                                                     @RequestParam("foto") MultipartFile foto) {
        // Extrai o email do token JWT
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));

        // Faz upload da imagem para o Cloudinary e recebe a URL
        String urlFoto = cloudinaryService.uploadImage(foto);

        // Atualiza o usuário com a URL da foto
        usuarioService.atualizarFoto(email, urlFoto);

        // Retorna a URL da foto para o front
        return ResponseEntity.ok(urlFoto);
    }


    // 🔹 Deletar a conta do usuário autenticado
    @DeleteMapping("/dados")
    public ResponseEntity<String> deleteCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String email = jwtService.extractEmail(authHeader.replace("Bearer ", ""));
        usuarioService.excluirUsuarioLogado(email);
        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }
}
