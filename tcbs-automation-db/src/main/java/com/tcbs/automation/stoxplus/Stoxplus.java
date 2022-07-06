package com.tcbs.automation.stoxplus;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Stoxplus {
  public static final HibernateEdition stoxDbConnection = Database.STOXPLUS.getConnection();
}
