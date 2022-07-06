package com.tcbs.automation.ixu;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class TcXu {
  static HibernateEdition ixuDbConnection = Database.IXU.getConnection();
}