package com.tcbs.automation.tcwealth;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;


public class Tcwealth {
  public static HibernateEdition tcWealthDbConnection = Database.TCWEALTH.getConnection();
}
