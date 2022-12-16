package com.project.integration.serv.services;

import com.project.integration.dao.entity.Employee;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.EmployeeRepository;
import com.project.integration.dao.repos.UserRepository;
import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.enums.UserStatus;
import com.project.integration.serv.exception.ServiceException;
import com.project.integration.serv.mapper.EmployeeMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class EmployeeService {
  private final EmployeeRepository employeeRepository;
  private final UserService userService;
  private final EmployeeMapper employeeMapper;
  private final PasswordEncoder passwordEncoder;
  private final MailSender mailSender;

  @Autowired
  public EmployeeService(
      EmployeeRepository employeeRepository,
      UserService userService,
      EmployeeMapper employeeMapper,
      PasswordEncoder passwordEncoder,
      MailSender mailSender) {
    this.employeeRepository = employeeRepository;
    this.userService = userService;
    this.employeeMapper = employeeMapper;
    this.passwordEncoder = passwordEncoder;
    this.mailSender = mailSender;
  }

  public List<EmployeeDto> findAll() {
    List<Employee> employees =
        employeeRepository.findByUserStatusNot(UserStatus.BLOCKED.getValue());
    return employeeMapper.convertToDto(employees);
  }

  public EmployeeDto findById(Integer id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isPresent()) {
      return employeeMapper.convertToDto(employee.get());
    } else throw new ServiceException("Employee not found");
  }

  public EmployeeDto findByUserId(Integer userId) {
    Optional<Employee> employee = employeeRepository.findByUserId(userId);
    if (employee.isPresent()) {
      return employeeMapper.convertToDto(employee.get());
    } else throw new ServiceException("Employee not found");
  }

  public void create(EmployeeDto employeeDto) {
    if (Objects.isNull(employeeDto.getUser())
        || Objects.isNull(employeeDto.getUser().getName())
        || Objects.isNull(employeeDto.getUser().getSurname())
        || Objects.isNull(employeeDto.getUser().getRole())
        || Objects.isNull(employeeDto.getPosition())
        || Objects.isNull(employeeDto.getStartDate()))
      throw new ServiceException("All fields must be filled");
    Employee employee = employeeMapper.convertToEntity(employeeDto);
    User user = userService.prepareUser(employeeDto.getUser(), employeeDto.getUser().getRole());
    String message =
        String.format(
            "Hello, %s! \n"
                + "Welcome to IT Manager Projects. Please, visit next link: http://localhost:3000/login\n"
                + "Your credentials: \n"
                + "Login: %s\n"
                + "Password: %s",
            (user.getName() + " " + user.getSurname()), user.getLogin(), user.getPassword());
    mailSender.send(user.getEmail(), "Welcome to IT Manager Projects", message);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    if (Objects.nonNull(user.getId())) {
      throw new ServiceException("Login already exists");
    }
    employee.setUser(user);
    createOrUpdate(employee);
  }

  public void update(EmployeeDto employeeDto, Integer id) {
    employeeDto.setId(id);
    Employee employee = employeeMapper.convertToEntity(employeeDto);
    prepareEmployeeForUpdate(employee);
    createOrUpdate(employee);
  }

  // TODO refactor
  private void prepareEmployeeForUpdate(Employee employee) {
    Optional<Employee> initialEmployee = employeeRepository.findById(employee.getId());
    if (initialEmployee.isEmpty()) throw new ServiceException("Employee not found");
    if (Objects.isNull(employee.getUser())) employee.setUser(initialEmployee.get().getUser());
    else userService.prepareUserForUpdate(employee.getUser(), initialEmployee.get().getUser().getId());
    if (Objects.isNull(employee.getBirthDate()))
      employee.setBirthDate(initialEmployee.get().getBirthDate());
    if (Objects.isNull(employee.getPosition()))
      employee.setPosition(initialEmployee.get().getPosition());
    if (Objects.isNull(employee.getTechnologies()))
      employee.setTechnologies(initialEmployee.get().getTechnologies());
    if (Objects.isNull(employee.getStartDate()))
      employee.setStartDate(initialEmployee.get().getStartDate());
    if (Objects.isNull(employee.getExperience()))
      employee.setExperience(initialEmployee.get().getExperience());
    if (Objects.isNull(employee.getPhoto())) employee.setPhoto(initialEmployee.get().getPhoto());
    if (employee.getAssigneeTickets().size() == 0)
      employee.setAssigneeTickets(initialEmployee.get().getAssigneeTickets());
    if (employee.getReporterTickets().size() == 0)
      employee.setReporterTickets(initialEmployee.get().getReporterTickets());
    if (employee.getComments().size() == 0)
      employee.setComments(initialEmployee.get().getComments());
    if (employee.getProjects().size() == 0)
      employee.setProjects(initialEmployee.get().getProjects());
  }

  private void createOrUpdate(Employee employee) {
    try {
      employeeRepository.save(employee);
    } catch (DataIntegrityViolationException e) {
      throw new ServiceException("Login already exists");
    }
  }
}
