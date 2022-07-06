package com.tcbs.automation.staging;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class AwsTcbsDwh {
  public static final HibernateEdition awsTcbsDwhDbConnection = Database.AWS_TCBS_DWH.getConnection();
}