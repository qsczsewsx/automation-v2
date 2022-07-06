package com.tcbs.automation.config.workflowengine;

import static com.tcbs.automation.config.productservice.ProductServiceHandler.BUNDLE;
import static com.tcbs.automation.config.productservice.ProductServiceHandler.UNDERLYING_CODE;

public class WorkflowEngineHandler {
  public static final String INSERT = "INSERT";
  public static final String UPDATE = "UPDATE";
  public static final String DELETE = "DELETE";
  public static final String CANCELED = "CANCELED";
  public static final String SERVICE_NAME = "tcbs-product";
  public static final String FINANCIAL_TERMS = "financial-terms";
  public static final String TRADABLE_PRODUCT = "tradable-product";
  public static final String CATEGORY_BOND = "BOND";
  public static final String MAKER = "maker";
  public static final Integer WORKFLOW_TEMPLATE_ID = 1;
  public static final String PAYLOAD_IDENTIFIER = "";
  public static final String CODE = "Test02001";
  public static final String PRODUCT_CODE = CODE + "." + UNDERLYING_CODE + "." + BUNDLE;
  public static final String WORKFLOW_DESCRIPTION = "description by tung ";
  public static final String INPROGRESS = "INPROGRESS";
  public static final String CHECKER = "CHECKER";
  public static final String M_C_ROLE = "M_C_ROLE";
  public static final String REJECTED = "REJECTED";
  public static final String COMPLETED = "COMPLETED";
  public static final String SYSTEM = "SYSTEM";
  public static final String ACTION_REJECT = "reject";
  public static final String ACTION_APPROVE = "approve";
}
