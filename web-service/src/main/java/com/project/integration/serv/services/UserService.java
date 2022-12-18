package com.project.integration.serv.services;

import com.project.integration.dao.entity.Order;
import com.project.integration.dao.entity.Role;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.UserRepository;
import com.project.integration.serv.dto.UserDto;
import com.project.integration.serv.enums.OrderStatus;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.enums.UserRoles;
import com.project.integration.serv.enums.UserStatus;
import com.project.integration.serv.exception.ServiceException;
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
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final MailSender mailSender;

  public UserService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      UserMapper userMapper,
      MailSender mailSender) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
    this.mailSender = mailSender;
  }

  @Override
  public UserDetails loadUserByUsername(String login) {
    Optional<User> user = userRepository.findByLogin(login);
    if (user.isPresent()) return UserDetailsImpl.fromUserEntityToUserDetails(user.get());
    else throw new ServiceException("User not found");
  }

  public UserDto findUserById(Integer id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) return userMapper.convertToDto(user.get());
    else throw new ServiceException("User not found");
  }

  public void update(UserDto userDto, Integer id) {
    userDto.setId(id);
    User user = userMapper.convertToEntity(userDto);
    prepareUserForUpdate(user, id);
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new ServiceException("Login already exists");
    }
  }

  public void prepareUserForUpdate(User user, Integer id) {
    Optional<User> initUser = userRepository.findById(id);
    if (initUser.isEmpty()) throw new ServiceException("User not found");
    if (Objects.isNull(user.getId())) user.setId(initUser.get().getId());
    if (Objects.isNull(user.getRole())) user.setRole(initUser.get().getRole());
    if (Objects.isNull(user.getLogin())) user.setLogin(initUser.get().getLogin());
    if (Objects.isNull(user.getPassword())) user.setPassword(initUser.get().getPassword());
    if (Objects.isNull(user.getStatus())) user.setStatus(initUser.get().getStatus());
    if (Objects.isNull(user.getOrders())) user.setOrders(initUser.get().getOrders());
  }

  public User autoCreate(UserDto userDto, Order order) {
    User user = prepareUser(userDto, UserRoles.ROLE_CUSTOMER), userWithOpenedPSWRD;
    String password = user.getPassword();
    if (Objects.isNull(user.getId())) {
      try {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        user.setOrders(Collections.singleton(order));
      } catch (Exception e) {
        throw new ServiceException("Failed to create user: " + user, e);
      }
    }
    userWithOpenedPSWRD = new User(user);
    userWithOpenedPSWRD.setPassword(password);
    return userWithOpenedPSWRD;
  }

  public User prepareUser(UserDto userDto, UserRoles role) {
    if (Objects.isNull(userDto)) throw new NullPointerException("User value is empty: " + userDto);
    if (Objects.isNull(role)) throw new NullPointerException("Role value is empty: " + role);
    userDto.setLogin(createLogin(userDto.getName(), userDto.getSurname()));
    User existUser = isExist(userDto);
    if (Objects.nonNull(existUser)) {
      return existUser;
    }
    userDto.setStatus(UserStatus.DEACTIVATED);
    User user = userMapper.convertToEntity(userDto);
    user.setRole(new Role(role.getValue()));
    user.setPassword((generateCommonLangPassword()));
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
    if (firstName.length() > 10) firstName = firstName.substring(0, 10);
    if (lastName.length() > 10) lastName = lastName.substring(0, 10);
    return firstName.toLowerCase() + "_" + lastName.toLowerCase();
  }

  public void blockUser(Integer id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      if (user.get().getRole().getId().equals(UserRoles.ROLE_CUSTOMER.getValue())) {
        for (Order order : user.get().getOrders()) order.setStatus(OrderStatus.BLOCKED.getValue());
      }
      user.get().setStatus(UserStatus.BLOCKED.getValue());
      userRepository.save(user.get());
    } else throw new ServiceException("User not found");
  }

  public void activateUser(Integer id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      if (user.get().getRole().getId().equals(UserRoles.ROLE_CUSTOMER.getValue())) {
        for (Order order : user.get().getOrders()) {
          if (Objects.nonNull(order.getProject())) {
            if (order.getProject().getStatus().equals(TicketStatus.CLOSE.getValue()))
              order.setStatus(OrderStatus.DONE.getValue());
            else order.setStatus(OrderStatus.IS_PROCESSED.getValue());
          } else order.setStatus(OrderStatus.OPEN.getValue());
        }
      }
      user.get().setStatus(UserStatus.ACTIVE.getValue());
      userRepository.save(user.get());
    } else throw new ServiceException("User not found");
  }

  public void deactivateUser(Integer id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      String newPassword = generateCommonLangPassword();
      String message =
          String.format(
              "Hello, %s! \n"
                  + "Your account was deactivated. To activate it back, log in and change password."
                  + "Your new temporary Password: %s",
              (user.get().getName() + " " + user.get().getSurname()), newPassword);
      user.get().setPassword(passwordEncoder.encode(newPassword));
      user.get().setStatus(UserStatus.DEACTIVATED.getValue());
      if (user.get().getRole().getId().equals(UserRoles.ROLE_CUSTOMER.getValue())) {
        for (Order order : user.get().getOrders()) {
          if (Objects.nonNull(order.getProject())) {
            if (order.getProject().getStatus().equals(TicketStatus.CLOSE.getValue()))
              order.setStatus(OrderStatus.DONE.getValue());
            else order.setStatus(OrderStatus.IS_PROCESSED.getValue());
          } else order.setStatus(OrderStatus.OPEN.getValue());
        }
      }
      userRepository.save(user.get());
      mailSender.send(user.get().getEmail(), "IT Manager Projects", message);
    }
  }

  public void changePassword(String login, String newPassword) {
    userRepository.changePassword(passwordEncoder.encode(newPassword), login);
  }
}
