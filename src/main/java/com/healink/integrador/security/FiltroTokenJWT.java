package com.healink.integrador.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.io.IOException;

@Component
public class FiltroTokenJWT extends OncePerRequestFilter {

    private final ProveedorTokenJWT proveedorTokenJWT;

    // Añadir constructor para la inyección de dependencias
    public FiltroTokenJWT(ProveedorTokenJWT proveedorTokenJWT) {
        this.proveedorTokenJWT = proveedorTokenJWT;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token != null && proveedorTokenJWT.validateToken(token)) {
            // En lugar de cargar el usuario aquí, usa las claims del token
            Authentication authentication = proveedorTokenJWT.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}