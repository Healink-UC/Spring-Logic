package com.healink.integrador.domain.triaje_riesgo_cardiaco;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.healink.integrador.domain.triaje.NivelPrioridad;
import com.healink.integrador.domain.triaje.TriajeDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TriajeYRiesgoDTOTest {

    private Validator validator;
    private TriajeDTO triajeDTO;

    @BeforeEach
    void setUp() {
        // Configurar validador
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Configurar TriajeDTO válido
        triajeDTO = new TriajeDTO();
        triajeDTO.setId(1L);
        triajeDTO.setPacienteId(1L);
        triajeDTO.setFechaTriaje(LocalDate.now());
        triajeDTO.setEdad(45);
        triajeDTO.setPresionSistolica(140.0f);
        triajeDTO.setPresionDiastolica(85.0f);
        triajeDTO.setColesterolTotal(210.0f);
        triajeDTO.setHdl(45.0f);
        triajeDTO.setTabaquismo(true);
        triajeDTO.setAlcoholismo(false);
        triajeDTO.setDiabetes(false);
        triajeDTO.setPeso(70.0f);
        triajeDTO.setTalla(1.75f);
        triajeDTO.setImc(22.9f);
        triajeDTO.setDolorPecho(false);
        triajeDTO.setDolorIrradiado(false);
        triajeDTO.setSudoracion(false);
        triajeDTO.setNauseas(false);
        triajeDTO.setAntecedentesCardiacos(false);
        triajeDTO.setResultadoRiesgoCardiovascular(25.5f);
        triajeDTO.setNivelPrioridad(NivelPrioridad.MEDIA);
    }

    // CP-TRIAJ-01: Triaje completo con todos los datos válidos
    @Test
    @DisplayName("CP-TRIAJ-01: Triaje completo con todos los datos válidos")
    void triaje_conTodosLosDatosValidos_noDebeProducirViolaciones() {
        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para un triaje válido");
    }

    // CP-TRIAJ-09: Edad = 18 años (límite inferior válido)
    @Test
    @DisplayName("CP-TRIAJ-09: Edad = 18 años (límite inferior válido)")
    void triaje_conEdad18_noDebeProducirViolaciones() {
        // Arrange
        triajeDTO.setEdad(18);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para edad = 18");
    }

    // CP-TRIAJ-10: Edad = 17 años (probablemente bajo el límite, pero no validado a
    // nivel DTO)
    @Test
    @DisplayName("CP-TRIAJ-10: Edad = 17 años (probablemente bajo el límite, pero no validado a nivel DTO)")
    void triaje_conEdad17_noDebeProducirViolaciones() {
        // Arrange
        triajeDTO.setEdad(17);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        // Nota: A nivel de DTO, no hay validación específica para el valor mínimo de
        // edad
        // Esta validación debería hacerse a nivel de servicio
        assertTrue(violations.isEmpty(), "No hay validación específica para edad mínima a nivel DTO");
    }

    // CP-TRIAJ-11: Edad = 100 años (límite superior válido)
    @Test
    @DisplayName("CP-TRIAJ-11: Edad = 100 años (límite superior válido)")
    void triaje_conEdad100_noDebeProducirViolaciones() {
        // Arrange
        triajeDTO.setEdad(100);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para edad = 100");
    }

    // CP-TRIAJ-13: Presión arterial = 90 mmHg (límite inferior válido)
    @Test
    @DisplayName("CP-TRIAJ-13: Presión arterial = 90 mmHg (límite inferior válido)")
    void triaje_conPresionArterial90_noDebeProducirViolaciones() {
        // Arrange
        triajeDTO.setPresionSistolica(90.0f);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para presión arterial = 90 mmHg");
    }

    // CP-TRIAJ-15: Presión arterial = 200 mmHg (límite superior válido)
    @Test
    @DisplayName("CP-TRIAJ-15: Presión arterial = 200 mmHg (límite superior válido)")
    void triaje_conPresionArterial200_noDebeProducirViolaciones() {
        // Arrange
        triajeDTO.setPresionSistolica(200.0f);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber violaciones para presión arterial = 200 mmHg");
    }

    // Prueba de campo requerido faltante
    @Test
    @DisplayName("Campo requerido pacienteId faltante debe producir violación")
    void triaje_sinPacienteId_debeProducirViolacion() {
        // Arrange
        triajeDTO.setPacienteId(null);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones para pacienteId null");
        assertEquals(1, violations.size(), "Debería haber exactamente 1 violación");

        ConstraintViolation<TriajeDTO> violation = violations.iterator().next();
        assertEquals("pacienteId", violation.getPropertyPath().toString(), "La violación debería ser para pacienteId");
    }

    // Prueba de campo requerido faltante
    @Test
    @DisplayName("Campo requerido fechaTriaje faltante debe producir violación")
    void triaje_sinFechaTriaje_debeProducirViolacion() {
        // Arrange
        triajeDTO.setFechaTriaje(null);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones para fechaTriaje null");

        boolean hayViolacionFechaTriaje = violations.stream()
                .anyMatch(v -> "fechaTriaje".equals(v.getPropertyPath().toString()));

        assertTrue(hayViolacionFechaTriaje, "Debería haber una violación para fechaTriaje");
    }

    // Prueba de valor de nivel de prioridad
    @Test
    @DisplayName("Nivel de prioridad válido no debe producir violaciones")
    void triaje_conNivelPrioridadValido_noDebeProducirViolaciones() {
        // Arrange - Probar todos los valores de la enumeración
        for (NivelPrioridad nivel : NivelPrioridad.values()) {
            triajeDTO.setNivelPrioridad(nivel);

            // Act
            Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

            // Assert
            assertTrue(violations.isEmpty(), "No debería haber violaciones para nivel prioridad " + nivel);
        }
    }

    // Prueba de nivel de prioridad null
    @Test
    @DisplayName("Nivel de prioridad null debe producir violación")
    void triaje_conNivelPrioridadNull_debeProducirViolacion() {
        // Arrange
        triajeDTO.setNivelPrioridad(null);

        // Act
        Set<ConstraintViolation<TriajeDTO>> violations = validator.validate(triajeDTO);

        // Assert
        assertFalse(violations.isEmpty(), "Debería haber violaciones para nivelPrioridad null");

        boolean hayViolacionNivelPrioridad = violations.stream()
                .anyMatch(v -> "nivelPrioridad".equals(v.getPropertyPath().toString()));

        assertTrue(hayViolacionNivelPrioridad, "Debería haber una violación para nivelPrioridad");
    }
}