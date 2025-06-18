package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.UsuarioRepository;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Usuario atualizarUsuarioLogado(String email, Usuario dadosAtualizados) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        usuario.setNome(dadosAtualizados.getNome());
        usuario.setCargo(dadosAtualizados.getCargo());
        usuario.setCpf(dadosAtualizados.getCpf());
        usuario.setEndereco(dadosAtualizados.getEndereco());

        if (dadosAtualizados.getSenhaHash() != null && !dadosAtualizados.getSenhaHash().isEmpty()) {
            usuario.setSenhaHash(passwordEncoder.encode(dadosAtualizados.getSenhaHash()));
        }

        return usuarioRepository.save(usuario);
    }

    public void excluirUsuarioLogado(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuarioRepository.delete(usuario);
    }

    public void atualizarFoto(String email, String urlFoto) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.setFotoPerfil(urlFoto);
        usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}