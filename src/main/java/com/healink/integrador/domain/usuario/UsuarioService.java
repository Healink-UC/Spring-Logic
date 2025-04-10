package com.healink.integrador.domain.usuario;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;
import com.healink.integrador.domain.rol.RolRepository;
import com.healink.integrador.enums.TipoIdentificacion;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService extends ServicioGenerico<Usuario> implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository; // Añade esta dependencia

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            RolRepository rolRepository) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // Codificar clave solo si es nueva o ha cambiado
        if (usuario.getClave() != null && !usuario.getClave().isEmpty() && !usuario.getClave().startsWith("$2a$")) {
            usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        }

        // Verificar correo único
        if (usuario.getId() == null && usuario.getCorreo() != null &&
                usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalStateException("El correo ya está registrado");
        }

        // Cargar el Rol completo si solo viene el ID
        if (usuario.getRol() != null && usuario.getRol().getId() != null) {
            usuario.setRol(rolRepository.findById(usuario.getRol().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Rol no encontrado con ID: " + usuario.getRol().getId())));
        }

        return super.guardar(usuario);
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByTipoIdentificacionAndIdentificacion(
            TipoIdentificacion tipoIdentificacion,
            String identificacion) {
        return usuarioRepository.findByTipoIdentificacionAndIdentificacion(tipoIdentificacion,
                identificacion);
    }

    // Implementación de UserDetailsService para autenticación
    // Este método se llama cuando se intenta autenticar un usuario

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] parts = username.split(":");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Formato de identificación inválido");
        }
        try {
            TipoIdentificacion tipoIdentificacion = TipoIdentificacion.fromValue(parts[0]);
            String identificacion = parts[1];

            return usuarioRepository
                    .findByTipoIdentificacionAndIdentificacion(tipoIdentificacion, identificacion)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Tipo de identificación inválido", e);
        }
    }
}