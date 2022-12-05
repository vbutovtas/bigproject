package com.project.integration.serv.services;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.OrderRepository;
import com.project.integration.serv.dto.OrderDto;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    private final MailSender mailSender;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, MailSender mailSender) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.mailSender = mailSender;
    }

    public void create(OrderDto orderDto) {
        Order order = new Order(orderDto.getDescription());
        order.setStartDate(LocalDate.now());
        User user = userService.autoCreate(orderDto.getClient(), order);
        order.setClient(user);
        orderRepository.save(order);
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to IT Manager Projects. Please, visit next link: http://localhost:3000/login\n" +
                        "Your credentials: \n" +
                        "Login: %s\n" +
                        "Password: %s",
                (user.getName() + " " + user.getSurname()),
                user.getLogin(),
                user.getPassword()
        );
        mailSender.send(user.getEmail(), "Welcome to IT Manager Projects", message);
    }
}
