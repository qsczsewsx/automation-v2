package com.tcbs.automation.clients.krema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsSEResDto {
  private String object;
  private String accountNo;
  private String custodyID;
  private String fullName;
  private List<AccountSEResStockItemDto> stock;
}
