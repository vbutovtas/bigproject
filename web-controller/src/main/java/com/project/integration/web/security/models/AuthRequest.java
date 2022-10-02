package com.project.integration.web.security.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
  private String login;
  private String password;
}
