package com.healink.integrador.core.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.healink.integrador.domain.atenciones_medicas.validators.TimestampValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { TimestampValidator.class })
public @interface TimeValidator {
    String message() default "La fecha no es válida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String compareWith();
}
