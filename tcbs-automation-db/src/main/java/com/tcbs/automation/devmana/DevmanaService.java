package com.tcbs.automation.devmana;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class DevmanaService {
  public static final HibernateEdition devmanaConnection = Database.DEVMANA.getConnection();
}
