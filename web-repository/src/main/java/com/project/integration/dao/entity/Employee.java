package com.project.integration.dao.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

  @ManyToMany(mappedBy = "employees")
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
