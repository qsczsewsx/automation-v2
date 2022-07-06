package com.tcbs.automation.stockgate;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class StockgateConnBridge {

  public static final HibernateEdition stockgateConnection = Database.STOCK_GATE.getConnection();

}
