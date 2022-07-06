package com.tcbs.automation.stoxplus;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class StoxplusV2 {
  public static final HibernateEdition stoxV2DbConnection = Database.STOXPLUS_V2.getConnection();
}
