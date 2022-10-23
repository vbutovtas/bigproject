//package com.project.integration.serv;
//
//import com.project.integration.dao.entity.Ticket;
//import com.project.integration.dao.repos.RoleRepository;
//import com.project.integration.serv.dto.TicketDto;
//import java.util.List;
//import java.util.Objects;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeMap;
//import org.modelmapper.spi.Mapping;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@SpringBootTest
//public class RoleServiceTest {
//  @Autowired RoleRepository roleRepository;
//  @Autowired
//  ModelMapper modelMapper;
//
//  @Autowired
//  PasswordEncoder passwordEncoder;
//
//  @Test
//  void check() {
//    TypeMap<Ticket, TicketDto> propertyMapper = this.modelMapper.createTypeMap(Ticket.class, TicketDto.class);
//    List<Mapping> list = propertyMapper.getMappings();
//    for (Mapping m : list)
//    {
//      System.out.println(m);
//    }
//    Assertions.assertTrue(Objects.nonNull(roleRepository));
//  }
//
//  @SpringBootApplication
//  static class TestConfiguration {
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//      return new BCryptPasswordEncoder();
//    }
//

//  }
//}
