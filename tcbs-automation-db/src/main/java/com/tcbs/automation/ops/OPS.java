package com.tcbs.automation.ops;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class OPS {
  public static HibernateEdition opsConnection = Database.OPS.getConnection();
}
