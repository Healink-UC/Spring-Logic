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

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ControladorAuth controlAuth;

    @Autowired
    PasswordEncoder passwordEncoder;

    Rol rol = null;
    Usuario usuario = null;
    EntidadSalud entidad = null;
    Localizacion localizacion = null;
    CampanaDTO campana = null;
    ObjectMapper mapper = null;

    // Nuevos objetos
    Rol nuevoRol = null;
    Usuario nuevoUsuario = null;
    EntidadSalud nuevaEntidad = null;
    Localizacion nuevaLocalizacion = null;

    // datos para el inicio de sesion
    private static final String USUARIO = "12345";
    private static final String PASSWORD = "12345";

    private static final LocalDate FECHA_INICIO = LocalDate.now();
    private static final LocalDate FECHA_FIN = LocalDate.now();
    private static final LocalDate FECHA_LIMITE = LocalDate.now();

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
                null, TipoIdentificacion.CC, USUARIO,
                "pepito", "perez", "correo@me.com", PASSWORD,
                "12123123",
                Estado.ACTIVO, rol, 1L, null, null);

        entidad = new EntidadSalud(null, "Salud Total", "Cra 123", "3123123123", "saludtotal@gmail.com", null, null);

        localizacion = new Localizacion(
                null, "Antioquia", "Medellin",
                "San Antonio", "Centro", 0.0, 0.0);

        nuevoRol = rolRepository.save(rol);
        usuario.setRolId(nuevoRol.getId());
        usuario.setRol(nuevoRol);

        nuevoUsuario = usuarioRepository.save(usuario);

        nuevaEntidad = entidadRepository.save(entidad);
        nuevaLocalizacion = localizacionRepository.save(localizacion);

    }

    // clases válidas

    @Test
    void guardarNuevaCampanaTest() {
        campana = new CampanaDTO();
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setEntidadId(nuevaEntidad.getId());
        campana.setFechaInicio(FECHA_INICIO.plusDays(2));
        campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
        campana.setFechaLimite(FECHA_FIN.plusDays(7));
        campana.setMinParticipantes(50);
        campana.setMaxParticipantes(100);
        campana.setLocalizacionId(nuevaLocalizacion.getId());
        campana.setEstado(EstadoCampana.POSTULADA);

        ResponseEntity<CampanaDTO> res = campanaController.crear(campana);
        Long idCreado = null;
        CampanaDTO body = res.getBody();
        if (body != null) {
            idCreado = body.getId();
        } else {
            throw new AssertionError("Response body is null");
        }

        ResponseEntity<CampanaDTO> expectedResponse = campanaController.buscarPorId(idCreado);

        assertNotNull(res);
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());
        assertEquals(res.getBody(), expectedResponse.getBody());
    }

    @Test
    void guardarCampanaMenorNumeroParticipantesTest() {
        // guardar hash clave
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        try {
            campana = new CampanaDTO();
            campana.setNombre("campaña prueba");
            campana.setDescripcion("descripcion de prueba");
            campana.setEntidadId(nuevaEntidad.getId());
            campana.setFechaInicio(FECHA_INICIO.plusDays(2));
            campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
            campana.setFechaLimite(FECHA_FIN.plusDays(7));
            campana.setMinParticipantes(9);
            campana.setMaxParticipantes(50);
            campana.setLocalizacionId(nuevaLocalizacion.getId());
            campana.setEstado(EstadoCampana.POSTULADA);

            // Crear un objeto de solicitud de acceso con los datos proporcionados
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, PASSWORD);
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";
            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/campana")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campana)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.minParticipantes")// Verifica el mensaje de error en
                                .value("El número mínimo de participantes de la campaña es 10"));

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

    @Test
    void guardarCampanaMayorNumeroMaxParticipantesTest() {
        // guardar hash clave
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        try {
            campana = new CampanaDTO();
            campana.setNombre("campaña prueba");
            campana.setDescripcion("descripcion de prueba");
            campana.setEntidadId(nuevaEntidad.getId());
            campana.setFechaInicio(FECHA_INICIO.plusDays(2));
            campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
            campana.setFechaLimite(FECHA_FIN.plusDays(7));
            campana.setMinParticipantes(10);
            campana.setMaxParticipantes(201);
            campana.setLocalizacionId(nuevaLocalizacion.getId());
            campana.setEstado(EstadoCampana.POSTULADA);

            // Crear un objeto de solicitud de acceso con los datos proporcionados
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, PASSWORD);
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";
            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/campana")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campana)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.maxParticipantes")// Verifica el mensaje de error en
                                .value("El número máximo de participantes de la campaña es 200."));

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

    // Clases inválidas
    @Test
    void guardarCampanaSinLocalizacionTest() {
        campana = new CampanaDTO();
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setEntidadId(nuevaEntidad.getId());
        campana.setFechaInicio(FECHA_INICIO.plusDays(2));
        campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
        campana.setFechaLimite(FECHA_FIN.plusDays(7));
        campana.setMinParticipantes(10);
        campana.setMaxParticipantes(50);
        campana.setEstado(EstadoCampana.POSTULADA);

        // guardar hash clave
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        try {
            // Crear un objeto de solicitud de acceso con los datos proporcionados
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, PASSWORD);
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";
            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/campana")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campana)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.localizacionId")// Verifica el mensaje de error en
                                .value("La campaña debe tener una localizacion")); // 'localizacionId'

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

    @Test
    void campañaConFechaInicioAnteriorHoyTest() {
        campana = new CampanaDTO();
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setEntidadId(nuevaEntidad.getId());
        campana.setFechaInicio(FECHA_INICIO.minusDays(2));
        campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
        campana.setFechaLimite(FECHA_FIN.plusDays(7));
        campana.setMinParticipantes(10);
        campana.setMaxParticipantes(50);
        campana.setLocalizacionId(nuevaLocalizacion.getId());
        campana.setEstado(EstadoCampana.POSTULADA);
        // guardar hash clave
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        try {
            // Crear un objeto de solicitud de acceso con los datos proporcionados
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, PASSWORD);
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";
            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/campana")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campana)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.fechaInicio")// Verifica el mensaje de error en
                                .value("La fecha de inicio debe ser hoy o una fecha posterior."));

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

    @Test
    void campañaFechaInicioPosteriorFechaFinTest() {
        campana = new CampanaDTO();
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setEntidadId(nuevaEntidad.getId());
        campana.setFechaInicio(FECHA_INICIO.plusDays(8));
        campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
        campana.setFechaLimite(FECHA_FIN.plusDays(5));
        campana.setMinParticipantes(10);
        campana.setMaxParticipantes(50);
        campana.setLocalizacionId(nuevaLocalizacion.getId());
        campana.setEstado(EstadoCampana.POSTULADA);
        // guardar hash clave
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        try {
            // Crear un objeto de solicitud de acceso con los datos proporcionados
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, PASSWORD);
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";
            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/campana")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campana)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.fechaInicio")// Verifica el mensaje de error
                                .value("La fecha de inicio debe ser anterior a la fecha de finalizacion de la campaña."));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

    @Test
    void campañaFechaFinalizacionAnteriorFechaInicoTest() {
        campana = new CampanaDTO();
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setEntidadId(nuevaEntidad.getId());
        campana.setFechaInicio(FECHA_INICIO.plusDays(3));
        campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
        campana.setFechaLimite(FECHA_FIN.plusDays(1));
        campana.setMinParticipantes(10);
        campana.setMaxParticipantes(50);
        campana.setLocalizacionId(nuevaLocalizacion.getId());
        campana.setEstado(EstadoCampana.POSTULADA);
        // guardar hash clave
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        try {
            // Crear un objeto de solicitud de acceso con los datos proporcionados
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, PASSWORD);
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";
            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/campana")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campana)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.fechaInicio")// Verifica el mensaje de error
                                .value("La fecha de inicio debe ser anterior a la fecha de finalizacion de la campaña."));

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

    @Test
    void campañaFechaLimiteAnteriorFechaInicioTest() {
        campana = new CampanaDTO();
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setEntidadId(nuevaEntidad.getId());
        campana.setFechaInicio(FECHA_INICIO.plusDays(1));
        campana.setFechaLimiteInscripcion(FECHA_LIMITE.minusDays(3));
        campana.setFechaLimite(FECHA_FIN.plusDays(7));
        campana.setMinParticipantes(10);
        campana.setMaxParticipantes(50);
        campana.setLocalizacionId(nuevaLocalizacion.getId());
        campana.setEstado(EstadoCampana.POSTULADA);
        // guardar hash clave
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        try {
            // Crear un objeto de solicitud de acceso con los datos proporcionados
            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, PASSWORD);
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";
            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/campana")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campana)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.fechaLimiteInscripcion")// Verifica el mensaje de error
                                .value("La fecha límite de inscripción debe ser posterior a la fecha de inicio de la campaña."));

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

}
