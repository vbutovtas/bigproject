package com.project.integration.web.controller;

import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.services.EmployeeService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
  private final EmployeeService employeeService;

  @Autowired
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<EmployeeDto> findById(@PathVariable("id") Integer id) {
    EmployeeDto employeeDto = employeeService.findById(id);
    return new ResponseEntity<>(employeeDto, HttpStatus.OK);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Void> update(@RequestBody EmployeeDto employeeDto, @PathVariable("id") Integer id){
    employeeService.update(employeeDto, id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<EmployeeDto>> findAll(){
    List<EmployeeDto> employeeDtos = employeeService.findAll();
    return new ResponseEntity<>(employeeDtos, HttpStatus.OK);
  }

  @PostMapping(value = "/new")
  public ResponseEntity<Void> create(@RequestBody @Valid EmployeeDto employeeDto){
    employeeService.create(employeeDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
