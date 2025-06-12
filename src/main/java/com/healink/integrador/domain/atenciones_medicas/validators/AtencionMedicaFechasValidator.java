package com.healink.integrador.domain.atenciones_medicas.validators;

import java.sql.Timestamp;

import com.healink.integrador.domain.atenciones_medicas.AtencionMedicaDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtencionMedicaFechasValidator implements ConstraintValidator<ValidAtencionMedicaFechas, AtencionMedicaDTO> {

    @Override
    public void initialize(ValidAtencionMedicaFechas constraintAnnotation) {
        // No necesita inicialización especial
    }

    @Override
    public boolean isValid(AtencionMedicaDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null es válido, @NotNull debería manejar esto si es necesario
        }

        Timestamp fechaInicio = value.getFechaHoraInicio();
        Timestamp fechaFin = value.getFechaHoraFin();

        // Si cualquiera de las fechas es null, no validamos aquí (dejar que @NotNull lo maneje)
        if (fechaInicio == null || fechaFin == null) {
            return true;
        }

        boolean valid = true;

        // Deshabilitar la violación por defecto para crear mensajes personalizados
        context.disableDefaultConstraintViolation();

        // Validar que fecha de inicio sea anterior a fecha de fin
        if (!fechaInicio.before(fechaFin)) {
            context.buildConstraintViolationWithTemplate(
                "La fecha de inicio debe ser anterior a la fecha de finalización de la atención médica.")
                .addPropertyNode("fechaHoraInicio")
                .addConstraintViolation();
            valid = false;
        }

        // Validar que fecha de fin sea posterior a fecha de inicio
        if (!fechaFin.after(fechaInicio)) {
            context.buildConstraintViolationWithTemplate(
                "La fecha de fin debe ser posterior a la fecha de inicio de la atención médica.")
                .addPropertyNode("fechaHoraFin")
                .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
} 