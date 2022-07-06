package com.tcbs.automation.clients.flex;

import com.tcbs.automation.clients.flex.dto.*;
import com.tcbs.automation.clients.flex.exception.FlexApiResException;
import com.tcbs.automation.clients.krema.dto.AccountsSEResDto;
import com.tcbs.automation.clients.krema.dto.CashInvestmentItemResDto;

import java.util.Optional;

public interface FlexClient {
  AccountResDto getAccountList(QueryAccountListParam param);

  // dto(s) moved to krema.dto
  @Deprecated
  Optional<CashInvestmentItemResDto> getCashInvestment(String accountNo);

  @Deprecated
  AccountsSEResDto getAccountSE(String accountNo) throws FlexApiResException;
}
