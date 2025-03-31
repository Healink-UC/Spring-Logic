package com.healink.integrador.security;

import com.healink.integrador.domain.usuario.UsuarioDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespuestaJWT {
    private String token;
    private UsuarioDTO usuario;
}