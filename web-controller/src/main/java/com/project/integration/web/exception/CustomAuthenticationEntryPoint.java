package com.project.integration.web.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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
        || authException instanceof InternalAuthenticationServiceException) // 401
    response.sendError(HttpStatus.UNAUTHORIZED.value(), "User account has expired");
    else if (authException instanceof LockedException) // 423
    response.sendError(HttpStatus.LOCKED.value(), "User account is locked");
    else throw authException;
  }
}
