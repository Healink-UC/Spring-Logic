package com.healink.integrador.domain.triaje_riesgo_cardiaco;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.healink.integrador.domain.paciente.Paciente;
import com.healink.integrador.domain.triaje.NivelPrioridad;
import com.healink.integrador.domain.triaje.Triaje;
import com.healink.integrador.domain.triaje.TriajeRepository;
import com.healink.integrador.domain.triaje.TriajeService;

@ExtendWith(MockitoExtension.class)
class TriajeYRiesgoServiceTest {

    @Mock
    private TriajeRepository triajeRepository;

    @InjectMocks
    private TriajeService triajeService;

    private Triaje triajeMock;
    private Paciente pacienteMock;

    @BeforeEach
    void setUp() {
        // Configurar paciente mock
        pacienteMock = new Paciente();
        pacienteMock.setId(1L);

        // Configurar triaje mock válido (CP-TRIAJ-01)
        triajeMock = new Triaje();
        triajeMock.setId(1L);
        triajeMock.setPacienteId(1L);
        triajeMock.setEdad(45);
        triajeMock.setTabaquismo(true);
        triajeMock.setAlcoholismo(false);
        triajeMock.setDiabetes(false);
        triajeMock.setDolorPecho(false);
        triajeMock.setDolorIrradiado(false);
        triajeMock.setSudoracion(false);
        triajeMock.setNauseas(false);
        triajeMock.setAntecedentesCardiacos(false);
        triajeMock.setResultadoRiesgoCardiovascular(25.5f);
        triajeMock.setNivelPrioridad(NivelPrioridad.MEDIA);
        triajeMock.setPaciente(pacienteMock);
    }

    // CP-TRIAJ-01: Triaje completo con todos los datos válidos
    @Test
    @DisplayName("CP-TRIAJ-01: Triaje completo con todos los datos válidos")
    void guardarTriajeCompleto_conDatosValidos_debeGuardarCorrectamente() {
        // Arrange
        when(triajeRepository.save(triajeMock)).thenReturn(triajeMock);

        // Act
        Triaje resultado = triajeService.guardar(triajeMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(triajeMock.getId(), resultado.getId());
        assertEquals(triajeMock.getResultadoRiesgoCardiovascular(), resultado.getResultadoRiesgoCardiovascular());
        assertEquals(triajeMock.getNivelPrioridad(), resultado.getNivelPrioridad());

        // Verify
        verify(triajeRepository).save(triajeMock);
    }

    // CP-TRIAJ-09: Edad = 18 años (límite inferior válido)
    @Test
    @DisplayName("CP-TRIAJ-09: Edad = 18 años (límite inferior válido)")
    void guardarTriaje_conEdad18_debeGuardarCorrectamente() {
        // Arrange
        triajeMock.setEdad(18);
        when(triajeRepository.save(triajeMock)).thenReturn(triajeMock);

        // Act
        Triaje resultado = triajeService.guardar(triajeMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(18, resultado.getEdad());

        // Verify
        verify(triajeRepository).save(triajeMock);
    }

    // CP-TRIAJ-11: Edad = 100 años (límite superior válido)
    @Test
    @DisplayName("CP-TRIAJ-11: Edad = 100 años (límite superior válido)")
    void guardarTriaje_conEdad100_debeGuardarCorrectamente() {
        // Arrange
        triajeMock.setEdad(100);
        when(triajeRepository.save(triajeMock)).thenReturn(triajeMock);

        // Act
        Triaje resultado = triajeService.guardar(triajeMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(100, resultado.getEdad());

        // Verify
        verify(triajeRepository).save(triajeMock);
    }

    // Prueba para verificar búsqueda por ID
    @Test
    @DisplayName("Buscar triaje por ID debe retornar el triaje correcto")
    void buscarTriajePorId_debeRetornarTriajeCorrecto() {
        // Arrange
        when(triajeRepository.findById(1L)).thenReturn(Optional.of(triajeMock));

        // Act
        Optional<Triaje> resultado = triajeService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(triajeMock.getId(), resultado.get().getId());

        // Verify
        verify(triajeRepository).findById(1L);
    }
}