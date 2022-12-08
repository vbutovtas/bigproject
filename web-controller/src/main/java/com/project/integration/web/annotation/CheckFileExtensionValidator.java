package com.project.integration.web.annotation;

import com.project.integration.web.consts.Attributes;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CheckFileExtensionValidator implements ConstraintValidator<CheckFileExtension, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(file) && Attributes.MEDIA_TYPES.contains(file.getContentType());
    }
}
