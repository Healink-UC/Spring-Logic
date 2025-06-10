package com.healink.integrador.core.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import com.healink.integrador.domain.campana.validators.CompararFechasValidator;

@Documented
@Constraint(validatedBy = CompararFechasValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FechasValidator {

    String compareWith(); // Nombre del otro campo con el que se comparará

    String message() default "La fecha debe ser anterior a la otra fecha";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
