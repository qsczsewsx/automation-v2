package com.tcbs.automation.intermana;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class IntermanaService {
  public static final HibernateEdition intermanaConnection = Database.INTERMANA.getConnection();
}
