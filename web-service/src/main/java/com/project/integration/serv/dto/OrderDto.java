package com.project.integration.serv.dto;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
  Integer id;
  UserDto client;
  String name;
  Blob description;
  LocalDate startDate;
  BigDecimal cost;

  public OrderDto(UserDto client, Blob description) {
    this.client = client;
    this.description = description;
  }
}
