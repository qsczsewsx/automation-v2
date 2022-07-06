package com.tcbs.automation.config.risk;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class RiskConfig {
  private static final Config conf = new ConfigImpl("risk").getConf();

  public static final String RISK_DOMAIN_URL = conf.getString("risk.domain");

  public static final String RISK_DOMAIN_INTERNAL_V1_URL = conf.getString("risk.domainInternalV1");
  public static final String RISK_DOMAIN_PUBLIC_V1_URL = conf.getString("risk.domainPublicV1");
  public static final String RISK_DOMAIN_IMPACT_V1_URL = conf.getString("risk.domainImpactV1");

  public static final String RISK_DOMAIN_IRIS_BOND_V1_URL = conf.getString("risk.domainIrisBondV1");

  //condition
  public static final String GET_RISK_CONDITION_URL = conf.getString("risk.riskConditionUrl.getAllRiskConditionsUrl");
  public static final String UPDATE_RISK_CONDITION_URL = conf.getString("risk.riskConditionUrl.updateRiskConditionUrl");

  //margin log
  public static final String GET_MARGIN_LOG_URL = conf.getString("risk.marginLog.getMarginLog");

  //risk config
  public static final String UPDATE_RISK_CONFIG_URL = conf.getString("risk.riskConfig.updateRiskConfigUrl");
  public static final String GET_RISK_CONFIG_URL = conf.getString("risk.riskConfig.getRiskConfigUrl");
  public static final String GET_RISK_CHECK_LIST_URL = conf.getString("risk.riskMarginUrl.checkList");

  // review margin
  public static final String REVIEW_MARGIN_URL = conf.getString("risk.margin.getReviewMarginUrl");
  public static final String ESTIMATED_FULL_LIST_URL = conf.getString("risk.margin.getEstimatedFullListUrl");
  public static final String RISK_ESTIMATED_FULL_LIST_X_API_KEY = conf.getString("risk.margin.x-api-key");

  //allowed ticker list
  public static final String ALLOWED_TICKER_LIST_URL = conf.getString("risk.margin.getAllowedTickerListUrl");

  //allowed ticker list
  public static final String WEEKLY_TICKER_LIST_URL = conf.getString("risk.margin.getWeeklyTickerListUrl");

  // assignment
  public static final String GET_ALL_ASSIGNMENT_INFO_URL = conf.getString("risk.assignmentInfo.getAllAssignment");
  public static final String GET_ALL_TICKERS_URL = conf.getString("risk.assignmentInfo.getAlTickers");
  public static final String POST_ASSIGNMENT_URL = conf.getString("risk.assignmentInfo.postAssignmentUrl");
  public static final String POST_REVIEW_MARGIN_URL = conf.getString("risk.assignmentInfo.reviewMarginUrl");
  public static final String GET_ASSIGN_TASK_URL = conf.getString("risk.assignmentInfo.assignTaskUrl");

  //basket
  public static final String BASKET_URL = conf.getString("risk.basket.basket");
  public static final String BASKET_CONFIG_URL = conf.getString("risk.basket.basketConfig");
  public static final String BASKET_FLEX_URL = conf.getString("risk.basket.basketFlex");

  //process

  public static final String PROCESS_URL = conf.getString("risk.process.process");
  // ClosePriceAdjusted

  public static final String GET_CLOSE_PRICE_ADJUSTED_URL = conf.getString("risk.closePriceAdjust.closePriceAdjust");
  // add Ticker

  public static final String GET_ADD_TICKER_URL = conf.getString("risk.addTicker.addTicker");

  // log

  public static final String POST_LOG_URL = conf.getString("risk.log.log");
  // Sync00120023

  public static final String GET_SYNC_0012_0023_URL = conf.getString("risk.sync00120023.sync00120023");

  // removeTicker

  public static final String GET_REMOVE_TICKER_URL = conf.getString("risk.removeTicker.getRemoveTicker");

  //update blacklist

  public static final String UPDATE_BLACKLIST_URL = conf.getString("risk.updateBlackList.updateBlackList");

  //post web
  public static final String UPDATE_FILE_EXCEL_URL = conf.getString("risk.uploadFileExcel.uploadFileExcel");

  // effective date
  public static final String GET_EFFECTIVE_DATE_URL = conf.getString("risk.getEffectiveDate.getEffectiveDate");

  // sync Ma5
  public static final String SYNC_MA5_URL = conf.getString("risk.syncMa5.syncMa5");


  // post margin to crm
  public static final String CRM_HOST = conf.getString("risk.crm.crmGetUrl");
  public static final String POST_AUTH_CRM_URL = conf.getString("risk.crm.authCRMUrl");
  public static final String POST_MARGIN_CRM_URL = conf.getString("risk.crm.postMarginCRMUrl");
  public static final String GET_CRM_RESULT_URL = conf.getString("risk.crm.getAllResultUrl");
  //post margin to crm exchange
  public static final String POST_MARGIN_CRM_EXCHANGE_URL = conf.getString("risk.crm.postMarginCRMExchangeUrl");

  //check working date
  public static final String GET_WORKING_DATE_URL = conf.getString("risk.getWorkingDate.getWorkingDate");
  public static final String ADD_WORKING_DATE_URL = conf.getString("risk.addWorkingDate.addWorkingDate");

  //impact
  public static final String GET_TICKER_CHANGE_URL = conf.getString("risk.impact.getTickerChange");
  public static final String SYNC_CUS_URL = conf.getString("risk.impact.syncCus");
  public static final String SYNC_STOCK_URL = conf.getString("risk.impact.syncStock");
  public static final String POST_CRM_IMPACT_URL = conf.getString("risk.impact.postCrm");
  public static final String GET_BONUS_SHARE_URL = conf.getString("risk.impact.getDetailBonusShare");
  public static final String GET_IMPACT_DATA_VALID_URL = conf.getString("risk.impact.getImpactDataValid");

  //impact daily
  public static final String POST_DAILY01_CUS_URL = conf.getString("risk.impactDaily.postDaily01Cus");
  public static final String POST_DAILY02_STOCK_URL = conf.getString("risk.impactDaily.postDaily02Stock");
  public static final String GET_DAILY03_CALCULATE_RTT_URL = conf.getString("risk.impactDaily.postDaily03CalculrateRtt");


  //fin5
  public static final String GET_INDICATOR_URL = conf.getString("risk.fin5.getIndicator");

  //monitor
  public static final String GET_CHECK_VALID_URL = conf.getString("risk.monitor.checkValid");
  public static final String GET_MANAGER_URL = conf.getString("risk.monitor.manager");

  //impact Daily

  public static final String GET_INPUT_CAL_URL = conf.getString("risk.impactDaily.calDataInput");
  public static final String UPDATE_INPUT_CAL_URL = conf.getString("risk.impactDaily.updateDataInput");

  //venus

  public static final String GET_SYNC_DATA_URL = conf.getString("risk.venus.syncData");
  public static final String POST_CHECK_DRAFT_URL = conf.getString("risk.venus.checkDraft");
  public static final String GET_SYNC_LOAN_DATA_URL = conf.getString("risk.venus.syncLoanData");

  //coco

  public static final String UPDATE_DWH_URL = conf.getString("risk.coco.updateDwh");
  public static final String POST_NOTIFY_URL = conf.getString("risk.coco.postNotify");

  //expireDate

  public static final String GET_EXPIRE_DATE_URL = conf.getString("risk.expireDate.expireDate");
  public static final String CAL_EXPIRE_DATE_URL = conf.getString("risk.expireDate.calExpireDate");

  //bond

  public static final String CONFIG_LIMIT_URL = conf.getString("risk.bond.configLimit");
  public static final String CONFIG_LIMIT_ALL_URL = conf.getString("risk.bond.configLimitAll");
  public static final String CASH_MOVEMENT_URL = conf.getString("risk.bond.cashMovement");
  public static final String RISK_BOND_SYNC_VENUS_URL = conf.getString("risk.bond.syncVenus");
  //pv01
  public static final String GET_CURVE_URL = conf.getString("risk.pv01.curve");

}
