package com.tcbs.automation.foureyes;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class FourEyes {
  public static final HibernateEdition foureyesConnection = Database.FOUR_EYES.getConnection();
}
