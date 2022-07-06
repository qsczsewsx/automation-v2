package com.tcbs.automation.config.idatatcanalysis;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class IData {
  public static final HibernateEdition idataDbConnection = Database.IDATA.getConnection();
}
