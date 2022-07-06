package com.tcbs.automation.conditionalorder;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheConditionalOrder {
  static final Logger logger = LoggerFactory.getLogger(TheConditionalOrder.class);
  public static HibernateEdition anattaDbConnection;
  public static HibernateEdition aniccaDbConnection;

  static {
    try {
      anattaDbConnection = Database.ANATTA.getConnection();
      aniccaDbConnection = Database.ANICCA.getConnection();
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex.getStackTrace());
    }
  }
}
