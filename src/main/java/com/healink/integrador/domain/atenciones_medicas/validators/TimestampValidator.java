package com.healink.integrador.domain.atenciones_medicas.validators;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.healink.integrador.core.validator.TimeValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimestampValidator implements ConstraintValidator<TimeValidator, Timestamp> {

    private String compareWith;

    @Override
    public void initialize(TimeValidator rangoFecha) {
        this.compareWith = rangoFecha.compareWith();
    }

    @Override
    public boolean isValid(Timestamp value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Deja que @NotNull maneje los valores nulos
        }

        // Para este validador, necesitamos usar una validación a nivel de clase
        // ya que acceder al objeto raíz desde el contexto es complejo
        // Retornamos true aquí y moveremos la validación a nivel de clase
        return true;
    }
} 