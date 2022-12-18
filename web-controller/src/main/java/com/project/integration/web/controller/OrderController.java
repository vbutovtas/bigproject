package com.project.integration.web.controller;

import com.project.integration.serv.dto.OrderDto;
import com.project.integration.serv.services.OrderService;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public ResponseEntity<List<OrderDto>> findAll(
      @RequestParam(name = "showBlocked", required = false) boolean showBlocked) {
    List<OrderDto> orders = orderService.findAll(showBlocked);
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  @PutMapping(value = "/{id}/block")
  public ResponseEntity<Void> blockOrder(@PathVariable("id") Integer id) {
    orderService.blockOrder(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/{id}/project")
  public ResponseEntity<OrderDto> getClientCurrentProjectId(@PathVariable("id") Integer clientId) {
    OrderDto clientProject = orderService.getClientCurrentProjectId(clientId);
    return new ResponseEntity<>(clientProject, HttpStatus.OK);
  }

  @GetMapping(
      value = "/{id}/description",
      produces = {"application/octet-stream"})
  public ResponseEntity<byte[]> getOrderDescription(@PathVariable("id") Integer id) {
    OrderDto orderDto = orderService.findById(id);
    String fileName = "order_" + orderDto.getId() + "_description.doc";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
    return new ResponseEntity<>(
        ArrayUtils.toPrimitive(orderDto.getDescription()), headers, HttpStatus.OK);
  }
}
