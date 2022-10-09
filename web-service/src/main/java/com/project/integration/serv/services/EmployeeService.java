package com.project.integration.serv.services;

import com.project.integration.dao.entity.Employee;
import com.project.integration.dao.repos.EmployeeRepository;
import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.dto.UserDto;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration.serv")
public class EmployeeService {
  private final EmployeeRepository employeeRepository;

  @Autowired
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public EmployeeDto findById(Integer id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if(employee.isPresent()){
      UserDto userDto =
          new UserDto(
              employee.get().getUser().getLogin(),
              employee.get().getUser().getName(),
              employee.get().getUser().getSurname(),
              employee.get().getUser().getEmail(),
              employee.get().getUser().getPhone());
      String photoEncoded;
      try {
        byte[] photoAsBytes =
            employee.get().getPhoto().getBytes(1, (int) employee.get().getPhoto().length());
        photoEncoded = Base64.getEncoder().encodeToString(photoAsBytes);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      return new EmployeeDto(
          userDto,
          employee.get().getBirthDate(),
          employee.get().getPosition(),
          employee.get().getTechnologies(),
          employee.get().getExperience(),
          photoEncoded);
    }
    else throw new RuntimeException("employee not found"); //TODO
  }
}
