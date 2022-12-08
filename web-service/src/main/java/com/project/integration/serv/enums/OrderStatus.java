package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum OrderStatus {
  OPEN("OPEN"),
  IS_PROCESSED("IS_PROCESSED"),
  DONE("DONE");
  private final String value;

  OrderStatus(String value) {
    this.value = value;
  }

  public static OrderStatus getEnumByValue(String value) {
    return Stream.of(OrderStatus.values())
        .filter(v -> v.getValue().equals(value))
        .findFirst()
        .orElse(null);
  }

  public String getValue() {
    return value;
  }
}
