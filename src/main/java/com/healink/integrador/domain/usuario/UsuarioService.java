package com.healink.integrador.domain.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.GenericService;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService extends GenericService<Usuario> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getClave() != null && !usuario.getClave().startsWith("$2a$")) {
            usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        }
        return super.save(usuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByTipoIdentificacionAndIdentificacion(String tipoIdentificacion,
            String identificacion) {
        return usuarioRepository.findByTipoIdentificacionAndIdentificacion(tipoIdentificacion, identificacion);
    }
}