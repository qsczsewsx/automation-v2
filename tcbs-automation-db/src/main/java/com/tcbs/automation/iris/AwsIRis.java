package com.tcbs.automation.iris;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class AwsIRis {
  public static final HibernateEdition AwsIRisDbConnection = Database.AWS_IRIS.getConnection();
}
