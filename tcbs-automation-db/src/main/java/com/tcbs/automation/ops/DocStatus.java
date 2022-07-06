package com.tcbs.automation.ops;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum DocStatus {
  ATTACH("Attach", "Hồ sơ đính kèm"),
  SCAN("Scan", "Hồ sơ scan"),
  ORIGIN("Origin", "Hồ sơ gốc");
  private String value;
  private String name;

  @JsonCreator
  public static DocStatus fromValue(String value) throws Exception {
    Optional<DocStatus> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    throw new Exception("Unknown DocStatus: " + value);
  }

  @Override
  public String toString() {
    return value;
  }
}
