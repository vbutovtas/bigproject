package com.project.integration.web.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckFileExtensionValidator.class)
public @interface CheckFileExtension {
    String message() default "Неверное расширение файла.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
