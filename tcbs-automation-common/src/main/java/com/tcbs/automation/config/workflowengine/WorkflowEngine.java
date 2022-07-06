package com.tcbs.automation.config.workflowengine;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class WorkflowEngine {
  private static final Config conf = new ConfigImpl("workflowengine").getConf();

  //Portfolio
  public static final String WORKFLOW_DOMAIN = conf.getString("workflowengine.domain");
  public static final String WORKFLOW_ENGINE = conf.getString("workflowengine.workflow");
  public static final String WORKFLOW_XAPI_MAKER = conf.getString("workflowengine.maker-xapi-key");
  public static final String WORKFLOW_XAPI_CHECKER = conf.getString("workflowengine.checker-xapi-key");
  public static final String WORKFLOW_XAPI_SYSTEM = conf.getString("workflowengine.system-xapi-key");
  public static final String WORKFLOW_XAPI_M_C_ROLE = conf.getString("workflowengine.mc-xapi-key");

  public static final String WORKFLOW_UPDATE = conf.getString("workflowengine.update");
}
