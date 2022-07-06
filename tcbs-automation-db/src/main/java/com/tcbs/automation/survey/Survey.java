package com.tcbs.automation.survey;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Survey {
  public static HibernateEdition surveyDbConnection = Database.SURVEY.getConnection();
}
