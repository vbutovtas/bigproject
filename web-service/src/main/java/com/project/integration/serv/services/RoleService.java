package com.project.integration.serv.services;

import com.project.integration.dao.entity.Role;
import com.project.integration.dao.repos.RoleRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class RoleService {
  private final RoleRepository roleRepository;

  @Autowired
  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public boolean findByName(String name) { //TODO redo
    Optional<Role> role = roleRepository.findByName(name);
    if (role.isPresent()) return true;
    else return false;
  }
}
