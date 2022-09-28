package com.project.integration.serv.services;

import com.project.integration.dao.entity.Role;
import com.project.integration.dao.repos.RoleRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@Service
@EnableAutoConfiguration
@EnableJpaRepositories("com.project.integration.dao")
@EntityScan("com.project.integration.dao")
@ComponentScan("com.project.integration")
public class RoleService {
  private final RoleRepository roleRepository;

  @Autowired
  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public boolean find(String name) {
    Optional<Role> role = roleRepository.findByName(name);
    if (role.isPresent()) return true;
    else return false;
  }
}
