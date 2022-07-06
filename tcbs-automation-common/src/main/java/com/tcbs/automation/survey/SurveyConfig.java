package com.tcbs.automation.survey;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class SurveyConfig {
  private static final Config conf = new ConfigImpl("survey").getConf();

  //Survey
  public static final String SURVEY_DOMAIN = conf.getString("survey.domain_survey");
  public static final String SURVEY_DOMAIN_TYPEFORM = conf.getString("survey.domain_survey_typeform");

  public static final String TYPEFORM_SURVEY_KVRR = conf.getString("survey.typeform_survey_kvrr");

}
