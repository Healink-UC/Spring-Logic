package com.healink.integrador.domain.atenciones_medicas.validators;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

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
            return false;
        }

        try {
            Object object = context.unwrap(HibernateConstraintValidatorContext.class);
            Field compareField = object.getClass().getDeclaredField(compareWith);
            compareField.setAccessible(true);
            Object compareValue = compareField.get(object);

            if (compareValue instanceof Timestamp) {
                return value.before((Timestamp) compareValue);
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
} 