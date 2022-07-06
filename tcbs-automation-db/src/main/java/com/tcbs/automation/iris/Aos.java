package com.tcbs.automation.iris;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;


public class Aos {
  public static final HibernateEdition aosDbConnection = Database.AOS.getConnection();
}