package com.healink.integrador.domain.atenciones_medicas.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { AtencionMedicaFechasValidator.class })
public @interface ValidAtencionMedicaFechas {
    String message() default "Las fechas de la atención médica no son válidas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
} 