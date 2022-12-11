package com.project.integration.serv.mapper;

import com.project.integration.dao.entity.User;
import com.project.integration.serv.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("com.project.integration.serv")
public class UserMapper {
  private final ModelMapper modelMapper;

  @Autowired
  public UserMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public User convertToEntity(UserDto userDto) {
    return modelMapper.map(userDto, User.class);
  }

  public UserDto convertToDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }
}
