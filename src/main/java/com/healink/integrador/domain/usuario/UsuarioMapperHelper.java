package com.healink.integrador.domain.usuario;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioMapperHelper {

    private final UsuarioRepository usuarioRepository;

    /**
     * Convierte un string de auditoría como "CC:1002643012" al ID del usuario correspondiente
     * @param creadoPorString String en formato "TIPO:IDENTIFICACION"
     * @return ID del usuario o null si no se encuentra
     */
    public Long convertirCreadoPorAId(String creadoPorString) {
        if (creadoPorString == null || creadoPorString.trim().isEmpty()) {
            return null;
        }

        try {
            // Separar el string por ":"
            String[] partes = creadoPorString.split(":");
            if (partes.length != 2) {
                return null;
            }

            String tipoStr = partes[0].trim();
            String identificacion = partes[1].trim();

            // Convertir el string del tipo a enum
            TipoIdentificacion tipo;
            try {
                tipo = TipoIdentificacion.valueOf(tipoStr);
            } catch (IllegalArgumentException e) {
                return null;
            }

            // Buscar el usuario en la base de datos
            return usuarioRepository.findByTipoIdentificacionAndIdentificacion(tipo, identificacion)
                    .map(Usuario::getId)
                    .orElse(null);

        } catch (Exception e) {
            // En caso de cualquier error, devolver null
            return null;
        }
    }
} 