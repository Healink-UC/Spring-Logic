package com.healink.integrador.domain.atenciones_medicas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healink.integrador.domain.campana.*;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedicaRepository;
import com.healink.integrador.domain.citaciones_medicas.EstadoCitacion;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;
import com.healink.integrador.domain.entidades_salud.EntidadSaludRepository;
import com.healink.integrador.domain.localizacion.Localizacion;
import com.healink.integrador.domain.localizacion.LocalizacionRepository;
import com.healink.integrador.domain.paciente.*;
import com.healink.integrador.domain.personal_medico.PersonalMedico;
import com.healink.integrador.domain.personal_medico.PersonalMedicoRepository;
import com.healink.integrador.domain.rol.Rol;
import com.healink.integrador.domain.rol.RolRepository;
import com.healink.integrador.domain.usuario.*;
import com.healink.integrador.security.ControladorAuth;
import com.healink.integrador.security.SolicitudAcceso;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AtencionMedicaTest {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EntidadSaludRepository entidadRepository;
    @Autowired
    private LocalizacionRepository localizacionRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private CampanaRepository campanaRepository;
    @Autowired
    private PersonalMedicoRepository personalMedicoRepository;
    @Autowired
    private CitacionMedicaRepository citacionMedicaRepository;
    @Autowired
    private AtencionMedicaController atencionMedicaController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ControladorAuth controlAuth;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String USUARIO = "12345";
    private static String PASSWORD = "12345";
    private static final LocalDate FECHA_INICIO = LocalDate.now();
    private static final LocalDate FECHA_FIN = LocalDate.now();
    private static final LocalDate FECHA_LIMITE = LocalDate.now();

    Rol rol, rolMedico, rolPaciente, nuevoRol, nuevoRolMedico, nuevoRolPaciente;
    Usuario usuario, usuarioMedico, usuarioPaciente, nuevoUsuario, nuevoUsuarioMedico, nuevoUsuarioPaciente;
    EntidadSalud entidad, nuevaEntidad;
    Localizacion localizacion, nuevaLocalizacion;
    PersonalMedico personalMedico, nuevoPersonalMedico;
    Campana campana, nuevaCampana;
    Paciente paciente, nuevoPaciente;
    CitacionMedica citacionMedica, nuevaCitacionMedica;
    AtencionMedicaDTO atencionMedicaDTO;

    @BeforeEach
    void setup() {
        // Eliminar todas las entidades previas si es necesario
        citacionMedicaRepository.deleteAll();
        campanaRepository.deleteAll();
        entidadRepository.deleteAll();
        personalMedicoRepository.deleteAll();
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();
        localizacionRepository.deleteAll();
        pacienteRepository.deleteAll();
        PASSWORD = passwordEncoder.encode(PASSWORD);
    }

    @Test
    void crearRolesTest() throws JsonProcessingException {
        JsonNode permisos = objectMapper.readTree("{\"crear\": true, \"editar\": false}");
        rol = new Rol(null, "administrador", "una descripcion", permisos);
        rolMedico = new Rol(null, "medico", "Medico", permisos);
        rolPaciente = new Rol(null, "paciente", "Paciente", permisos);

        nuevoRol = rolRepository.save(rol);
        nuevoRolMedico = rolRepository.save(rolMedico);
        nuevoRolPaciente = rolRepository.save(rolPaciente);

        assertNotNull(nuevoRol.getId());
        assertNotNull(nuevoRolMedico.getId());
        assertNotNull(nuevoRolPaciente.getId());
    }

    @Test
    void crearUsuarioTest() throws JsonProcessingException {
        crearRolesTest(); // dependencia

        usuario = new Usuario(null, TipoIdentificacion.CC, USUARIO,
                "pepito", "perez", "correo@me.com", PASSWORD,
                "12123123", Estado.ACTIVO, nuevoRol, nuevoRol.getId(), null, null);

        nuevoUsuario = usuarioRepository.save(usuario);
        assertNotNull(nuevoUsuario.getId());
    }

    @Test
    void crearEntidadSaludTest() throws JsonProcessingException {
        crearUsuarioTest(); // dependencia

        entidad = new EntidadSalud(null, "una razon social", null);
        nuevaEntidad = entidadRepository.save(entidad);
        assertNotNull(nuevaEntidad.getId());
    }

    @Test
    void crearLocalizacionTest() {
        localizacion = new Localizacion(null, "Antioquia", "Medellin", "San Antonio", "Centro", 0.0, 0.0);
        nuevaLocalizacion = localizacionRepository.save(localizacion);
        assertNotNull(nuevaLocalizacion.getId());
    }

    @Test
    void crearPersonalMedicoTest() throws JsonProcessingException {
        crearRolesTest();
        // crearEntidadSaludTest();

        usuarioMedico = new Usuario(null, TipoIdentificacion.CC, "CarlosHerrera",
                "Carlos", "Herrera", "carlosH@gmail.com", PASSWORD,
                "4454444644", Estado.ACTIVO, nuevoRolMedico, nuevoRolMedico.getId(), null, null);

        nuevoUsuarioMedico = usuarioRepository.save(usuarioMedico);

        personalMedico = new PersonalMedico();
        personalMedico.setEspecialidad("CARDIOLOGIA");
        personalMedico.setUsuarioId(nuevoUsuarioMedico.getId());
        personalMedico.setEntidadId(nuevaEntidad.getId());

        nuevoPersonalMedico = personalMedicoRepository.save(personalMedico);
        assertNotNull(nuevoPersonalMedico.getId());
    }

    @Test
    void crearPacienteTest() throws JsonProcessingException {
        crearRolesTest();
        crearLocalizacionTest();

        usuarioPaciente = new Usuario(null, TipoIdentificacion.CC, "JuanPerez",
                "Juan", "Perez", "juanP@gmail.com", PASSWORD,
                "4454444644", Estado.ACTIVO, nuevoRolPaciente, nuevoRolPaciente.getId(), null, null);

        nuevoUsuarioPaciente = usuarioRepository.save(usuarioPaciente);

        paciente = new Paciente();
        paciente.setUsuarioId(nuevoUsuarioPaciente.getId());
        paciente.setLocalizacionId(nuevaLocalizacion.getId());
        paciente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        paciente.setGenero(GeneroBiologico.MASCULINO);
        paciente.setTipoSangre(TipoSangre.A_POSITIVO);
        paciente.setDireccion("Calle 123");

        nuevoPaciente = pacienteRepository.save(paciente);
        assertNotNull(nuevoPaciente.getId());
    }

    @Test
    void crearCampanaTest() throws JsonProcessingException {
        crearEntidadSaludTest();
        crearLocalizacionTest();

        campana = new Campana();
        campana.setNombre("campaña prueba");
        campana.setDescripcion("descripcion de prueba");
        campana.setFechaInicio(FECHA_INICIO.plusDays(2));
        campana.setFechaLimiteInscripcion(FECHA_LIMITE.plusDays(1));
        campana.setMinParticipantes(50);
        campana.setMaxParticipantes(100);
        campana.setEstado(EstadoCampana.POSTULADA);
        campana.setEntidadId(nuevaEntidad.getId());
        campana.setLocalizacionId(nuevaLocalizacion.getId());

        nuevaCampana = campanaRepository.save(campana);
        assertNotNull(nuevaCampana.getId());
    }

    @Test
    void crearCitacionMedicaTest() throws JsonProcessingException {
        crearPacienteTest();
        crearCampanaTest();
        crearPersonalMedicoTest();

        citacionMedica = new CitacionMedica();
        citacionMedica.setPacienteId(nuevoPaciente.getId());
        citacionMedica.setCampanaId(nuevaCampana.getId());
        citacionMedica.setMedicoId(nuevoPersonalMedico.getId());
        citacionMedica.setHoraProgramada(LocalDateTime.now());
        citacionMedica.setHoraAtencion(LocalDateTime.now());
        citacionMedica.setEstado(EstadoCitacion.AGENDADA);
        citacionMedica.setPrediccionAsistencia(BigDecimal.valueOf(70));
        citacionMedica.setDuracionEstimada(8);

        nuevaCitacionMedica = citacionMedicaRepository.save(citacionMedica);
        assertNotNull(nuevaCitacionMedica.getId());
    }

    @Test
    void crearCitacionMedicaTestEstadoIncorrecto() throws JsonProcessingException {
        crearPacienteTest();
        crearCampanaTest();
        crearPersonalMedicoTest();

        citacionMedica = new CitacionMedica();
        citacionMedica.setPacienteId(nuevoPaciente.getId());
        citacionMedica.setCampanaId(nuevaCampana.getId());
        citacionMedica.setMedicoId(nuevoPersonalMedico.getId());
        citacionMedica.setHoraProgramada(LocalDateTime.now());
        citacionMedica.setHoraAtencion(LocalDateTime.now());
        citacionMedica.setEstado(EstadoCitacion.ATENDIDA);
        citacionMedica.setPrediccionAsistencia(BigDecimal.valueOf(70));
        citacionMedica.setDuracionEstimada(8);

        nuevaCitacionMedica = citacionMedicaRepository.save(citacionMedica);
        assertNotNull(nuevaCitacionMedica.getId());
    }

    @Test
    @DisplayName("CP-MED-01: 1-4-7-10 Atención médica completa.")
    void guardarNuevaAtencionMedicaTest() throws JsonProcessingException {
        crearCitacionMedicaTest();

        atencionMedicaDTO = new AtencionMedicaDTO();
        atencionMedicaDTO.setCitacionId(nuevaCitacionMedica.getId());
        atencionMedicaDTO.setFechaHoraInicio(Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
        atencionMedicaDTO.setFechaHoraFin(Timestamp.valueOf(LocalDateTime.now()));
        atencionMedicaDTO.setDuracionReal(5);
        atencionMedicaDTO.setEstado(EstadoAtencionMedica.EN_PROCESO.name());

        ResponseEntity<AtencionMedicaDTO> res = atencionMedicaController.crear(atencionMedicaDTO);
        AtencionMedicaDTO body = res.getBody();
        assertNotNull(body);

        ResponseEntity<AtencionMedicaDTO> expected = atencionMedicaController.buscarPorId(body.getId());
        CitacionMedica citacionMedica = citacionMedicaRepository.findById(body.getCitacionId()).orElse(null);

        assertEquals(HttpStatus.OK, expected.getStatusCode());
        assertEquals(EstadoCitacion.ATENDIDA, citacionMedica.getEstado());
    }

    @Test
    @DisplayName("CP-MED-02: 1-2-4-7-10 Atención con citación en estado incorrecto.")
    void guardarNuevaAtencionMedicaIncompletaTest() throws JsonProcessingException {
        crearCitacionMedicaTestEstadoIncorrecto();

        atencionMedicaDTO = new AtencionMedicaDTO();
        atencionMedicaDTO.setCitacionId(nuevaCitacionMedica.getId());
        atencionMedicaDTO.setFechaHoraInicio(Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
        atencionMedicaDTO.setFechaHoraFin(Timestamp.valueOf(LocalDateTime.now()));
        atencionMedicaDTO.setDuracionReal(5);
        atencionMedicaDTO.setEstado(EstadoAtencionMedica.EN_PROCESO.name());

        try {
            ResponseEntity<AtencionMedicaDTO> res = atencionMedicaController.crear(atencionMedicaDTO);
            fail("Se esperaba una excepción");
        } catch (Exception e) {
            assertEquals("La citación no está agendada o ya se ha atendido", e.getMessage());
        }
    }

    @Test
    @DisplayName("CP-MED-05: 1-6-7-10 Atención con fecha-hora de inicio mayor a fecha-hora de fin")
    void guardarNuevaAtencionMedicaFechaHoraInicioMayorAlLimiteSuperiorTest() throws JsonProcessingException {
        crearCitacionMedicaTest();
        try {

            atencionMedicaDTO = new AtencionMedicaDTO();
            atencionMedicaDTO.setCitacionId(nuevaCitacionMedica.getId());
            atencionMedicaDTO.setFechaHoraInicio(Timestamp.valueOf(LocalDateTime.now().plusDays(15)));
            atencionMedicaDTO.setFechaHoraFin(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));
            atencionMedicaDTO.setDuracionReal(5);
            atencionMedicaDTO.setEstado(EstadoAtencionMedica.EN_PROCESO.name());

            ObjectMapper mapper = new ObjectMapper();

            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, "12345");
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";

            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/atenciones_medicas/crear")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atencionMedicaDTO)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.fechaHoraInicio")// Verifica el mensaje de error en
                                .value("La fecha de inicio debe ser anterior a la fecha de finalizacion de la atencion medica."));

            } else {
                fail("No se pudo obtener el token" + res.getStatusCode().value());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("CP-MED-09: 1-4-7-12 Atención con duración demasiado larga")
    void guardarNuevaAtencionMedicaFechaHoraFinMenorAlLimiteInferiorTest() throws JsonProcessingException {
        crearCitacionMedicaTest();
        try {
            atencionMedicaDTO = new AtencionMedicaDTO();
            atencionMedicaDTO.setCitacionId(nuevaCitacionMedica.getId());
            atencionMedicaDTO.setFechaHoraInicio(Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
            atencionMedicaDTO.setFechaHoraFin(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));
            atencionMedicaDTO.setDuracionReal(61);
            atencionMedicaDTO.setEstado(EstadoAtencionMedica.EN_PROCESO.name());

            ObjectMapper mapper = new ObjectMapper();

            SolicitudAcceso solicitudAcceso = new SolicitudAcceso(TipoIdentificacion.CC, USUARIO, "12345");
            ResponseEntity<?> res = controlAuth.login(solicitudAcceso);
            String token = "";

            if (res.getStatusCode().value() == 200) {
                // Obtener el body como objeto genérico
                Object body = res.getBody();
                // Convertirlo a JSON
                JsonNode json = mapper.convertValue(body, JsonNode.class);

                // Acceder al atributo "token"
                token = json.get("token").asText();

                mockMvc.perform(post("/api/atenciones_medicas/crear")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atencionMedicaDTO)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()) // Verifica código de estado es 400 (Bad Request)
                        .andExpect(jsonPath("$.validationErrors.duracionReal")// Verifica el mensaje de error en
                                .value("La duración real debe ser menor o igual a 60 minutos"));

            } else {
                fail("No se pudo obtener el token" + res.getStatusCode().value());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e);
        }
    }

}
