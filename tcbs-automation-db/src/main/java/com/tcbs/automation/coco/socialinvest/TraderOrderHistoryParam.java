package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.constants.coco.Constants;
import com.tcbs.automation.tools.DateUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;

@Data
@Builder
public class TraderOrderHistoryParam {
  private Long traderId;
  private String symbol;
  private Constants.OrderSide orderSide;
  private String priceType;
  private Date fromDate;
  private Date toDate;
  private int page;
  private int size;

  public MultiValueMap<String, String> toQueryParamMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("page", String.valueOf(page));
    if (size > 0) {
      map.add("size", String.valueOf(size));
    }
    if (symbol != null) {
      map.add("symbol", symbol);
    }
    if (orderSide != null) {
      map.add("orderSide", Constants.OrderSide.stringValue(orderSide));
    }
    if (priceType != null) {
      map.add("priceType", priceType);
    }
    if (fromDate != null) {
      map.add("fromDate", DateUtils.toString(fromDate, DateUtils.SHORT_DATE_FORMAT_2));
    }
    if (toDate != null) {
      map.add("toDate", DateUtils.toString(toDate, DateUtils.SHORT_DATE_FORMAT_2));
    }
    return map;
  }

}
