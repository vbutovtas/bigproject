package com.project.integration.serv.dto;

import com.project.integration.dao.entity.Employee;
import com.project.integration.serv.enums.TicketSeverity;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.enums.TicketType;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketDto {
  Integer id;
  EmployeeDto assignee;
  EmployeeDto reporter;
  TicketDto ticket;
  String name;
  String description;
  LocalDate dueDate;
  Float estimatedTime;
  Float loggedTime;
  TicketStatus status;
  TicketType type;
  TicketSeverity severity;
  String gitRef;
  Set<CommentDto> comments = new HashSet<>();
  Set<Employee> employees = new HashSet<>();

  public TicketDto(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}
