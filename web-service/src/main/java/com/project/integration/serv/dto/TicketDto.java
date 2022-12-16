package com.project.integration.serv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.integration.serv.enums.TicketSeverity;
import com.project.integration.serv.enums.TicketStatus;
import com.project.integration.serv.enums.TicketType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
  @NotBlank(message = "Name cannot be empty")
  @Size(min=5, max=100, message = "Name length is from 5 to 100")
  String name;

  @NotBlank(message = "Description cannot be empty")
  @Size(min=10, max=1000, message = "Description length is from 10 to 1000")
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
  @NotBlank(message = "Git link cannot be empty")
  @Size(min=10, max=200, message = "Git link length is from 10 to 200")
  String gitRef;
  Integer order;
  List<EmployeeDto> employees = new ArrayList<>();

  public TicketDto(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}
