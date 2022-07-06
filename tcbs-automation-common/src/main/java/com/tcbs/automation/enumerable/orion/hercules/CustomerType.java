package com.tcbs.automation.enumerable.orion.hercules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerType {
  VIETNAM("Trong nước"),
  FOREIGN("Nước ngoài");

  private String value;
}
