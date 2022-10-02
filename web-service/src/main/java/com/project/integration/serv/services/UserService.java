package com.project.integration.serv.services;

import com.project.integration.dao.entity.Role;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.RoleRepository;
import com.project.integration.dao.repos.UserRepository;
import com.project.integration.serv.dto.UserDto;
import com.project.integration.serv.security.UserDetailsImpl;
import java.util.Optional;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String login) {
    Optional<User> user = userRepository.findByLogin(login);
    if (user.isPresent()) return UserDetailsImpl.fromUserEntityToUserDetails(user.get());
    else throw new RuntimeException("user not found"); // TODO
  }

  public void create(UserDto userDto) {
    Optional<Role> role = roleRepository.findByName(userDto.getRole().getValue());
    if (role.isEmpty()) throw new RuntimeException("role does not exist"); // TODO
    // TODO user status - enum as request param how
    User user =
        new User(
            role.get(),
            userDto.getLogin(),
            passwordEncoder.encode(userDto.getPassword()),
            userDto.getName(),
            userDto.getSurname(),
            userDto.getEmail(),
            userDto.getPhone(),
            userDto.getStatus().getValue());
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("user exists"); // TODO
    }
  }
}
