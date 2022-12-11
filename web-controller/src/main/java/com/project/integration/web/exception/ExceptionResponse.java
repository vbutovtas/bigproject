package com.project.integration.web.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Getter
@Setter
public class ExceptionResponse {
  private String message;
  private int status;

  public ExceptionResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  private List<FieldError> fieldErrors = new ArrayList<>();

  public void addFieldError(String object, String field, String message) {
    FieldError error = new FieldError(object, field, message);
    fieldErrors.add(error);
  }

  public List<FieldError> getFieldErrors() {
    return fieldErrors;
  }
}
