package com.tcbs.automation.tcbsdwh;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Tcbsdwh {
  public static final HibernateEdition tcbsDwhDbConnection = Database.TCBS_DWH.getConnection();
  public static final HibernateEdition tcbsStagingDwhDbConnection = Database.TCBS_DWH_STAGING.getConnection();
  public static final HibernateEdition tcbsDbMartDwhDbConnection = Database.AWS_TCBS_DWH.getConnection();
}
