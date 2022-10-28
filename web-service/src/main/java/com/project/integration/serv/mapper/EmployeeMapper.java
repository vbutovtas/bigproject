package com.project.integration.serv.mapper;

import com.project.integration.dao.entity.Employee;
import com.project.integration.dao.entity.Ticket;
import com.project.integration.serv.dto.EmployeeDto;
import com.project.integration.serv.dto.TicketDto;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.enums.TicketType;
import java.sql.Blob;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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

    TypeMap<Employee, EmployeeDto> employeeMapper =
        this.modelMapper.createTypeMap(Employee.class, EmployeeDto.class);

    Converter<Blob, String> photoConverter = ctx -> {
      try {
        if(Objects.nonNull(ctx.getSource())){
          byte[] photoAsBytes = ctx.getSource().getBytes(1, (int) ctx.getSource().length());
          return Base64.getEncoder().encodeToString(photoAsBytes);
        } else return "";
      } catch (Exception e) {
        throw new RuntimeException(e); // TODO
      }
    };

//    Converter<Set<Ticket>, Ticket> currentProjectConverter =
//        ctx -> {

//          return currentProject.orElse(null);
//        };

    Converter<Set<Ticket>, Integer> projectsCountConverter =
        ctx -> ctx.getSource().size();

    employeeMapper.addMappings(
        mapper -> mapper.using(photoConverter).map(Employee::getPhoto, EmployeeDto::setPhoto));

//    employeeMapper.addMappings(
//        mapper -> mapper.using(currentProjectConverter).map(Employee::getProjects, EmployeeDto::setCurrentProject));

    employeeMapper.addMappings(
        mapper -> mapper.using(projectsCountConverter).map(Employee::getProjects, EmployeeDto::setProjectsCount));

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
    if(currentProject.isPresent()){
      TicketDto currentProjectDto = modelMapper.map(currentProject.get(), TicketDto.class);
      employeeDto.setCurrentProject(currentProjectDto);
    }
    return employeeDto;
  }
}
