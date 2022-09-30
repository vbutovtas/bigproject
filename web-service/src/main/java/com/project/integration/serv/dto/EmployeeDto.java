package com.project.integration.serv.dto;

import java.sql.Blob;
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
public class EmployeeDto {
  Integer id;
  UserDto user;
  LocalDate birthDate;
  String position;
  String technologies;
  Integer experience;
  Blob photo;
  Set<TicketDto> assigneeTickets = new HashSet<>();
  Set<TicketDto> reporterTickets = new HashSet<>();
  Set<CommentDto> comments = new HashSet<>();
}
