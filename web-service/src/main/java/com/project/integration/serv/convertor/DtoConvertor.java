package com.project.integration.serv.convertor;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.User;
import com.project.integration.serv.dto.OrderDto;
import com.project.integration.serv.dto.UserDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoConvertor {
    public Order convertOrder(OrderDto orderDto) {
        return new Order(orderDto.getDescription());
    }

    public User convertToUser(UserDto userDto) {
        return new User(
                userDto.getLogin(),
                userDto.getPassword(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getEmail(),
                userDto.getPhone()
        );
    }
}
