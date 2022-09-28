package com.project.integration.dao;

import com.project.integration.dao.repos.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleRepositoryTest {

  @Autowired
  RoleRepository roleRepository;

  @Test
  void check() {
    Assertions.assertTrue(roleRepository.findByName("q").isPresent());
  }

  @SpringBootApplication
  static class TestConfiguration {
  }
}
