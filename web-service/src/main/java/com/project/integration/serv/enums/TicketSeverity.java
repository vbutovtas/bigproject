package com.project.integration.serv.enums;

import java.util.Arrays;

public enum TicketSeverity {
  CRITICAL(1),
  MAJOR(2),
  MINOR(3);

  private final int value;

  TicketSeverity(final int value) {
    this.value = value;
  }

  public static TicketSeverity getEnumByValue(int value) {
    return Arrays.stream(values())
        .filter(ticketSeverity -> ticketSeverity.value == value)
        .findFirst()
        .orElse(null);
  }

  public int getValue() {
    return value;
  }
}
