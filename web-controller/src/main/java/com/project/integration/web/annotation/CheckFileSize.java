package com.project.integration.web.annotation;

import static com.project.integration.web.consts.Attributes.MAX_FILE_SIZE_ERROR_MSG;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckFileSizeValidator.class)
public @interface CheckFileSize {
  long MAX_FILE_SIZE = 64000;

  String message() default MAX_FILE_SIZE_ERROR_MSG;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
