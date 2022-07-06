package com.tcbs.automation.clients.hfc;

import com.tcbs.automation.clients.hfc.dto.*;

public interface HfcClient {
  QueryResPage<CashStatementResDto> getCashStatements(CashStatementReqDto req);

  QueryResPage<StockStatementResDto> getStockStatements(StockStatementReqDto req);

  CashBODBalanceResDto getCashBODBalance(CashBODBalanceReqDto req);
}
