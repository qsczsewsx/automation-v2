package com.tcbs.automation.investingbundle;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class InvestingBundleDB {
  public static final HibernateEdition investingBundleDbConnection = Database.INVESTING_BUNDLE.getConnection();
}
