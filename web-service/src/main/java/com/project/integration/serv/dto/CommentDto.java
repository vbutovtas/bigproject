package com.project.integration.serv.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
  Integer id;
  String comment;
  EmployeeDto employee;
  TicketDto ticket;
}
