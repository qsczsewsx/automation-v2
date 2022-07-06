package com.tcbs.automation.hfcdata;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class HfcData {
  public static final HibernateEdition hfcDataDbConnection = Database.HFC_DATA.getConnection();
  public static final HibernateEdition dwhDbConnection = Database.DWH.getConnection();
  public static final HibernateEdition tcbsDwhDbConnection = Database.TCBS_DWH.getConnection();
  public static final HibernateEdition redshiftStockMarket = Database.REDSHIFT_STOCK_MARKET.getConnection();
  public static final HibernateEdition redshiftStaging = Database.REDSHIFT_STAGING.getConnection();

  public static final HibernateEdition tcbsDwhWriteDbConnection = Database.TCBS_DWH_WRITE.getConnection();
}
