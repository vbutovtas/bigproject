package com.project.integration.serv.services;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.repos.OrderRepository;
import com.project.integration.serv.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserService userService;

  @Autowired
  public OrderService(OrderRepository orderRepository, UserService userService) {
    this.orderRepository = orderRepository;
    this.userService = userService;
  }

  public void create(OrderDto orderDto) {
    Order order = new Order(orderDto.getDescription());
    order.setClient(userService.autoCreate(orderDto.getClient(), order));
    orderRepository.save(order);
  }
}
