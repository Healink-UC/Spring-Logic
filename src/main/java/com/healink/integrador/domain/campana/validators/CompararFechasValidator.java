package com.healink.integrador.domain.campana.validators;

import java.lang.reflect.Field;
import java.time.LocalDate;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import com.healink.integrador.core.validator.FechasValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompararFechasValidator implements ConstraintValidator<FechasValidator, LocalDate> {

    private String compareWith;

    @Override
    public void initialize(FechasValidator rangoFecha) {
        this.compareWith = rangoFecha.compareWith();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // Considerar nulo como inválido
        }

        try {
            Object object = context.unwrap(HibernateConstraintValidatorContext.class);
            Field compareField = object.getClass().getDeclaredField(compareWith);
            compareField.setAccessible(true);
            Object compareValue = compareField.get(object);

            if (compareValue instanceof LocalDate) {
                return value.isBefore((LocalDate) compareValue);
            }
        } catch (Exception e) {
            return false; // Si hay error, la validación falla
        }

        return true;
    }

}
