package com.project.integration.dao.entity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Entity(name = "orders")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @ManyToOne
  @JoinColumn(name = "client_id")
  User client;

  @NonNull String name;

  @NonNull Blob description;

  @Column(name = "start_date")
  LocalDate startDate;

  BigDecimal cost;

  public Order(User client, @NonNull Blob description) {
    this.client = client;
    this.description = description;
  }
}
