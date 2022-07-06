package com.tcbs.automation.config.idata;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IDataConfig {
  // TCBS-DWH-V2
  public static final String PAGING_DATA = "pagingData";
  private static final Config conf = new ConfigImpl("idata").getConf();
  // iData - iCalendar
  public static final String ICALENDAR_TIMELINEBATCH_V1 = conf.getString("idata.icalendar.timelineBatchV1");

  // =========================================================== //
  public static final String ICALENDAR_TIMELINEBATCH_JOB_LAUNCHER_URL = conf
    .getString("idata.icalendar.timelineBatch.jobLauncher");
  // TCBS-DWH
  public static final String TCBS_DWH_DOMAIN = conf.getString("idata.tcbsDwh.domain");
  public static final String DWH_X_API_KEY = conf.getString("idata.tcbsDwh.x-api-key");
  public static final String DWH_GET_BOND_PRO_EXPIRED_STATIC = conf.getString("idata.tcbsDwh.iCalendarUrl.getBondProExpiredStatic");
  public static final String DWH_ICALENDAR_PROC_X_API_KEY = conf.getString("idata.tcbsDwh.iCalendarUrl.x-api-key");
  public static final String DWH_GET_BOND_STATIC = conf.getString("idata.tcbsDwh.iCalendarUrl.getBondStatic");
  public static final String DWH_PERFORMANCE_X_API_KEY = conf.getString("idata.tcbsDwh.performance.x-api-key");
  public static final String DWH_GET_NAV_URL = conf.getString("idata.tcbsDwh.fund.navUrl");
  public static final String DWH_GET_NAV_X_API_KEY = conf.getString("idata.tcbsDwh.fund.x-api-key");
  public static final String DWH_GET_CONTRACT_EXPIRED_URL = conf.getString("idata.tcbsDwh.performance.getContractExpiredUrl");
  public static final String DWH_GET_CONTRACT_MATURED_URL = conf.getString("idata.tcbsDwh.performance.getContractMaturedUrl");
  public static final String DWH_GET_COUPON_PAYMENT_URL = conf.getString("idata.tcbsDwh.performance.getCouponPaymentUrl");
  public static final String DWH_GET_CASH_FLOW_IN_FUTURE_URL = conf.getString("idata.tcbsDwh.performance.getCashFlowUrl");
  public static final String DWH_GET_VIP_URL = conf.getString("idata.tcbsDwh.owner-data.getVipUrl");
  public static final String DWH_GET_VIP_HIS_URL = conf.getString("idata.tcbsDwh.owner-data.getVipHisUrl");
  public static final String DWH_GET_VIP_X_API_KEY = conf.getString("idata.tcbsDwh.owner-data.x-api-key");
  public static final String DWH_GET_PLAN_EVENT = conf.getString("idata.tcbsDwh.iCalPlanEventUrl.getPlanEvent");
  public static final String DWH_GET_PLAN_EVENT_DOMAIN = conf.getString("idata.tcbsDwh.iCalPlanEventUrl.dwhDomain");
  public static final String DWH_ICAL_PLAN_EVENT_X_API_KEY = conf.getString("idata.tcbsDwh.iCalPlanEventUrl.x-api-key");
  public static final String DWH_GET_CUS_OF_RM_URL = conf.getString("idata.tcbsDwh.customer-rm.getCusOfRm");
  public static final String DWH_CUS_OF_RM_X_CLIENT_KEY = conf.getString("idata.tcbsDwh.customer-rm.x-client-key");
  public static final String DWH_GET_TOTAL_TRANS_VALUE_URL = conf.getString("idata.tcbsDwh.iXu.getTotalTransValueUrl");
  public static final String TCBS_DWH_DOMAIN_V2 = conf.getString("idata.tcbsDwhV2.domain");
  public static final String DWH_GET_STOX_EVENTS = conf.getString("idata.tcbsDwhV2.apiUrl.getStoxEvents.url");
  public static final String DWH_GET_STOX_EVENTS_BY_EXRIGHTDATE = conf.getString("idata.tcbsDwhV2.apiUrl.getStoxEvents.byExRightDateUrl");
  public static final String DWH_FETCH_VIPACCOUNT_BYCASE = conf.getString("idata.tcbsDwhV2.apiUrl.fetchVipAccountRealtime.byCaseUrl");
  public static final String DWH_FETCH_VIPACCOUNT_BYMANY = conf.getString("idata.tcbsDwhV2.apiUrl.fetchVipAccountRealtime.byManyUrl");
  public static final String DWH_GET_VIP_ACCOUNT = conf.getString("idata.tcbsDwhV2.apiUrl.fetchVipAccountRealtime.getVipBankFromMock");
  public static final String DWH_GET_VIPEXT_ACCOUNT = conf.getString("idata.tcbsDwhV2.apiUrl.fetchVipAccountRealtime.getVipExtBankFromMock");
  public static final String DWH_X_CLIENT_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.fetchVipAccountRealtime.x-client-key");
  public static final String DWH_GET_VIP_BANK_URL = conf.getString("idata.tcbsDwhV2.apiUrl.getVipBank.url");
  public static final String DWH_GET_VIP_BANK_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.getVipBank.x-api-key");
  public static final String DWH_GET_TOP_RM_URL = conf.getString("idata.tcbsIData.apiUrl.getTopRM");
  public static final String DWH_GET_TOP_BOND_URL = conf.getString("idata.tcbsIData.apiUrl.getTopBond");
  public static final String DWH_GET_IWP_GROWTH_IN_MONTH_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iWPGrowthInMonth.getiWPGrowthInMonthUrl");
  public static final String DWH_GET_IWP_GROWTH_IN_MONTH_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.iWPGrowthInMonth.x-api-key");
  public static final String DWH_GET_TRANSACTION_AMOUNT_URL = conf.getString("idata.tcbsDwhV2.apiUrl.transactionAmount.getTransactionAmountUrl");
  public static final String DWH_GET_TRANSACTION_AMOUNT_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.transactionAmount.x-api-key");
  // DWH for 3rd party
  public static final String DWH_3_PARTY_GET_BOND_CODE_URL = conf.getString("idata.tcbsDwh.thirdParty.getBondCouponUrl");
  public static final String DWH_3_PARTY_GET_BOND_CODE_X_CLIENT_KEY = conf.getString("idata.tcbsDwh.thirdParty.getBondCoupon-x-client-key");
  public static final String DWH_3_PARTY_GET_BOND_CODE_X_API_KEY = conf.getString("idata.thirdParty.x-api-key");

  // DWH for iXu
  public static final String DWH_GET_MARGIN_BY_DATE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getMarginUrl");
  public static final String DWH_GET_DATA_IXU_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.x-api-key");
  public static final String DWH_GET_ZERO_BALANCE_STATUS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.zeroBalanceStatusUrl");
  public static final String DWH_GET_CUS_STOCK_TXN_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getCusStockTxnUrl");
  public static final String DWH_GET_PORTFOLIO_CASH_BALANCE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getPortfolioCashBalanceUrl");
  public static final String DWH_GET_PORTFOLIO_FUND_BALANCE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getPortfolioFundBalanceUrl");
  public static final String DWH_GET_CUS_SELL_ODD_LOT_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getCusSellOddLot");
  public static final String DWH_GET_ODD_LOT_TRANS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getOddLotTrans");
  public static final String DWH_GET_TCBS_EKYC_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getTcbsEkyc");
  public static final String DWH_GET_MAX_CASA_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getMaxCasa");
  public static final String DWH_GET_EOP_CASA_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getEopCasa");
  public static final String DWH_GET_CASA_AT_DATE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getCasaAtDate");
  public static final String DWH_GET_AVG_CASA_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iXu.getAvgCasa");
  // DWH for Fund
  public static final String DWH_GET_ALL_CUS_HOLD_ASSET_URL = conf.getString("idata.tcbsDwhV2.apiUrl.fund.getAllCusHoldAssetUrl");
  public static final String DWH_GET_DP_CUSTOMER_IN_DATE_EVENTS = conf.getString("idata.tcbsDwhV2.apiUrl.fund.getDPCusInDateUrl");
  public static final String DWH_GET_MONTHLY_STATE = conf.getString("idata.tcbsDwh.fund.monthlyStateUrl");
  public static final String DWH_FUND_IBOND_BUY_BACK_URL = conf.getString("idata.tcbsDwhV2.apiUrl.fund.iBondBuyBackUrl");

  public static final String DWH_FUND_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.fund.x-api-key");
  // DWH for Focus
  public static final String DWH_GET_PRO_INVESTOR_ELIGIBLE_DATE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.proInvestorEligibleDateUrl");
  public static final String DWH_GET_PRO_INVESTOR_CROSS_THRESHOLD_DATE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.proInvestorCrossThresholdDateUrl");
  public static final String DWH_GET_PRO_INVESTOR_STATUS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.proInvestorStatusUrl");
  public static final String DWH_GET_PRO_INVESTOR_MBOND_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.proInvestorMBondUrl");
  public static final String DWH_FOCUS_X_CLIENT_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.focus.x-client-key");
  public static final String DWH_GET_STOCK_TXN_MICRO_INVESTING_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.getStockTxnMicroInvestingUrl");
  public static final String DWH_GET_ALL_CUS_CONNECT_IA_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.getAllCusConnectIaUrl");
  public static final String DWH_GET_SPECIFIC_CUS_CONNECT_IA_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.getSpecificCusConnectIaUrl");
  public static final String DWH_CREATE_CUS_CONNECT_IA_URL = conf.getString("idata.tcbsDwhV2.apiUrl.focus.createCusConnectIaUrl");
  // DWH for starwar
  public static final String DWH_GET_CUSTOMER_WEALTH_URL = conf.getString("idata.tcbsDwhV2.apiUrl.starwar.getCusWealthUrl");
  public static final String DWH_STAR_WAR_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.starwar.x-api-key");
  // DWH for Orion
  public static final String DWH_GET_EX_DIVIDEND_SYMBOL_URL = conf.getString("idata.tcbsDwhV2.apiUrl.orion.getExDividendSymbolUrl");
  public static final String DWH_GET_EX_DIVIDEND_SYMBOL_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.orion.x-api-key");

  public static final String DWH_GET_BOND_PRICE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.orion.getBondPriceUrl");
  public static final String DWH_GET_BOND_PRICE_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.orion.bondPrice-x-api-key");
  // DWH for TD
  public static final String DWH_GET_CUS_DEPOSIT_FROM_BANK_URL = conf.getString("idata.tcbsDwhV2.apiUrl.termDeposit.getCusDepositUrl");
  public static final String DWH_GET_CUS_DEPOSIT_FROM_BANK_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.termDeposit.x-api-key");
  // DWH for tcAnalysis
  public static final String DWH_TCANALYSIS_X_CLIENT_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.tcAnalysis.x-api-key");
  public static final String DWH_TCANALYSIS_STOCK_RECOMMEND_URL = conf.getString("idata.tcbsDwhV2.apiUrl.tcAnalysis.stockRecommendUrl");
  public static final String DWH_TCANALYSIS_STOCK_SAME_INDUSTRY_URL = conf.getString("idata.tcbsDwhV2.apiUrl.tcAnalysis.stockSameIndustryUrl");

  //DWH for wholesale banking
  public static final String DWH_GET_WHOLESALE_BANKING_URL = conf.getString("idata.tcbsDwhV2.apiUrl.wb.getWBUrl");
  public static final String DWH_GET_WHOLESALE_BANKING_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.wb.x-api-key");

  //DWH for ROBO
  public static final String DWH_GET_SUGGEST_RATE_ROBO_URL = conf.getString("idata.tcbsDwhV2.apiUrl.robo.getSuggestUrl");
  public static final String DWH_GET_BUY_BACK_ICN_ROBO_URL = conf.getString("idata.tcbsDwhV2.apiUrl.robo.getBuyBackIcnUrl");
  public static final String DWH_GET_ROBO_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.robo.x-api-key");

  // Mountebank
  public static final String DWH_MOUNTEBANK_DOMAIN = conf.getString("idata.mountebank.domain");
  public static final String DWH_MOUNTEBANK_TERM_DEPOSIT_URL = conf.getString("idata.mountebank.termDepositUrl");
  public static final String DWH_MOUNTEBANK_GET_VIP_BANK_URL = conf.getString("idata.mountebank.vipBankUrl");

  // Staging
  public static final String STAGING_GET_BIRTHDAY = conf.getString("idata.tcbsDwh.stagingUrl.cusBirthday");
  public static final String STAGING_X_API_KEY = conf.getString("idata.tcbsDwh.stagingUrl.x-api-key");
  // TCBS-iData
  public static final String TCBS_IDATA_DOMAIN = conf.getString("idata.tcbsIData.domain");
  public static final String TCBS_IDATA_PUBLIC_DOMAIN = conf.getString("idata.tcbsIData.publicDomain");
  public static final String IDATA_GET_STOCK_RELATED = conf.getString("idata.tcbsIData.apiUrl.getStockRelated");
  public static final String IDATA_GET_STOCK_HOLD_COUNT = conf.getString("idata.tcbsIData.apiUrl.getStockHoldCount");
  public static final String IDATA_GET_STOCK_CORRELATION = conf.getString("idata.tcbsIData.apiUrl.getStockCorrelation");
  public static final String IDATA_GET_PORTFOLIO_PERFORMANCE = conf.getString("idata.tcbsIData.apiUrl.getPortfolioPerformance");
  public static final String IDATA_GET_COUPON_PAYMENT_URL = conf.getString("idata.tcbsIData.apiUrl.getCouponPayment");
  public static final String IDATA_GET_CONTRACT_EXPIRED_URL = conf.getString("idata.tcbsIData.apiUrl.getContractExpired");
  public static final String IDATA_GET_CONTRACT_MATURED_URL = conf.getString("idata.tcbsIData.apiUrl.getContractMatured");
  public static final String IDATA_GET_CASH_FLOW_URL = conf.getString("idata.tcbsIData.apiUrl.getCashFlow");
  public static final String IDATA_STOCK_RECOMMEND_URL = conf.getString("idata.tcbsIData.apiUrl.getStockRecommend");
  public static final String IDATA_STOCK_SAME_INDUSTRY_URL = conf.getString("idata.tcbsIData.apiUrl.getStockSameIndustry");
  public static final String IDATA_ICALENDAR_GET_PLAN_EVENT = conf.getString("idata.tcbsIData.apiUrl.getPlanEvent");

  public static final String IDATA_ISQUARE_GET_USER_EVENT = conf.getString("idata.tcbsIData.apiUrl.getUserEvent");
  public static final String IDATA_ISQUARE_CREATE_USER_EVENT = conf.getString("idata.tcbsIData.apiUrl.createUserEvent");
  public static final String IDATA_ISQUARE_UPDATE_USER_EVENT = conf.getString("idata.tcbsIData.apiUrl.updateUserEvent");
  public static final String IDATA_ISQUARE_DELETE_USER_EVENT = conf.getString("idata.tcbsIData.apiUrl.deleteUserEvent");
  public static final String IDATA_ISQUARE_GET_COMPANY_EVENT = conf.getString("idata.tcbsIData.apiUrl.getCompanyEvent");

  // IBond
  public static final String IDATA_IBOND_GET_LIST_RBO_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iBond.getListRBOUrl");
  public static final String IDATA_IBOND_X_CLIENT_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.iBond.x-client-key");
  public static final String IDATA_IBOND_GET_RM_URL = conf.getString("idata.tcbsDwhV2.apiUrl.getRM.getRMUrl");
  public static final String IDATA_IBOND_GET_RM_X_CLIENT_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.getRM.x-client-key");

  // Venus getBondCOGSAcc
  public static final String IDATA_GET_BOND_COGS_ACC = conf.getString("idata.tcbsDwhV2.apiUrl.bondCOGSAcc.getBondCOGSAccUrl");
  public static final String IDATA_GET_BOND_COGS_ACC_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.bondCOGSAcc.x-api-key");

  //iWP
  public static final String IDATA_IWP_X_API_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.iWP.x-api-key");
  public static final String IDATA_IWP_GET_IBER_DIRECT_CUS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iWP.getIBerDirectCusUrl");
  public static final String IDATA_IWP_GET_DAILY_MARGIN_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iWP.getDailyMarginUrl");
  public static final String IDATA_IWP_GET_DEBT_PAY_URL = conf.getString("idata.tcbsDwhV2.apiUrl.iWP.getDebtPayUrl");

  //Business Finance
  public static final String IDATA_BUSINESS_FINANCE_GET_MARGIN_LENDING_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getMarginLendingUrl");
  public static final String IDATA_TCBS_DWH_V2_X_API_KEY = conf.getString("idata.tcbsDwhV2.x-api-key");
  public static final String IDATA_BUSINESS_FINANCE_X_API_CLIENT = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.x-client-key");
  public static final String IDATA_BUSINESS_FINANCE_GET_ACT_TRANS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getActTransUrl");
  public static final String IDATA_BUSINESS_FINANCE_GET_TD_TRANS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getTDTransUrl");
  public static final String IDATA_BUSINESS_FINANCE_GET_LC_TRANS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getLCTransUrl");
  public static final String IDATA_BUSINESS_FINANCE_GET_ACT_OPEX_TRANS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getActOPEXTransUrl");
  public static final String IDATA_BUSINESS_FINANCE_GET_ACT_FINANCE_REPORT_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getActFinanceReportUrl");
  public static final String IDATA_BUSINESS_FINANCE_GET_ACT_DAILY_TRANS_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getActDailyTransUrl");
  public static final String BUSINESS_FINANCE_GET_STOCK_AND_MARGIN_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getActualStockAndMarginIncentiveUrl");
  public static final String BUSINESS_FINANCE_GET_IXU_CASHBACK_COMMISSION_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getIxuCashbackAndCommission");
  public static final String BUSINESS_FINANCE_GET_ESTIMATED_STOCK_AND_MARGIN_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getEstimatedStockAndMarginIncentiveUrl");
  public static final String BUSINESS_FINANCE_GET_TOTAL_MARGIN_VALUE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getTotalMarginValueUrl");
  public static final String BUSINESS_FINANCE_GET_TOTAL_STOCK_VALUE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getTotalStockValueUrl");
  public static final String BUSINESS_FINANCE_GET_DAILY_ICOPY_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getDailyICopyUrl");
  public static final String BUSINESS_FINANCE_GET_DAILY_FEE_BY_EXCHANGE_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getDailyFeeExchangeUrl");
  public static final String BUSINESS_FINANCE_GET_BRV_CASA_PNL_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getBrvCasaPnlUrl");
  public static final String BUSINESS_FINANCE_GET_DAILY_APAR_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getDailyAparUrl");
  public static final String BUSINESS_FINANCE_GET_ASSET_LIABILITIES_URL = conf.getString("idata.tcbsDwhV2.apiUrl.businessFinance.getAssetLiabilitiesEquityUrl");

  // =========================================================== //

  // INBOX
  public static final String INBOX_DOMAIN = conf.getString("idata.inbox.domain");
  public static final String INBOX_API_KEY = conf.getString("idata.inbox.api-key");

  public static final String BANK_DWH_AUTH_URL = conf.getString("idata.bankdwh_vip.authen.baseurl");
  public static final String BANK_DWH_AUTH_USER = conf.getString("idata.bankdwh_vip.authen.username");
  public static final String BANK_DWH_AUTH_PASS = conf.getString("idata.bankdwh_vip.authen.password");
  public static final String BANK_DWH_VIP_BANK = conf.getString("idata.bankdwh_vip.fetchVipBank");

  // GET TRAILINGVIP
  public static final String DWH_GET_TRAILING_VIP_X_CLIENT_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.trailingVip.x-client-key");
  public static final String DWH_GET_TRAILING_VIP_URL = conf.getString("idata.tcbsDwhV2.apiUrl.trailingVip.getTrailingVipUrl");
  // =============================================================== //

  //ETL EVENT IXU GET VIP
  public static final String DWH_ETL_EVENT_URL = conf.getString("idata.tcbsDwhV2.apiUrl.getVipBank.etlEvent");
  // GET Vip IWP
  public static final String DWH_GET_VIP_IWP_X_CLIENT_KEY = conf.getString("idata.tcbsDwhV2.apiUrl.getVipIwp.x-client-key");
  public static final String DWH_GET_VIP_IWP_URL = conf.getString("idata.tcbsDwhV2.apiUrl.getVipIwp.getVipIwpUrl");
  // =============================================================== //

  // DWH service - BAYMAX internal api
  public static final String DWH_GET_ORDERHISTORY_INTERNAL = conf.getString("idata.bayMaxInternal.orderHistoriesInternal");
  public static final String DWH_GET_RIGHTS_INTERNAL = conf.getString("idata.bayMaxInternal.rightsInternal");

}
