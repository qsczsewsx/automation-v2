package com.tcbs.automation.enumerable.orion.hercules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CreatedChannel {
  THEO_SAN_PHAM("THEO_SAN_PHAM", "Theo sản phẩm"),
  KHONG_THEO_SAN_PHAM("KHONG_THEO_SAN_PHAM", "Không theo sản phẩm");
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

//    @JsonCreator
//    public static CreatedChannel fromValue(String value) {
//        Optional<CreatedChannel> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
//        if (comp.isPresent()) {
//            return comp.get();
//        }
//        throw new BindingException("Unknown status: " + value);
//    }
}
