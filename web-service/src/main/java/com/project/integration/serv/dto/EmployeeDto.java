package com.project.integration.serv.dto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
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
  LocalDate startDate;
  String experience;
  String photo;
  Set<TicketDto> assigneeTickets = new HashSet<>();
  Set<TicketDto> reporterTickets = new HashSet<>();
  Set<CommentDto> comments = new HashSet<>();
  TicketDto currentProject;
  int projectsCount;

  public EmployeeDto(
      Integer id,
      UserDto userDto,
      LocalDate birthDate,
      String position,
      String technologies,
      LocalDate startDate,
      Float experience,
      String photo,
      int projectsCount,
      TicketDto currentProject) {
    this.id = id;
    this.user = userDto;
    this.birthDate = birthDate;
    this.position = position;
    this.technologies = technologies;
    this.startDate = startDate;
    long monthsBetween =
        ChronoUnit.MONTHS.between(YearMonth.from(startDate), YearMonth.from(LocalDate.now()));
    long years = experience.intValue() + monthsBetween / 12;
    long months =
        (int) ((experience - experience.intValue()) * 12) + monthsBetween % 12;
    years += months / 12;
    months %= 12;
    this.experience = String.format("%1$d год, %2$d месяцев", years, months);
    this.photo = photo;
    this.projectsCount = projectsCount;
    this.currentProject = currentProject;
  }
}
