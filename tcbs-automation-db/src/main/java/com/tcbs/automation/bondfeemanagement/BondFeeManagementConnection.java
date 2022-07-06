package com.tcbs.automation.bondfeemanagement;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class BondFeeManagementConnection {
  public static final HibernateEdition CONNECTION = Database.BOND_FEE_MANAGEMENT.getConnection();

  private BondFeeManagementConnection() {

  }
}
