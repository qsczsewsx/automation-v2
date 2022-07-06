package com.tcbs.automation.riskcloud;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class AwsRiskCloud {
  public static final HibernateEdition awsRiskCloudDbConnection = Database.AWS_RISK_CLOUD.getConnection();
}