package com.ranieriiuri.eclesial_arrecadacoes.security.jwt;

import com.ranieriiuri.eclesial_arrecadacoes.security.details.UsuarioDetailsService;
import com.ranieriiuri.eclesial_arrecadacoes.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // <-- importa SLF4J
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class); //usando o logger do spring p lidar com msgs de erro, ao ínves de 'system.out...'
    private final JwtService jwtService;
    private final UsuarioDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header ausente ou inválido."); //Aplicando o logger do spring
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String email = jwtService.extractEmail(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(jwt, email)) {
                    // Define o tenant a partir do token
                    TenantContext.setCurrentTenant(jwtService.extractIgrejaId(jwt));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao processar JWT: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
