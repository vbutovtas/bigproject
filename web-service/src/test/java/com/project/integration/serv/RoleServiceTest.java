package com.project.integration.serv;

import com.project.integration.dao.repos.RoleRepository;
import com.project.integration.serv.services.RoleService;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleServiceTest {

//  @Autowired
//  RoleService roleService;
//
//  @Test
//  void check() {
//    Assertions.assertTrue(roleService.find("q"));
//  }

  @Autowired
  RoleRepository roleRepository;

  @Test
  void check() {
    Assertions.assertTrue(Objects.nonNull(roleRepository));
  }

  @SpringBootApplication
  static class TestConfiguration {
  }
}
