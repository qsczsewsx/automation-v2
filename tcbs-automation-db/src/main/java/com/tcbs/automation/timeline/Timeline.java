package com.tcbs.automation.timeline;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Timeline {
  public static HibernateEdition timelineDbConnection;

  static {
    try {
      timelineDbConnection = Database.TIMELINE.getConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
