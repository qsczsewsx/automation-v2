package com.tcbs.automation.clients.krema;

import com.tcbs.automation.clients.krema.dto.AccountsSEResDto;
import com.tcbs.automation.clients.krema.dto.CashInvestmentItemResDto;
import com.tcbs.automation.clients.krema.exception.KremaResException;

import java.util.Optional;

public interface KremaClient {
  Optional<CashInvestmentItemResDto> getCashInvestment(String accountNo);

  AccountsSEResDto getAccountSE(String accountNo) throws KremaResException;
}
