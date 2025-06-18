package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.UsuarioRepository;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuariosDaIgrejaAtual() {
        UUID igrejaId = TenantContext.getCurrentTenant();
        return usuarioRepository.findAllByIgrejaId(igrejaId);
    }

    public Usuario salvar(Usuario usuario) {
        usuario.setIgrejaId(TenantContext.getCurrentTenant());
        return usuarioRepository.save(usuario);
    }
}

