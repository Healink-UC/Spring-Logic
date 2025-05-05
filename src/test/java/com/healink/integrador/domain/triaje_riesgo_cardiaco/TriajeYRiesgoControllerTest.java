package com.healink.integrador.domain.triaje_riesgo_cardiaco;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.healink.integrador.domain.triaje.NivelPrioridad;
import com.healink.integrador.domain.triaje.Triaje;
import com.healink.integrador.domain.triaje.TriajeController;
import com.healink.integrador.domain.triaje.TriajeDTO;
import com.healink.integrador.domain.triaje.TriajeMapper;
import com.healink.integrador.domain.triaje.TriajeService;

@ExtendWith(MockitoExtension.class)
class TriajeYRiesgoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TriajeService triajeService;

    @Mock
    private TriajeMapper triajeMapper;

    @InjectMocks
    private TriajeController triajeController;

    private ObjectMapper objectMapper;
    private TriajeDTO triajeDTOMock;
    private Triaje triajeMock;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc sin cargar el contexto de Spring
        mockMvc = MockMvcBuilders.standaloneSetup(triajeController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configurar DTO mock válido (CP-TRIAJ-01)
        triajeDTOMock = new TriajeDTO();
        triajeDTOMock.setId(1L);
        triajeDTOMock.setPacienteId(1L);
        triajeDTOMock.setFechaTriaje(LocalDate.now());
        triajeDTOMock.setEdad(45);
        triajeDTOMock.setPresionSistolica(140.0f);
        triajeDTOMock.setPresionDiastolica(85.0f);
        triajeDTOMock.setColesterolTotal(210.0f);
        triajeDTOMock.setHdl(45.0f);
        triajeDTOMock.setTabaquismo(true);
        triajeDTOMock.setAlcoholismo(false);
        triajeDTOMock.setDiabetes(false);
        triajeDTOMock.setPeso(70.0f);
        triajeDTOMock.setTalla(1.75f);
        triajeDTOMock.setImc(22.9f);
        triajeDTOMock.setDolorPecho(false);
        triajeDTOMock.setDolorIrradiado(false);
        triajeDTOMock.setSudoracion(false);
        triajeDTOMock.setNauseas(false);
        triajeDTOMock.setAntecedentesCardiacos(false);
        triajeDTOMock.setResultadoRiesgoCardiovascular(25.5f);
        triajeDTOMock.setNivelPrioridad(NivelPrioridad.MEDIA);

        // Configurar entidad mock
        triajeMock = new Triaje();
        triajeMock.setId(1L);
        triajeMock.setPacienteId(1L);
        triajeMock.setFechaTriaje(LocalDate.now());
        triajeMock.setEdad(45);
        triajeMock.setPresionSistolica(140.0f);
        triajeMock.setPresionDiastolica(85.0f);
        triajeMock.setColesterolTotal(210.0f);
        triajeMock.setHdl(45.0f);
        triajeMock.setTabaquismo(true);
        triajeMock.setAlcoholismo(false);
        triajeMock.setDiabetes(false);
        triajeMock.setPeso(70.0f);
        triajeMock.setTalla(1.75f);
        triajeMock.setImc(22.9f);
        triajeMock.setDolorPecho(false);
        triajeMock.setDolorIrradiado(false);
        triajeMock.setSudoracion(false);
        triajeMock.setNauseas(false);
        triajeMock.setAntecedentesCardiacos(false);
        triajeMock.setResultadoRiesgoCardiovascular(25.5f);
        triajeMock.setNivelPrioridad(NivelPrioridad.MEDIA);
    }

    // CP-TRIAJ-01: Triaje completo con todos los datos válidos
    @Test
    @DisplayName("CP-TRIAJ-01: Triaje completo con todos los datos válidos")
    void crearTriaje_conDatosValidos_debeRetornarCreado() throws Exception {
        // Arrange
        when(triajeMapper.aEntidad(any(TriajeDTO.class))).thenReturn(triajeMock);
        when(triajeService.guardar(any(Triaje.class))).thenReturn(triajeMock);
        when(triajeMapper.aDTO(any(Triaje.class))).thenReturn(triajeDTOMock);

        // Act & Assert
        mockMvc.perform(post("/api/triaje")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(triajeDTOMock)))
                .andExpect(status().isCreated());

        // Verify
        verify(triajeMapper).aEntidad(any(TriajeDTO.class));
        verify(triajeService).guardar(any(Triaje.class));
        verify(triajeMapper).aDTO(any(Triaje.class));
    }

    // CP-TRIAJ-09: Edad = 18 años (límite inferior válido)
    @Test
    @DisplayName("CP-TRIAJ-09: Crear triaje con edad = 18 años debe ser válido")
    void crearTriaje_conEdad18_debeSerValido() throws Exception {
        // Arrange
        triajeDTOMock.setEdad(18);
        triajeMock.setEdad(18);

        when(triajeMapper.aEntidad(any(TriajeDTO.class))).thenReturn(triajeMock);
        when(triajeService.guardar(any(Triaje.class))).thenReturn(triajeMock);
        when(triajeMapper.aDTO(any(Triaje.class))).thenReturn(triajeDTOMock);

        // Act & Assert
        mockMvc.perform(post("/api/triaje")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(triajeDTOMock)))
                .andExpect(status().isCreated());
    }

    // CP-TRIAJ-13: Presión arterial = 90 mmHg (límite inferior válido)
    @Test
    @DisplayName("CP-TRIAJ-13: Crear triaje con presión arterial = 90 mmHg debe ser válido")
    void crearTriaje_conPresionArterial90_debeSerValido() throws Exception {
        // Arrange
        triajeDTOMock.setPresionSistolica(90.0f);
        triajeMock.setPresionSistolica(90.0f);

        when(triajeMapper.aEntidad(any(TriajeDTO.class))).thenReturn(triajeMock);
        when(triajeService.guardar(any(Triaje.class))).thenReturn(triajeMock);
        when(triajeMapper.aDTO(any(Triaje.class))).thenReturn(triajeDTOMock);

        // Act & Assert
        mockMvc.perform(post("/api/triaje")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(triajeDTOMock)))
                .andExpect(status().isCreated());
    }

    // CP-TRIAJ-15: Presión arterial = 200 mmHg (límite superior válido)
    @Test
    @DisplayName("CP-TRIAJ-15: Crear triaje con presión arterial = 200 mmHg debe ser válido")
    void crearTriaje_conPresionArterial200_debeSerValido() throws Exception {
        // Arrange
        triajeDTOMock.setPresionSistolica(200.0f);
        triajeMock.setPresionSistolica(200.0f);

        when(triajeMapper.aEntidad(any(TriajeDTO.class))).thenReturn(triajeMock);
        when(triajeService.guardar(any(Triaje.class))).thenReturn(triajeMock);
        when(triajeMapper.aDTO(any(Triaje.class))).thenReturn(triajeDTOMock);

        // Act & Assert
        mockMvc.perform(post("/api/triaje")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(triajeDTOMock)))
                .andExpect(status().isCreated());
    }
}