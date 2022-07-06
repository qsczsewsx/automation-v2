package com.tcbs.automation.enumerable.orion.hercules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UnBlockPurpose {
  DA_TRA_GOC_LAI("DA_TRA_GOC_LAI", "KH đã trả gốc và lãi vay"),
  BAN_DE_TRA_GOC_LAI("BAN_DE_TRA_GOC_LAI", "KH bán để trả gốc và lãi vay");
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

}
