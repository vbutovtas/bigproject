package com.project.integration.web.convertor;

import com.project.integration.serv.dto.OrderDto;
import com.project.integration.serv.dto.UserDto;
import com.project.integration.web.formmodel.OrderForm;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PojoConverter {
  @SneakyThrows
  public OrderDto convertOrderPojoToDto(OrderForm orderForm) {
    return new OrderDto(
        new UserDto(
            orderForm.getFirstName(),
            orderForm.getLastName(),
            orderForm.getEmail(),
            orderForm.getPhone()),
        orderForm.getDocument().getBytes());
  }
}
