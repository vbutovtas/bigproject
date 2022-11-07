package com.project.integration.serv.services;

import com.project.integration.dao.entity.Employee;
import com.project.integration.dao.entity.User;
import com.project.integration.dao.repos.EmployeeRepository;
import com.project.integration.dao.repos.RoleRepository;
import com.project.integration.dao.repos.UserRepository;
import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.mapper.EmployeeMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class EmployeeService {
  private final EmployeeRepository employeeRepository;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserService userService;
  private final EmployeeMapper employeeMapper;

  @Autowired
  public EmployeeService(
      EmployeeRepository employeeRepository,
      UserRepository userRepository,
      RoleRepository roleRepository,
      UserService userService,
      EmployeeMapper employeeMapper) {
    this.employeeRepository = employeeRepository;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userService = userService;
    this.employeeMapper = employeeMapper;
  }

  public List<EmployeeDto> findAll() {
    List<Employee> employees = employeeRepository.findAll();
    return employeeMapper.convertToDto(employees);
  }

  public EmployeeDto findById(Integer id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isPresent()) {
      return employeeMapper.convertToDto(employee.get());
    } else throw new RuntimeException("employee not found"); // TODO
  }

  public void create(EmployeeDto employeeDto) {
    if (Objects.isNull(employeeDto.getUser())
        || Objects.isNull(employeeDto.getUser().getName())
        || Objects.isNull(employeeDto.getUser().getSurname())
        || Objects.isNull(employeeDto.getUser().getRole())
        || Objects.isNull(employeeDto.getPosition())
        || Objects.isNull(employeeDto.getStartDate()))
      throw new IllegalArgumentException("All fields must be filled"); //TODO
    Employee employee = employeeMapper.convertToEntity(employeeDto);
    User user = userService.prepareUser(employeeDto.getUser(), employeeDto.getUser().getRole());
    if (Objects.nonNull(user.getId())) {
      throw new RuntimeException("Employee already exists"); // TODO
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
    if (initialEmployee.isEmpty()) throw new RuntimeException("Employee not found"); // TODO
    if (Objects.isNull(employee.getUser())) employee.setUser(initialEmployee.get().getUser());
    else prepareUserForUpdate(employee.getUser(), initialEmployee.get().getUser().getLogin());
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

  private void prepareUserForUpdate(User user, String login) {
    Optional<User> initUser = userRepository.findByLogin(login);
    if (initUser.isEmpty()) throw new RuntimeException("User not found"); // TODO
    if (Objects.isNull(user.getId())) user.setId(initUser.get().getId());
    if (Objects.isNull(user.getRole())) user.setRole(initUser.get().getRole());
    if (Objects.isNull(user.getPassword())) user.setPassword(initUser.get().getPassword());
    if (Objects.isNull(user.getStatus())) user.setStatus(initUser.get().getStatus());
    if (Objects.isNull(user.getOrders())) user.setOrders(initUser.get().getOrders());
  }

  private void createOrUpdate(Employee employee) {
    try {
      employeeRepository.save(employee);
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("Login already exists"); // TODO
    }
  }
}
