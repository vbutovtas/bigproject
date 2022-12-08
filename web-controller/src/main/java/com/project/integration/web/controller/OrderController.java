package com.project.integration.web.controller;

import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.dto.OrderDto;
import com.project.integration.serv.services.EmployeeService;
import com.project.integration.serv.services.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<OrderDto>> findAll(){
    List<OrderDto> orders = orderService.findAll();
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }
}
