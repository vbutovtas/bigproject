package com.project.integration.dao.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  User user;

  @Column(name = "date_of_birth")
  LocalDate birthDate;

  String position;
  String technologies;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "experience_before")
  Float experience;

  Blob photo;

  @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
  Set<Ticket> assigneeTickets = new HashSet<>();

  @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
  Set<Ticket> reporterTickets = new HashSet<>();

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  Set<Comment> comments = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "projects_has_employees",
      joinColumns = @JoinColumn(name = "employees_id"),
      inverseJoinColumns = @JoinColumn(name = "tickets_id"))
  Set<Ticket> projects = new HashSet<>();

  public Employee(Integer id, User user, LocalDate birthDate, String position,
      String technologies, Blob photo) {
    this.id = id;
    this.user = user;
    this.birthDate = birthDate;
    this.position = position;
    this.technologies = technologies;

    //DON'T DELETE. IT'S USEFUL WHEN MANAGER WILL CREATE EMPLOYEE ACC
    //IT'S USED TO PARSE WORK EXPERIENCE FROM STRING

//    if( Objects.nonNull(experienceString)){
//      Pattern integerPattern = Pattern.compile("\\d+");
//      Matcher matcher = integerPattern.matcher(experienceString);
//      List<String> integerList = new ArrayList<>();
//      while (matcher.find()) {
//        integerList.add(matcher.group());
//      }
//      this.experience =
//          Float.parseFloat(integerList.get(0)) + Float.parseFloat(integerList.get(1)) / 12;
//    }
    this.photo = photo;
  }
}
