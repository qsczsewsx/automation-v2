package com.tcbs.automation.config.iplan;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IplanConfig {
  public static final String IFUND_GETBALANCESACCOUNTRM = "cho team fund chuyen code sang project moi";
  private static final Config conf = new ConfigImpl("iplan").getConf();
  public static final String IPLAN_CUSTOMER_CREATE_CBF = conf.getString("starwars.customerCreateCBF");
  public static final String IPLAN_RM_CREATE_CBF = conf.getString("starwars.rmCreateCBF");
  public static final String IPLAN_REMOVE_PLAN = conf.getString("starwars.removePlan");
  public static final String IPLAN_STABLE_INCOME_BASE = conf.getString("starwars.removePlan");
  public static final String IPLAN_CUSTOMER_BASE = conf.getString("starwars.customerBase");
  public static final String IPLAN_RM_BASE = conf.getString("starwars.rmBase");
  public static final String IPLAN_VLAG_BASE = conf.getString("starwars.vlagApiBase");
  public static final String IPLAN_RM_CREATE_TNOD = conf.getString("starwars.rmCreateTNOD");
  public static final String IPLAN_CUSTOMER_CREATE_TNOD = conf.getString("starwars.customerCreateTNOD");
  public static final String IPLAN_GENERATE_TIMELINE_BONDCONVERSION = conf.getString("starwars.generateTimelineBondConversion");
  public static final String IPLAN_GET_DATA_CBF = conf.getString("starwars.getDataCBF");
  public static final String IPLAN_GET_ACCOUNTING_ITEM = conf.getString("starwars.getAccountingItem");
  public static final String IPLAN_CUSTOMER_CONFIRM_TNOD = conf.getString("starwars.customerConfirmTNOD");
  public static final String IPLAN_CUSTOMER_VLAG = conf.getString("starwars.customerVLAG");
  public static final String IPLAN_INVEST_AMT = conf.getString("starwars.investamount");
  public static final String IPLAN_UPDATE_CB = conf.getString("starwars.updateCB");
  public static final String IPLAN_TCWEALTH_BASE = conf.getString("starwars.iplanTCWealthBase");
  public static final String TCWEALTH_BASE = conf.getString("starwars.tcwealthBase");
  public static final String TCWEALTH_BASE1 = conf.getString("starwars.tcwealthBase1");
  public static final String BOND_IPLAN_CHECKING_DATA = conf.getString("starwars.conversionGetTrackingId");
  public static final String IPLAN_GET_EVENT_BY_GOAL_URL = conf.getString("starwars.getEventByGoal");
  public static final String PRODUCT_INV_URL = conf.getString("starwars.productInvestment");
  public static final String ALLOCATION_INV_URL = conf.getString("starwars.allocationInvestment");
  public static final String PRODUCT_CATEGORY_URL = conf.getString("starwars.productCategory");
  public static final String PRODUCT_SUGGESTION_URL = conf.getString("starwars.productSuggestion");
  public static final String LOAN_PAYMENT_SCHEDULE = conf.getString("starwars.loanPaymentSchedule");
  public static final String INSURANCE_FUND_RATION = conf.getString("starwars.insuranceFundRatio");
  public static final String CASH_FOLLOW_FUND_LIFE = conf.getString("starwars.cashFollowFundLife");
  public static final String BACKEND_VIEW_DETAIL_PLAN_INSTANCE = conf.getString("starwars.backendViewDetailPlanInstance");
  public static final String BACKEND_VIEW_EVENT_AUDIT_LOG = conf.getString("starwars.backendViewEventAuditLog");
  public static final String BACKEND_VIEW_PLAN_AUDIT_LOG = conf.getString("starwars.backendViewPlanAuditLog");
  public static final String PROPERTY_OPTION_GET_LIST_APARTMENT = conf.getString("starwars.propertyOptionGetListApartment");
  public static final String PROPERTY_OPTION_UPDATE_APARTMENT = conf.getString("starwars.propertyOptionUpdateApartmentInfo");
  public static final String PROPERTY_OPTION_CREATE_ORDER = conf.getString("starwars.propertyOptionCreateOrder");
  public static final String IPLAN_VLAG2_BASE = conf.getString("starwars.vlagApiBase2");
  public static final String PROPERTY_OPTION_VIEW_DETAIL_IPLAN = conf.getString("starwars.propertyOptionCusViewDetailIplan");
  public static final String RM_VIEW_PLAN_CP_PROTECTED = conf.getString("starwars.RMViewPlanCapitalProtected");
  public static final String CUS_VIEW_PLAN_CP_PROTECTED = conf.getString("starwars.CusViewPlanCapitalProtected");
  public static final String BACKEND_PLAN_INSTANCE_SEARCH = conf.getString("starwars.BackendPlanInstanceSearch");
  public static final String BACKEND_PLAN_INSTANCE_SEARCH_DETAIL = conf.getString("starwars.BackendPlanInstanceSearchDetail");
}
