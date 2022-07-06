package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class DwhAP {

  public static final HibernateEdition dwhAPDbConnection = Database.DWH_AP.getConnection();
}
