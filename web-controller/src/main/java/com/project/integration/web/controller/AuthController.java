package com.project.integration.web.controller;

import com.project.integration.serv.services.UserService;
import com.project.integration.web.security.jwt.JwtProvider;
import com.project.integration.web.security.models.AuthRequest;
import com.project.integration.web.security.models.AuthResponse;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtProvider jwtProvider;

  @Autowired
  public AuthController(
      AuthenticationManager authenticationManager,
      UserService userService,
      JwtProvider jwtProvider) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtProvider = jwtProvider;
  }

  @PostMapping("/auth")
  public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
    String token =
        jwtProvider.generateToken(userService.loadUserByUsername(authRequest.getLogin()));
    return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
    DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
    String token = null;
    if (Objects.nonNull(claims)) {
      Map<String, Object> expectedMap = new HashMap<>(claims);
      token = jwtProvider.generateRefreshedToken(expectedMap, expectedMap.get("sub").toString());
    }
    return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
  }
}