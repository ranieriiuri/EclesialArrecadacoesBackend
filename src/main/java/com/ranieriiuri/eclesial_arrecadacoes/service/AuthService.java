package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Igreja;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.IgrejaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.AuthRequest;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.AuthResponse;
import com.ranieriiuri.eclesial_arrecadacoes.security.dto.RegisterRequest;
import com.ranieriiuri.eclesial_arrecadacoes.security.jwt.JwtService;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Endereco;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.EnderecoRepository;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final JwtService jwtService;
    private final IgrejaRepository igrejaRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository, EnderecoRepository enderecoRepository,
                       JwtService jwtService, IgrejaRepository igrejaRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.jwtService = jwtService;
        this.igrejaRepository = igrejaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = jwtService.generateToken(usuario.getEmail(), usuario.getIgreja().getId());

            return new AuthResponse(token);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        // Cria e salva a Igreja
        Igreja igreja = Igreja.builder()
                .nome(request.getIgreja().getNome())
                .cnpj(request.getIgreja().getCnpj())
                .cidade(request.getIgreja().getCidade())
                .estado(request.getIgreja().getEstado())
                .criadoEm(LocalDateTime.now())
                .build();
        igreja = igrejaRepository.save(igreja);

        // Cria e salva o Endereco (se fornecido)
        Endereco endereco = null;
        if (request.getEndereco() != null) {
            endereco = Endereco.builder()
                    .cep(request.getEndereco().getCep())
                    .logradouro(request.getEndereco().getLogradouro())
                    .numero(request.getEndereco().getNumero())
                    .complemento(request.getEndereco().getComplemento())
                    .bairro(request.getEndereco().getBairro())
                    .cidade(request.getEndereco().getCidade())
                    .estado(request.getEndereco().getEstado())
                    .pais(request.getEndereco().getPais())
                    .build();
            endereco = enderecoRepository.save(endereco);
        }

        // Cria e salva o Usuário
        Usuario novoUsuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senhaHash(passwordEncoder.encode(request.getSenha()))
                .cpf(request.getCpf())
                .cargo(request.getCargo())
                .igreja(igreja)
                .endereco(endereco)
                .criadoEm(LocalDateTime.now())
                .build();

        usuarioRepository.save(novoUsuario);

        String token = jwtService.generateToken(novoUsuario.getEmail(), igreja.getId());
        return new AuthResponse(token);
    }

}
