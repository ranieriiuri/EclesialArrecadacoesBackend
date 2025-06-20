package com.ranieriiuri.eclesial_arrecadacoes.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationMillis;

    /**
     * Gera a chave de assinatura HMAC a partir da secret.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extrai o e-mail (subject) do token JWT.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai o ID da igreja do token JWT.
     */
    public UUID extractIgrejaId(String token) {
        Claims claims = extractAllClaims(token);
        String id = claims.get("igrejaId", String.class);
        if (id == null) {
            throw new IllegalStateException("Token JWT não contém o claim 'igrejaId'");
        }
        return UUID.fromString(id);
    }

    /**
     * Extrai um claim genérico do token JWT.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Extrai todos os claims contidos no token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica se o token é válido comparando o e-mail e se está expirado.
     */
    public boolean isTokenValid(String token, String userEmail) {
        final String email = extractEmail(token);
        return (email.equals(userEmail)) && !isTokenExpired(token);
    }

    /**
     * Verifica se o token está expirado (com tolerância de 30 segundos).
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        long now = System.currentTimeMillis();
        return expiration.before(new Date(now - 30_000)); // 30s de tolerância
    }

    /**
     * Gera um token com o e-mail e o ID da igreja.
     */
    public String generateToken(String email, UUID igrejaId) {
        return generateToken(Map.of("igrejaId", igrejaId.toString()), email);
    }

    /**
     * Gera um token com claims adicionais e o e-mail do usuário.
     */
    public String generateToken(Map<String, Object> extraClaims, String email) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
