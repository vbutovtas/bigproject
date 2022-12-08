package com.project.integration.serv.services;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.OrderRepository;
import com.project.integration.serv.dto.OrderDto;
import com.project.integration.serv.enums.OrderStatus;
import com.project.integration.serv.enums.UserStatus;
import com.project.integration.serv.mapper.OrderMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserService userService;
  private final OrderMapper orderMapper;
  private final MailSender mailSender;

  @Autowired
  public OrderService(
      OrderRepository orderRepository,
      UserService userService,
      OrderMapper orderMapper,
      MailSender mailSender) {
    this.orderRepository = orderRepository;
    this.userService = userService;
    this.orderMapper = orderMapper;
    this.mailSender = mailSender;
  }

  public void create(OrderDto orderDto) {
    orderDto.setStartDate(LocalDate.now());
    orderDto.setStatus(OrderStatus.OPEN);
    Order order = orderMapper.convertToEntity(orderDto);
    User user = userService.autoCreate(orderDto.getClient(), order);
    order.setClient(user);
    orderRepository.save(order);
    if (user.getStatus().equals(UserStatus.DEACTIVATED.getValue())) {
      String message =
          String.format(
              "Hello, %s! \n"
                  + "Welcome to IT Manager Projects. Please, visit next link: http://localhost:3000/login\n"
                  + "Your credentials: \n"
                  + "Login: %s\n"
                  + "Password: %s",
              (user.getName() + " " + user.getSurname()), user.getLogin(), user.getPassword());
      mailSender.send(user.getEmail(), "Welcome to IT Manager Projects", message);
    }
  }

  public List<OrderDto> findAll() {
    List<Order> orders = orderRepository.findAll();
    return orderMapper.convertToDto(orders);
  }

  public List<OrderDto> findByClientId(Integer id) {
    User client = new User();
    client.setId(id);
    List<Order> orders = orderRepository.findByClient(client);
    return orderMapper.convertToDto(orders);
  }

  public void setProjectToOrder(Integer projectId, Integer orderId){
    orderRepository.setProject(projectId, orderId);
  }
}
