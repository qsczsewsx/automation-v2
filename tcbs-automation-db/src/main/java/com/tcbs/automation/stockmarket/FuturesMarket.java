package com.tcbs.automation.stockmarket;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class FuturesMarket {
  public static final HibernateEdition futuresMarketConnection = Database.FUTURES_MARKET.getConnection();
}
