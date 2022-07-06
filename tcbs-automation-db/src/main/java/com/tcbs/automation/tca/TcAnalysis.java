package com.tcbs.automation.tca;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class TcAnalysis {
  public static final HibernateEdition tcaDbConnection = Database.TCA.getConnection();
}
