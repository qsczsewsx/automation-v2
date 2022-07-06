package com.tcbs.automation.ipartner;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class IWpartner {
  public static HibernateEdition iwpDBConnection = Database.IPARTNER.getConnection();
}
