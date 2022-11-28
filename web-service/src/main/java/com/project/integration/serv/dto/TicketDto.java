package com.project.integration.serv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.integration.serv.enums.TicketSeverity;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.enums.TicketType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  LocalDateTime createDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  LocalDateTime dueDate;
  Float estimatedTime;
  Float loggedTime;
  TicketStatus status;
  TicketType type;
  TicketSeverity severity;
  String gitRef;
  Integer order;
  List<EmployeeDto> employees = new ArrayList<>();

  public TicketDto(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}
