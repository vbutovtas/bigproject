package com.project.integration.web.security.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePswdRequest {
  private String login;
  private String currentPassword;
  private String newPassword;
}
