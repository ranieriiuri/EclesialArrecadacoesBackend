package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.TokenRecuperacaoSenha;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Usuario;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.TokenRecuperacaoSenhaRepository;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacaoSenhaRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RecuperacaoSenhaService(UsuarioRepository usuarioRepository,
                                   TokenRecuperacaoSenhaRepository tokenRepository,
                                   PasswordEncoder passwordEncoder,
                                   EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // ✅ Envia o link de recuperação
    public void solicitarRecuperacaoSenha(String email) {
        try {
            usuarioRepository.findByEmail(email).ifPresent(usuario -> {
                boolean jaExisteTokenValido = tokenRepository
                        .findByUsuarioAndUsadoFalse(usuario).stream()
                        .anyMatch(token -> token.getExpiraEm().isAfter(LocalDateTime.now()));

                if (jaExisteTokenValido) {
                    // Evita spam de tokens
                    return;
                }

                String token = UUID.randomUUID().toString();

                TokenRecuperacaoSenha novoToken = TokenRecuperacaoSenha.builder()
                        .token(token)
                        .usuario(usuario)
                        .expiraEm(LocalDateTime.now().plusHours(1))
                        .usado(false)
                        .build();

                tokenRepository.save(novoToken);

                // 🔗 Link para o front-end
                String link = "https://seu-frontend.com/redefinir-senha?token=" + token;

                String assunto = "Recuperação de Senha - Eclesial Arrecadações";
                String corpo = """
                    Olá, %s!

                    Você solicitou a recuperação de senha. Clique no link abaixo para redefinir sua senha:

                    %s

                    Este link expirará em 1 hora.

                    Se você não solicitou isso, ignore este e-mail.
                    """.formatted(usuario.getNome(), link);

                emailService.enviarEmail(usuario.getEmail(), assunto, corpo);
            });

        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(RecuperacaoSenhaService.class);
            logger.warn("Erro ao processar solicitação de recuperação para o e-mail: {}", email, e);
        }

        // Sempre responde como se tudo ocorreu normalmente
        Logger logger = LoggerFactory.getLogger(RecuperacaoSenhaService.class);
        logger.info("Solicitação de recuperação de senha processada para o e-mail: {}", email);
    }

    // ✅ Redefine a senha com base no token
    public void redefinirSenha(String token, String novaSenha) {
        TokenRecuperacaoSenha tokenRecuperacao = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        if (tokenRecuperacao.isUsado()) {
            throw new IllegalArgumentException("Este token já foi utilizado.");
        }

        if (tokenRecuperacao.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Este token expirou.");
        }

        Usuario usuario = tokenRecuperacao.getUsuario();
        usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        // Marca o token como usado
        tokenRecuperacao.setUsado(true);
        tokenRepository.save(tokenRecuperacao);
    }
}
