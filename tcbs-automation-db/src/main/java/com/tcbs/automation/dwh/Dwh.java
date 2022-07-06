package com.tcbs.automation.dwh;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Dwh {
  public static HibernateEdition dwhDbConnection = Database.DWH.getConnection();
}
