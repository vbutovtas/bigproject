package com.project.integration.serv.dto;

import java.sql.Blob;
import java.sql.SQLException;
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
  String experience;
  String photo;
  Set<TicketDto> assigneeTickets = new HashSet<>();
  Set<TicketDto> reporterTickets = new HashSet<>();
  Set<CommentDto> comments = new HashSet<>();

  public EmployeeDto(
      UserDto userDto,
      LocalDate birthDate,
      String position,
      String technologies,
      Float experience,
      String photo) {
    this.user = userDto;
    this.birthDate = birthDate;
    this.position = position;
    this.technologies = technologies;
    this.experience = String.format(
        "%1$d год, %2$d месяцев", experience.intValue(), (int)((experience - experience.intValue())*12));
    this.photo = photo;
  }
}
