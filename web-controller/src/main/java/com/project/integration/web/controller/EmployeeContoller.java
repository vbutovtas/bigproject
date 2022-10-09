package com.project.integration.web.controller;

import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeContoller {
  private final EmployeeService employeeService;

  @Autowired
  public EmployeeContoller(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<EmployeeDto> create(@PathVariable("id") Integer id) {
    EmployeeDto employeeDto = employeeService.findById(id);
    return new ResponseEntity<>(employeeDto, HttpStatus.OK);
  }
}
