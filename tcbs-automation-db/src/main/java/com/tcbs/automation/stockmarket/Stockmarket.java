package com.tcbs.automation.stockmarket;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Stockmarket {
  public static final HibernateEdition stockMarketConnection = Database.STOCK_MARKET.getConnection();
}
