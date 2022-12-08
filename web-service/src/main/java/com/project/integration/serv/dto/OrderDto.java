package com.project.integration.serv.dto;

import com.project.integration.dao.entity.Ticket;
import com.project.integration.serv.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ArrayUtils;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
  Integer id;
  UserDto client;
  Ticket project;
  Byte[] description;
  LocalDate startDate;
  BigDecimal cost;
  OrderStatus status;

  public OrderDto(UserDto client, byte[] description) {
    this.client = client;
    this.description = ArrayUtils.toObject(description);
  }
}
