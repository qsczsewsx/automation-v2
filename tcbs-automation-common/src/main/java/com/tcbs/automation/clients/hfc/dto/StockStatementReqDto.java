package com.tcbs.automation.clients.hfc.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

@Getter
@Setter
@SuperBuilder
public class StockStatementReqDto extends HfcReqDto {
  private String symbol;

  @Override
  public MultiValueMap<String, String> toHfcQueryParamMap() {
    MultiValueMap<String, String> map = super.toHfcQueryParamMap();
    if (!StringUtils.isEmpty(symbol)) {
      map.add("symbol", symbol);
    }
    return map;
  }

  @Override
  public MultiValueMap<String, String> toIcopyQueryParamMap() {
    MultiValueMap<String, String> map = super.toIcopyQueryParamMap();
    if (!StringUtils.isEmpty(symbol)) {
      map.add("symbol", symbol);
    }
    return map;
  }
}
