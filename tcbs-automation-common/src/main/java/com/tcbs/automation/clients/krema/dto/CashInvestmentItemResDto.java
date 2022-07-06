package com.tcbs.automation.clients.krema.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashInvestmentItemResDto {
  private String accountNo;
  private String custodyID;
  private Double pp0;
  private Double bankAvlBalance;
  private Double avlWithdraw;
  private Double avlAdvanceAmount;
  private Double buyingAmount;
  private Double blockAmount;
  private Double cashDevident;
  private Double balance;


  public double getAvailableCash() {
    if (avlWithdraw != null) {
      return avlWithdraw + bankAvlBalance;
    }
    return 0;
  }
}
