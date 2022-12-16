package com.project.integration.web.exception;

import static com.project.integration.web.consts.Attributes.MAX_FILE_SIZE_ERROR_MSG;

import com.project.integration.serv.exception.ServiceException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler({ServiceException.class, IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleServiceException(Exception ex) {
    return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
  }

  @ExceptionHandler({JwtException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ExceptionResponse handleAuthorizationException(Exception ex) {
    return new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public String handleFileSizeLimitExceeded(MaxUploadSizeExceededException exc) {
    return MAX_FILE_SIZE_ERROR_MSG;
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    return processFieldErrors(fieldErrors);
  }

  private ExceptionResponse processFieldErrors(
      List<org.springframework.validation.FieldError> fieldErrors) {
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "validation error");
    for (FieldError fieldError : fieldErrors) {
      exceptionResponse.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
    }
    return exceptionResponse;
  }
}
