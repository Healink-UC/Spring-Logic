package com.healink.integrador.campana;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.healink.integrador.domain.campana.CampanaController;
import com.healink.integrador.domain.campana.CampanaDTO;
import com.healink.integrador.domain.campana.EstadoCampana;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;
import com.healink.integrador.domain.entidades_salud.EntidadSaludRepository;
import com.healink.integrador.domain.localizacion.Localizacion;
import com.healink.integrador.domain.localizacion.LocalizacionRepository;
import com.healink.integrador.domain.rol.Rol;
import com.healink.integrador.domain.rol.RolRepository;
import com.healink.integrador.domain.usuario.Estado;
import com.healink.integrador.domain.usuario.TipoIdentificacion;
import com.healink.integrador.domain.usuario.Usuario;
import com.healink.integrador.domain.usuario.UsuarioRepository;
import com.healink.integrador.security.ControladorAuth;
import com.healink.integrador.security.SolicitudAcceso;

import java.time.LocalDate;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class CampanaTest {
    // entidades necesarias
    @Autowired
    private CampanaController campanaController;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntidadSaludRepository entidadRepository;

    @Autowired
    private LocalizacionRepository localizacionRepository;

    Rol rol = null;
    Usuario usuario = null;
    EntidadSalud entidad = null;
    Localizacion localizacion = null;
    CampanaDTO campana = null;
    ObjectMapper mapper = null;

    // Este método se ejecutará antes de cada test
    @BeforeEach
    void setup() {
        // Eliminar todas las entidades previas si es necesario
        rolRepository.deleteAll();
        usuarioRepository.deleteAll();
        entidadRepository.deleteAll();
        localizacionRepository.deleteAll();

        // pre Configuraciones
        mapper = new ObjectMapper();
        JsonNode permisos;
        // crear un json para la prueba, comprobando una posible excepcion
        {
            try {
                permisos = mapper.readTree("{\"crear\": true, \"editar\": false}");
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error initializing permisos JSON", e);
            }
        }

        rol = new Rol(null, "administrador", "una descripcion", permisos);
        usuario = new Usuario(
                null, TipoIdentificacion.CC, "124343",
                "pepito", "perez", "correo@me.com", "12345",
                "12123123",
                Estado.ACTIVO, rol, 1L);
        entidad = new EntidadSalud(
                null, "una razon social",
                1L,
                usuario);
        localizacion = new Localizacion(
                null, "Caldas", "Manizales",
                "una vereda", "una localidad", 0.0, 0.0);

    }

    @Test
    void guardarNuevaCampanaTest() {
        rolRepository.save(rol);
        usuarioRepository.save(usuario);
        entidadRepository.save(entidad);
        localizacionRepository.save(localizacion);

        campana = new CampanaDTO();
        campana.setId(1L);
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setEntidadId(1L);
        campana.setFechaInicio(LocalDate.parse("2025-05-05"));
        campana.setFechaLimite(LocalDate.parse("2025-05-12"));
        campana.setFechaLimiteInscripcion(LocalDate.parse("2025-05-04"));
        campana.setMinParticipantes(10);
        campana.setMaxParticipantes(50);
        campana.setLocalizacionId(1L);
        campana.setEstado(EstadoCampana.POSTULADA);

        ResponseEntity<CampanaDTO> res = campanaController.crear(campana);

        ResponseEntity<CampanaDTO> expectedResponse = campanaController.buscarPorId(1L);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());
        assertEquals(res.getBody(), expectedResponse.getBody());
    }

    @Test
    void guardarCampanaSinLocalizacionTest() {

    }
}
