package com.tcbs.automation.enumerable.orion.hercules;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PledgeType {
  PHONG_TOA("PHONG_TOA", "Phong tỏa"),
  GIAI_TOA("GIAI_TOA", "Giải tỏa");
  @JsonValue
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

}
