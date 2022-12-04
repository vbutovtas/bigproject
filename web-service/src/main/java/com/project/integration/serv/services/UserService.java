package com.project.integration.serv.services;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.Role;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.RoleRepository;
import com.project.integration.dao.repos.UserRepository;
import com.project.integration.serv.dto.UserDto;
import com.project.integration.serv.enums.UserRoles;
import com.project.integration.serv.enums.UserStatus;
import com.project.integration.serv.mapper.UserMapper;
import com.project.integration.serv.security.UserDetailsImpl;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
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
  private final UserMapper userMapper;

  public UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String login) {
    Optional<User> user = userRepository.findByLogin(login);
    if (user.isPresent()) return UserDetailsImpl.fromUserEntityToUserDetails(user.get());
    else throw new RuntimeException("user not found"); // TODO
  }

  public void create(UserDto userDto) {
    Optional<Role> role = roleRepository.findByName(userDto.getRole().name());
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

  public User autoCreate(UserDto userDto, Order order) {
    User user = prepareUser(userDto, UserRoles.CUSTOMER);
    try {
      userRepository.save(user);
      user.setOrders(Collections.singleton(order));
    } catch (Exception e) {
      throw new RuntimeException("Failed to create user: " + user, e);
    }
    return user;
  }

  public User prepareUser(UserDto userDto, UserRoles role) {
    if (Objects.isNull(userDto)) throw new NullPointerException("User value is empty: " + userDto);
    if (Objects.isNull(role)) throw new NullPointerException("Role value is empty: " + role);
    userDto.setLogin(createLogin(userDto.getName(), userDto.getSurname()));
    if (Objects.nonNull(isExist(userDto))) {
      // TODO: log.info("User with login {} exists", userDto.getLogin());
      return isExist(userDto);
    }
    userDto.setStatus(UserStatus.DEACTIVATED);
    User user = userMapper.convertToEntity(userDto);
    user.setRole(new Role(role.getValue()));
    user.setPassword(passwordEncoder.encode(generateCommonLangPassword()));
    return user;
  }

  private User isExist(UserDto userDto) {
    return userRepository
        .findByLogin(createLogin(userDto.getName(), userDto.getSurname()))
        .orElse(null);
  }

  public String generateCommonLangPassword() {
    String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
    String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
    String numbers = RandomStringUtils.randomNumeric(2);
    String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
    String totalChars = RandomStringUtils.randomAlphanumeric(2);
    String combinedChars =
        upperCaseLetters
            .concat(lowerCaseLetters)
            .concat(numbers)
            .concat(specialChar)
            .concat(totalChars);
    List<Character> pwdChars =
        combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    Collections.shuffle(pwdChars);
    return pwdChars.stream()
        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
        .toString();
  }

  public String createLogin(String firstName, String lastName) {
    if (firstName.length() < 4 || lastName.length() < 4)
      throw new IllegalArgumentException("Length of firstName/lastName is less than 4");
    return firstName.substring(0, 3).toLowerCase() + lastName.substring(0, 3).toLowerCase();
  }

  public void blockUser(Integer id) {
    userRepository.blockUser(id);
  }

  public void changePassword(String login, String newPassword){
    userRepository.changePassword(passwordEncoder.encode(newPassword), login);
  }
}
