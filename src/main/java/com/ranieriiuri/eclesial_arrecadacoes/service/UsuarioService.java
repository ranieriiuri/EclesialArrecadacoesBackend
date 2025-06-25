package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.UsuarioRepository;
import com.ranieriiuri.eclesial_arrecadacoes.dto.AlterarSenhaRequest;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Endereco;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.dto.UsuarioDTO;
import com.ranieriiuri.eclesial_arrecadacoes.dto.EnderecoDTO;
import com.ranieriiuri.eclesial_arrecadacoes.dto.IgrejaDTO;
import org.springframework.web.server.ResponseStatusException;


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

    @Transactional
    public void alterarSenha(AlterarSenhaRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenhaHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta.");
        }

        if (!request.getNovaSenha().equals(request.getConfirmarSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova senha e confirmação não coincidem.");
        }

        String novaSenha = request.getNovaSenha();

        if (novaSenha.length() < 6 ||
                !novaSenha.matches(".*[A-Z].*") ||
                !novaSenha.matches(".*[a-z].*") ||
                !novaSenha.matches(".*\\d.*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A nova senha deve ter pelo menos 6 caracteres, incluindo uma letra maiúscula, uma letra minúscula e um número.");
        }

        usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
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

    public UsuarioDTO toDTO(Usuario usuario) {
        Endereco endereco = usuario.getEndereco();
        Igreja igreja = usuario.getIgreja();

        EnderecoDTO enderecoDTO = new EnderecoDTO(
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getPais()
        );

        IgrejaDTO igrejaDTO = new IgrejaDTO(
                igreja.getId(),
                igreja.getNome(),
                igreja.getCnpj(),
                igreja.getCidade(),
                igreja.getEstado()
        );

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getCargo(),
                enderecoDTO,
                usuario.getFotoPerfil(),
                igrejaDTO
        );
    }

    public Usuario atualizarUsuario(String email, UsuarioDTO dto) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        // Nome, e-mail, cargo e foto podem ser atualizados normalmente
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCargo(dto.getCargo());
        usuario.setFotoPerfil(dto.getFotoPerfil());

        // ❌ CPF só pode ser incluído se ainda não existir
        if (usuario.getCpf() == null || usuario.getCpf().isBlank()) {
            usuario.setCpf(dto.getCpf());
        }

        // Atualização de endereço
        if (usuario.getEndereco() != null && dto.getEndereco() != null) {
            usuario.getEndereco().setCep(dto.getEndereco().getCep());
            usuario.getEndereco().setLogradouro(dto.getEndereco().getLogradouro());
            usuario.getEndereco().setNumero(dto.getEndereco().getNumero());
            usuario.getEndereco().setComplemento(dto.getEndereco().getComplemento());
            usuario.getEndereco().setBairro(dto.getEndereco().getBairro());
            usuario.getEndereco().setCidade(dto.getEndereco().getCidade());
            usuario.getEndereco().setEstado(dto.getEndereco().getEstado());
            usuario.getEndereco().setPais(dto.getEndereco().getPais());
        }

        // Atualização de dados da igreja (exceto ID)
        if (usuario.getIgreja() != null && dto.getIgreja() != null) {
            usuario.getIgreja().setNome(dto.getIgreja().getNome());
            usuario.getIgreja().setCidade(dto.getIgreja().getCidade());
            usuario.getIgreja().setEstado(dto.getIgreja().getEstado());

            // ❌ CNPJ só pode ser inserido se ainda for null ou vazio
            if (usuario.getIgreja().getCnpj() == null || usuario.getIgreja().getCnpj().isBlank()) {
                usuario.getIgreja().setCnpj(dto.getIgreja().getCnpj());
            }
        }

        return usuarioRepository.save(usuario);
    }


    public void excluirUsuario(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuarioRepository.delete(usuario);
    }
}