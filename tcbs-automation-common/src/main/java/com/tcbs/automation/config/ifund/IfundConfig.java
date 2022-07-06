package com.tcbs.automation.config.ifund;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IfundConfig {
  private static final Config conf = new ConfigImpl("ifund").getConf();

  public static final String SERVICE_TEST = conf.getString("ifund.serviceTest");
  public static final String PRICING_DOMAIN = conf.getString(getPricingDomain());
  public static final String PRICING_POSTCASHBACKVIP = conf.getString("ifund.cashbackVip");
  public static final String PRICING_POSTSELLQUANTITY = conf.getString("ifund.sellQuantity");
  public static final String FUND_PRICING_POSTBUYINGBATCH = conf.getString("ifund.buyingBatch");
  public static final String FUND_PRICING_POSTSELLINGBATCH = conf.getString("ifund.sellingBatch");
  public static final String FUND_PRICING_POSTAVERAGENAV = conf.getString("ifund.averageNav");
  public static final String FUND_PRICING_POSTCASHBACK = conf.getString("ifund.cashbackChangeProduct");
  public static final String FUND_PRICING_POSTFEESELL = conf.getString("ifund.feeSell");
  public static final String FUND_PRICING_RMPOSTFEESELL = conf.getString("ifund.RMfeeSell");
  public static final String FUND_PRICING_GET_CASHBACKVIP = conf.getString("ifund.getCashBackVip");
  public static final String FUNDTRADING_DOMAIN = conf.getString(getFundTradingDomain());
  public static final String FUNDTRADING_TC3POSTORDERS = conf.getString("ifund.tc3CreateOrders");
  public static final String FUNDTRADING_TC3POSTMULTIPLEORDERS = conf.getString("ifund.tc3CreateMultipleOrders");
  public static final String FUNDTRADING_TC3RMPOSTORDERS = conf.getString("ifund.tc3RMCreateOrders");
  public static final String FUNDTRADING_TV3POSTORDERS = conf.getString("ifund.v3CreateOrders");
  public static final String FUNDTRADING_TC3PUTCHANGEPRODUCT = conf.getString("ifund.tc3ChangeProduct");
  public static final String FUNDTRADING_TC3RMPUTCHANGEPRODUCT = conf.getString("ifund.tc3RMChangeProduct");
  public static final String FUNDTRADING_TC3POSTBALANCESACCOUNT = conf.getString("ifund.tc3BalancesAccount");
  public static final String FUNDTRADING_TC3RMPOSTBALANCESACCOUNT = conf.getString("ifund.tc3RMBalancesAccount");
  public static final String FUNDTRADING_TC3GETBALANCESBASEINFO = conf.getString("ifund.tc3BalancesBaseInfo");
  public static final String FUNDTRADING_TC3RMGETBALANCESBASEINFO = conf.getString("ifund.tc3RMBalancesBaseInfo");
  public static final String FUNDTRADING_TC3MATCHEDDATE = conf.getString("ifund.tc3MatchedDate");
  public static final String FUNDTRADING_TC3GET_BALANCES = conf.getString("ifund.tc3Balances");
  public static final String FUNDTRADING_TC3GET_SIPCONFIG = conf.getString("ifund.tc3SipConfig");
  public static final String FUNDTRADING_TC3PUTCONFIRMORDER = conf.getString("ifund.tc3ConfirmOrder");
  public static final String FUNDTRADING_TC3GETINTERNALUSER = conf.getString("ifund.tc3InternalUser");
  public static final String FUNDTRADING_TC3GET_BALANCESACCOUNTDETAIL = conf.getString("ifund.tc3BalancesAccountDetail");
  public static final String FUNDTRADING_V3GET_BALANCESACCOUNTDETAIL = conf.getString("ifund.v3BalancesAccountDetail");
  public static final String FUNDTRADING_TC3RMGET_BALANCESACCOUNTDETAIL = conf.getString("ifund.tc3RMBalancesAccountDetail");
  public static final String FUNDTRADING_V3GET_PREVIOUSTRADINGDATE = conf.getString("ifund.v3PreviousTradingDate");
  public static final String FUNDTRADING_V3POST_ORDERSSUBMIT = conf.getString("ifund.v3OrderSubmit");
  public static final String FUNDTRADING_TC3_GETHOLIDAY = conf.getString("ifund.tc3GetHoliday");
  public static final String FUNDTRADING_TC3_GETORIGINALSIP = conf.getString("ifund.tc3OriginalSip");
  /*=======================================================================================*/
  public static final String FUNDTRADING_IPLAN_GETBALANCES = conf.getString("ifund.iPlanBalances");
  public static final String FUNDTRADING_JOB_PROCESSDRAFT = conf.getString("ifund.jobProcessDraft");
  public static final String FUNDTRADING_TRADING_HOLDINGTIME = conf.getString("ifund.tradingHoldingTime");
  /* Management Service */
  public static final String MANAGEMENT_DOMAIN = conf.getString("ifund.management-service");
  public static final String FUND_MANAGEMENT_GETAGREEMENTS = conf.getString("ifund.getAgreements");

  private static String getPricingDomain() {
    if (SERVICE_TEST.equals(TestType.ORCL.name())) {
      return "ifund.pricing-service-orcl";
    } else if (SERVICE_TEST.equals(TestType.DOCKER.name())) {
      return "ifund.pricing-service-docker";
    } else {
      return "ifund.pricing-service";
    }
  }

  /* iFund Trading Service */
  private static String getFundTradingDomain() {
    if (SERVICE_TEST.equals(TestType.ORCL.name())) {
      return "ifund.ifund-trading-orcl";
    } else if (SERVICE_TEST.equals(TestType.DOCKER.name())) {
      return "ifund.ifund-trading-docker";
    } else {
      return "ifund.ifund-trading";
    }
  }
}
