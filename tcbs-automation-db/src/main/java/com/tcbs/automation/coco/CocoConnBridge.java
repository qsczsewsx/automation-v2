package com.tcbs.automation.coco;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class CocoConnBridge {
  public static final HibernateEdition cocoConnection = Database.COCO.getConnection();
  public static final HibernateEdition socialInvestConnection = Database.SOCIAL_INVEST.getConnection();
}
