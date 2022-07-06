package com.tcbs.automation.staging;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class AwsStagingDwh {
  public static final HibernateEdition awsStagingDwhDbConnection = Database.AWS_STAGING_DWH.getConnection();
}