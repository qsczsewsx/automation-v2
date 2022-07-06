package com.tcbs.automation.ivoucher;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class IVoucher {
  static HibernateEdition ivoucherDbConnection = Database.IVOUCHER.getConnection();
}