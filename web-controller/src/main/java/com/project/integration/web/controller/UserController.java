package com.project.integration.web.controller;

import com.project.integration.serv.dto.UserDto;
import com.project.integration.serv.services.UserService;
import com.project.integration.web.security.models.ChangePswdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public UserController(UserService userService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody UserDto userDto) {
    userService.create(userDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(value = "/{id}/block")
  public ResponseEntity<Void> blockUser(@PathVariable("id") Integer id) {
    userService.blockUser(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/password")
  public ResponseEntity<Void> blockUser(@RequestBody ChangePswdRequest changePswdRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            changePswdRequest.getLogin(), changePswdRequest.getCurrentPassword()));
    userService.changePassword(changePswdRequest.getLogin(), changePswdRequest.getNewPassword());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
