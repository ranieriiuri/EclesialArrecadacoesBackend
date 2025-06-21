package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.UsuarioRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.AlterarSenhaRequest;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
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

    @Transactional
    public void alterarSenhaUsuarioLogado(AlterarSenhaRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenhaHash())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        usuario.setSenhaHash(passwordEncoder.encode(request.getNovaSenha()));
        usuarioRepository.save(usuario);
    }

    public void atualizarFotoPerfil(String email, String urlFoto) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.setFotoPerfil(urlFoto);
        usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return usuarioRepository.findAllByIgrejaId(igrejaId);
    }

    public void excluirUsuarioLogado(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuarioRepository.delete(usuario);
    }
}