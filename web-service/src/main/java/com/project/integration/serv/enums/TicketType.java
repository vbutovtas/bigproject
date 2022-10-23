package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum TicketType {
  PROJECT("PROJECT"),
  TASK("TASK");

  private final String value;

  TicketType(String value) {
    this.value = value;
  }

  public static TicketType getEnumByValue(String value) {
    return Stream.of(TicketType.values())
        .filter(v -> v.getValue().equals(value))
        .findFirst()
        .orElse(null);
  }

  public String getValue() {
    return value;
  }
}
