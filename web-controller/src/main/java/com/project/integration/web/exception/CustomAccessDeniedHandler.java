package com.project.integration.web.exception;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc) {
    String message = exc.getMessage();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    if (Objects.nonNull(auth)) {
//      logger.warn(
//          "User: "
//              + auth.getName()
//              + " attempted to access the protected URL: "
//              + request.getRequestURI());
//    }
    //SecurityExceptionHandler.handle(message, response);
    throw new RuntimeException(message);
  }
}

