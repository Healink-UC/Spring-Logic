package com.healink.integrador.domain.campana.validators;

import java.lang.reflect.Field;
import java.time.LocalDate;

import com.healink.integrador.core.validator.FechasValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompararFechasValidator implements ConstraintValidator<FechasValidator, Object> {

    @Override
    public void initialize(FechasValidator rangoFecha) {
        // No necesitamos inicializacion especifica para validacion a nivel de clase
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true; // Dejar que @NotNull maneje los valores nulos
        }

        try {
            // Obtener todas las fechas del objeto
            LocalDate fechaLimiteInscripcion = getFieldValue(object, "fechaLimiteInscripcion");
            LocalDate fechaInicio = getFieldValue(object, "fechaInicio");
            LocalDate fechaLimite = getFieldValue(object, "fechaLimite");

            // Si alguna fecha es null, no validamos (dejar que @NotNull lo maneje)
            if (fechaLimiteInscripcion == null || fechaInicio == null || fechaLimite == null) {
                return true;
            }

            boolean allValid = true;
            context.disableDefaultConstraintViolation();

            // Validar fechaLimiteInscripcion > fechaInicio
            if (!fechaLimiteInscripcion.isAfter(fechaInicio)) {
                context.buildConstraintViolationWithTemplate(
                        "La fecha limite de inscripcion debe ser anterior a la fecha de inicio de la campana.")
                        .addPropertyNode("fechaLimiteInscripcion")
                        .addConstraintViolation();
                allValid = false;
            }

            // Validar fechaInicio < fechaLimite
            if (!fechaInicio.isBefore(fechaLimite)) {
                context.buildConstraintViolationWithTemplate(
                        "La fecha de inicio debe ser anterior a la fecha de finalizacion de la campana.")
                        .addPropertyNode("fechaInicio")
                        .addConstraintViolation();
                allValid = false;
            }

            return allValid;

        } catch (Exception e) {
            return true; // En caso de error, no fallar la validacion
        }
    }

    private LocalDate getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (LocalDate) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }
}
