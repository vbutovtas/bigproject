package com.project.integration.dao.entity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @ManyToOne
  @JoinColumn(name = "client_id")
  User client;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "project_id", referencedColumnName = "id")
  Ticket project;

  @NonNull Blob description;

  @Column(name = "create_date")
  LocalDate startDate;

  BigDecimal cost;

  String status;
}
