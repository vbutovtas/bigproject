package com.project.integration.serv.services;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.Role;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.RoleRepository;
import com.project.integration.dao.repos.UserRepository;
import com.project.integration.serv.convertor.DtoConvertor;
import com.project.integration.serv.dto.UserDto;
import com.project.integration.serv.enums.UserRoles;
import com.project.integration.serv.enums.UserStatus;
import com.project.integration.serv.security.UserDetailsImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
  public User autoCreate(UserDto userDto, Order order){
    if(Objects.isNull(userDto))
      throw new NullPointerException("User value is empty: " + userDto);
    userDto.setLogin(createLogin(userDto.getName(), userDto.getSurname()));
    if(Objects.nonNull(isExist(userDto))) {
      // TODO: log.info("User with login {} is exist", userDto.getLogin());
      return isExist(userDto);
    }
    User user = DtoConvertor.convertToUser(userDto);
    user.setStatus(UserStatus.DEACTIVATED.getValue());
    user.setRole(new Role(UserRoles.CUSTOMER.getValue()));
    user.setPassword(passwordEncoder.encode(generateCommonLangPassword()));
    user.setOrders(Collections.singleton(order));
    try {
      userRepository.save(user);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create user: " + user, e);
    }
    return user;
  }

  private User isExist(UserDto userDto){
    return userRepository.findByLogin(createLogin(userDto.getName(), userDto.getSurname())).orElse(null);
  }
  public String generateCommonLangPassword() {
    String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
    String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
    String numbers = RandomStringUtils.randomNumeric(2);
    String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
    String totalChars = RandomStringUtils.randomAlphanumeric(2);
    String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
            .concat(numbers)
            .concat(specialChar)
            .concat(totalChars);
    List<Character> pwdChars = combinedChars.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toList());
    Collections.shuffle(pwdChars);
    return pwdChars.stream()
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
  }

  private String createLogin(String firstName, String lastName){
    return firstName.substring(0, 2).toLowerCase() + lastName.substring(0, 2).toLowerCase();
  }
}
