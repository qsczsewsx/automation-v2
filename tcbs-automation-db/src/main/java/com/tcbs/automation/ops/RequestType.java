package com.tcbs.automation.ops;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum RequestType implements Serializable {
  PHONG_TOA("PHONG_TOA", "Phong tỏa"),
  GIAI_TOA("GIAI_TOA", "Giải tỏa");
  private String value;
  private String name;

  @JsonCreator
  public static RequestType fromValue(String value) {
    Optional<RequestType> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    throw new RuntimeException();
  }

  @Override
  public String toString() {
    return value;
  }
}
