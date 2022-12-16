package com.project.integration.web.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    if (authException instanceof BadCredentialsException
        || authException instanceof InternalAuthenticationServiceException)
      response.sendError(
          HttpStatus.UNAUTHORIZED.value(), "User account has expired"); // http-status is 401
    else if (authException instanceof LockedException) // http-status is 423
      response.sendError(HttpStatus.LOCKED.value(), "User account is locked");
    else if (authException instanceof InsufficientAuthenticationException)
      response.sendError(HttpStatus.FORBIDDEN.value(), "User is not authorized");
    else throw authException;
  }
}
