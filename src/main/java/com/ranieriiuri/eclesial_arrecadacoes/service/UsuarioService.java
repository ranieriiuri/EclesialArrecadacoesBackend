package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.UsuarioRepository;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuariosDaIgrejaAtual() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return usuarioRepository.findAllByIgrejaId(igrejaId);
    }

    public Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Usuario atualizarUsuarioLogado(Usuario dadosAtualizados) {
        Usuario usuario = getUsuarioLogado();

        usuario.setNome(dadosAtualizados.getNome());
        usuario.setCargo(dadosAtualizados.getCargo());
        usuario.setCpf(dadosAtualizados.getCpf());
        usuario.setEndereco(dadosAtualizados.getEndereco());

        if (dadosAtualizados.getSenhaHash() != null && !dadosAtualizados.getSenhaHash().isEmpty()) {
            usuario.setSenhaHash(passwordEncoder.encode(dadosAtualizados.getSenhaHash()));
        }

        return usuarioRepository.save(usuario);
    }

    public void excluirUsuarioLogado() {
        Usuario usuario = getUsuarioLogado();
        usuarioRepository.delete(usuario);
    }

    public Usuario atualizarFoto(String email, String fotoUrl) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setFotoPerfil(fotoUrl);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}
