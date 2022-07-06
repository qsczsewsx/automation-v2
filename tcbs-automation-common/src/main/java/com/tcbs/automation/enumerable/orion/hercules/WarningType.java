package com.tcbs.automation.enumerable.orion.hercules;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum WarningType {
  TU_CHOI("TU_CHOI", "Bị từ chối"),
  CAN_BO_SUNG_HO_SO("CAN_BO_SUNG_HO_SO", "Cần bổ sung hồ sơ");
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

  @JsonCreator
  public static WarningType fromValue(String value) throws Exception {
    Optional<WarningType> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    throw new Exception("Input=" + value);
  }
}
