package com.healink.integrador.security;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.healink.integrador.config.ConfiguracionJWT;
import com.healink.integrador.domain.usuario.Usuario;

import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProveedorTokenJWT {

    private final ConfiguracionJWT configJWT;
    private final UserDetailsService userDetailsService; // Inyección
    private final ListaNegraToken listaNegraToken;

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

    public String createToken(Usuario usuario) {
        SecretKey llave = Keys.hmacShaKeyFor(Base64.getDecoder().decode(configJWT.getLlaveSecreta()));

        Date ahora = new Date();
        Date validez = new Date(ahora.getTime() + configJWT.getExpiracion() * 1000);

        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("id", usuario.getId())
                .claim("roles", usuario.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .claim("nombres", usuario.getNombres())
                .claim("apellidos", usuario.getApellidos())
                .setIssuedAt(ahora)
                .setExpiration(validez)
                .signWith(llave)
                .compact();
    }

    private Claims getClaims(String token) {
        SecretKey llave = Keys.hmacShaKeyFor(Base64.getDecoder().decode(configJWT.getLlaveSecreta()));

        return Jwts.parserBuilder()
                .setSigningKey(llave)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            // Verificar primero si el token está en la lista negra
            if (listaNegraToken.estaEnListaNegra(token)) {
                return false;
            }

            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}