package com.tcbs.automation.enumerable.orion.hercules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
public enum BondType {
  OTC("OTC", "Chưa niêm yết"),
  LISTED("Listed", "Đã niêm yết");
  @JsonValue
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

  @JsonCreator
  public static BondType fromValue(String value) {
    Optional<BondType> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    log.info("Input error: " + value);
    return null;
  }
}
