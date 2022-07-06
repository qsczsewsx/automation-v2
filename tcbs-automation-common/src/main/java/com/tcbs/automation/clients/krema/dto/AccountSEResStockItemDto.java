package com.tcbs.automation.clients.krema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSEResStockItemDto {
  private String symbol;
  private String secType;
  private String secTypeName;
  private Float availableTrading;
  private Float mortgaged;
  private Float t0;
  private Float t1;
  private Float t2;
  private Float blocked;
  private Float securedQuantity;
  private Float sellRemain;
  private Float exercisedCA;
  private Float unexercisedCA;
  private Float stockDividend;
  private Float cashDividend;
  private Float waitForTransfer;
  private Float waitForWithdraw;
  private Float waitForTrade;
  private Float currentPrice;
  private Float costPrice;

  public Float getUnavailableTrading() {
    return t0 + t1 + t2 + stockDividend + blocked + mortgaged + sellRemain + exercisedCA;
  }

  public Float getQuantityRemain() {
    return availableTrading + t0 + t1 + t2 + sellRemain;
  }
}
