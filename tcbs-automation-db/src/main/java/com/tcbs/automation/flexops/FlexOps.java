package com.tcbs.automation.flexops;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class FlexOps {
  public static final HibernateEdition flexOps = Database.FLEXOPS.getConnection();
}
