package com.tcbs.automation.bondlifecycle;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class BondLifeCycleConnection {
  public static final HibernateEdition CONNECTION = Database.BONDLIFECYCLE.getConnection();

  private BondLifeCycleConnection() {

  }
}
