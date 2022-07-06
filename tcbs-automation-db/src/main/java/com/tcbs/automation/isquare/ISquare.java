package com.tcbs.automation.isquare;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class ISquare {
  public static final HibernateEdition iSquareDbConnection = Database.ISQUARE.getConnection();

  private ISquare() {

  }
}
