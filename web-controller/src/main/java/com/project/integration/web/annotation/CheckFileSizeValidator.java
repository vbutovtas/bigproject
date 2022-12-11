package com.project.integration.web.annotation;

import static com.project.integration.web.annotation.CheckFileSize.MAX_FILE_SIZE;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class CheckFileSizeValidator
    implements ConstraintValidator<CheckFileSize, MultipartFile> {

  @Override
  public boolean isValid(
      MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
    return Objects.nonNull(file) && file.getSize() < MAX_FILE_SIZE;
  }
}
