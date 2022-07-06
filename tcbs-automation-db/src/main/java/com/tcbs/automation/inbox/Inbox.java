package com.tcbs.automation.inbox;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Inbox {
  public static HibernateEdition inboxDbConnection = Database.INBOX.getConnection();
}
