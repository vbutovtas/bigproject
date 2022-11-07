package com.project.integration.serv.mapper;

import static com.project.integration.serv.mapper.Utils.convertList;

import com.project.integration.dao.entity.Employee;
import com.project.integration.dao.entity.Ticket;
import com.project.integration.dao.entity.User;
import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.enums.TicketType;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.sql.rowset.serial.SerialBlob;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan("com.project.integration.serv")
public class EmployeeMapper {
  private ModelMapper modelMapper;

  @Autowired
  public EmployeeMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;

    TypeMap<Employee, EmployeeDto> employeeMapperToDto =
        this.modelMapper.createTypeMap(Employee.class, EmployeeDto.class);

    TypeMap<EmployeeDto, Employee> employeeMapperToEntity =
        this.modelMapper.createTypeMap(EmployeeDto.class, Employee.class);

    Converter<Blob, String> photoConverterToString =
        ctx -> {
          try {
            if (Objects.nonNull(ctx.getSource())) {
              byte[] photoAsBytes = ctx.getSource().getBytes(1, (int) ctx.getSource().length());
              return Base64.getEncoder().encodeToString(photoAsBytes);
            } else return "";
          } catch (Exception e) {
            throw new RuntimeException(e); // TODO
          }
        };

    Converter<String, Blob> photoConverterToBlob =
        ctx -> {
          Blob photoBlob = null;
          if (Objects.nonNull(ctx.getSource())) {
            byte[] decodedBytes = Base64.getDecoder().decode(ctx.getSource());
            try {
              photoBlob = new SerialBlob(decodedBytes);
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          }
          return photoBlob;
        };

    Converter<Set<Ticket>, Integer> projectsCountConverter = ctx -> ctx.getSource().size();

    employeeMapperToDto.addMappings(
        mapper ->
            mapper.using(photoConverterToString).map(Employee::getPhoto, EmployeeDto::setPhoto));

    employeeMapperToEntity.addMappings(
        mapper ->
            mapper.using(photoConverterToBlob).map(EmployeeDto::getPhoto, Employee::setPhoto));

    employeeMapperToDto.addMappings(
        mapper ->
            mapper
                .using(projectsCountConverter)
                .map(Employee::getProjects, EmployeeDto::setProjectsCount));
  }

  public EmployeeDto convertToDto(Employee employee) {
    Optional<Ticket> currentProject =
        employee.getProjects().stream()
            .filter(
                ticket ->
                    ticket.getType().equals(TicketType.PROJECT.getValue())
                        && !ticket.getStatus().equals(TicketStatus.CLOSE.getValue()))
            .findFirst();
    EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
    if (currentProject.isPresent()) {
      TicketDto currentProjectDto = modelMapper.map(currentProject.get(), TicketDto.class);
      employeeDto.setCurrentProject(currentProjectDto);
    }
    if(Objects.nonNull(employee.getExperience())){
      long monthsBetween =
          ChronoUnit.MONTHS.between(YearMonth.from(employee.getStartDate()), YearMonth.from(LocalDate.now()));
      long years = employee.getExperience().intValue() + monthsBetween / 12;
      long months = (int) ((employee.getExperience() - employee.getExperience().intValue()) * 12) + monthsBetween % 12;
      years += months / 12;
      months %= 12;
      employeeDto.setExperience(String.format("%1$d year, %2$d months", years, months));
    }
    return employeeDto;
  }

  public Employee convertToEntity(EmployeeDto employeeDto) {
    Employee employee = modelMapper.map(employeeDto, Employee.class);
    User user = new User();
    if (Objects.nonNull(employeeDto.getUser())) {
      user =
          new User(
              employeeDto.getUser().getLogin(),
              employeeDto.getUser().getName(),
              employeeDto.getUser().getSurname(),
              employeeDto.getUser().getEmail(),
              employeeDto.getUser().getPhone());
    }
    employee.setUser(user);
    return employee;
  }

  public List<EmployeeDto> convertToDto(List<Employee> employeeList) {
    return convertList(employeeList, this::convertToDto);
  }
}
