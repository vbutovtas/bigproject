package com.project.integration.serv.enums;

import java.util.stream.Stream;

public enum TicketStatus {
  OPEN("OPEN"),
  IN_DESIGN("IN_DESIGN"),
  IN_BUILD("IN_BUILD"),
  READY_FOR_TEST("READY_FOR_TEST"),
  CLOSE("CLOSE");

  private final String value;

  TicketStatus(String value) {
    this.value = value;
  }

  public static TicketStatus getEnumByValue(String value) {
    return Stream.of(TicketStatus.values())
        .filter(v -> v.getValue().equals(value))
        .findFirst()
        .orElse(null);
  }

  public String getValue() {
    return value;
  }
}
