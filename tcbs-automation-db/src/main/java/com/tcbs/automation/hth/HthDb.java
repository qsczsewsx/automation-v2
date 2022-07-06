package com.tcbs.automation.hth;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class HthDb {
  public static final HibernateEdition h2hConnection = Database.H2H.getConnection();

}
