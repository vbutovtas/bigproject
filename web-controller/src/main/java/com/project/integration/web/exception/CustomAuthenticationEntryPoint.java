package com.project.integration.web.exception;

import com.project.integration.web.consts.Attributes;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {
    Exception exception = (Exception) request.getAttribute(Attributes.EXCEPTION);

    if (Objects.nonNull(exception))
      //SecurityExceptionHandler.handle(exception.getMessage(), response); //TODO
      throw new RuntimeException(exception);
    else
      //SecurityExceptionHandler.handle(authException.getMessage(), response); //TODO
      throw new RuntimeException(authException.getMessage());
  }
}
