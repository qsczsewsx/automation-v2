package com.tcbs.automation.config.idatahfc;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IDataHfcConfig {
  private static final Config conf = new ConfigImpl("idatahfc").getConf();

  // x-api-key
  public static final String TCBS_HFC_X_API_KEY = conf.getString("hfc.x-api-key");

  // fengshui
  public static final String TCBS_HFC_DOMAIN_PUBLIC_URL = conf.getString("hfc.domain-public");
  public static final String TCBS_HFC_DOMAIN_EXT_URL = conf.getString("hfc.domain-ext");
  public static final String TCBS_HFC_DOMAIN_INT_URL = conf.getString("hfc.domain-int");

  public static final String IDATA_FENGSHUI_TICKER_DESTINY_URL = conf.getString("hfc.fengshui.getTickerDestinyUrl");
  public static final String IDATA_FENGSHUI_LUNAR_CALENDAR_URL = conf.getString("hfc.fengshui.getLunarCalendarUrl");
  public static final String IDATA_FENGSHUI_COMMENTARY_URL = conf.getString("hfc.fengshui.getCommentaryUrl");
  public static final String IDATA_FENGSHUI_MING_URL = conf.getString("hfc.fengshui.getMingUrl");
  public static final String IDATA_FENGSHUI_USER_DESTINY_URL = conf.getString("hfc.fengshui.getUserDestinyUrl");
  public static final String IDATA_FENGSHUI_DATE_DESTINY_URL = conf.getString("hfc.fengshui.getDateDestinyUrl");
  public static final String IDATA_FENGSHUI_STATIC_URL = conf.getString("hfc.fengshui.getStaticUrl");

  // dividend
  public static final String HFC_GET_EX_DIVIDEND_URL = conf.getString("hfc.exdividend.getExDividendUrl");
  public static final String HFC_GET_EX_DIVIDEND_LEAN_URL = conf.getString("hfc.exdividend.getExDividendLeanUrl");

  // icalendar
  public static final String HFC_GET_ICALENDAR = conf.getString("hfc.icalendar.icalendarUrl");
  public static final String HFC_GET_ICALENDAR_FROM_HOME = conf.getString("hfc.icalendar.icalendarHomeUrl");
  public static final String HFC_GET_BOND_PRO_EXPIRED_STATIC = conf.getString("hfc.icalendar.getBondProExpiredStaticUrl");
  public static final String HFC_GET_BOND_STATIC = conf.getString("hfc.icalendar.getBondStaticUrl");
  public static final String HFC_GET_ANNIVERSARY_EVENT = conf.getString("hfc.icalendar.getAnniversaryEventUrl");
  public static final String HFC_GET_RM_CUSTOMER = conf.getString("hfc.icalendar.getRmCustomerUrl");
  public static final String HFC_GET_MY_ASSET_STOCK = conf.getString("hfc.icalendar.getMyAssetStock");
  public static final String HFC_GET_WL_STOCK = conf.getString("hfc.icalendar.getWLStock");
  public static final String HFC_GET_MY_ASSET_CW = conf.getString("hfc.icalendar.getMySetCW");
  public static final String HFC_GET_OTHER_EVENT = conf.getString("hfc.icalendar.getOtherEvent");

  // orion
  public static final String HFC_GET_CUS_CUSTODYCODE_URL = conf.getString("hfc.orion.getCusCustomerUrl");


  // nav
  public static final String HFC_GET_NAV_BY_DATE = conf.getString("hfc.nav.getNavByDateUrl");

  // annual sigma
  public static final String HFC_GET_ANNUAL_SIGMA_URL = conf.getString("hfc.orion.getAnnualSigmaUrl");

  // cw
  public static final String HFC_GET_CW_INFO_URL = conf.getString("hfc.cw.getCwInfoUrl");

  public static final String HFC_GET_CW_STATE_CHART = conf.getString("hfc.cw.getStateChartUrl");
  public static final String HFC_GET_CW_BACK_TESTING_CHART = conf.getString("hfc.cw.getBackTestingChartUrl");
  public static final String HFC_GET_CW_LIST = conf.getString("hfc.cw.getCwUrl");
  public static final String HFC_GET_CW_CONTENT = conf.getString("hfc.cw.getCwContentUrl");

  // psycho
  public static final String HFC_GET_BUY_SELL_RATIO = conf.getString("hfc.psycho.getBuySellRatioUrl");
  public static final String HFC_GET_BUY_SELL_RATIO_OVER_TIME = conf.getString("hfc.psycho.getBuySellRatioOverTimeUrl");
  public static final String HFC_GET_BUY_SELL_RATIO_TOP_TRADING = conf.getString("hfc.psycho.getBuySellRatioOverTopTradingUrl");

  //wb
  public static final String HFC_GET_WHOLE_BANK_ALL_REF_LIST = conf.getString("hfc.wb.refList");
  public static final String HFC_GET_WHOLE_BANK_URL = conf.getString("hfc.wb.getWB");

  //upload
  public static final String HFC_UPLOAD_PICTURE_URL = conf.getString("hfc.upload.uploadUrl");
  public static final String HFC_GET_AWS_UPLOAD_PICTURE_URL = conf.getString("hfc.awsUploadUrl");

  //risk appetite
  public static final String HFC_GET_RISK_APPETITE_URL = conf.getString("hfc.riskappetite.riskAppetiteUrl");

  //Ms Vip
  public static final String HFC_GET_LIST_CUSTOMER_MS_VIP_URL = conf.getString("hfc.msVip.getListCustomer");
  public static final String MS_VIP_X_API_KEY = conf.getString("hfc.msVip.x-api-key");
  public static final String HFC_GET_ACCOUNT_STATEMENT_INFO_MS_VIP_URL = conf.getString("hfc.msVip.getAccountStatementInfo");
  public static final String HFC_GET_STOCK_BALANCE_INFO_MS_VIP_URL = conf.getString("hfc.msVip.getStockBalanceInfo");
  public static final String HFC_GET_DEBIT_MARGIN_INFO_MS_VIP_URL = conf.getString("hfc.msVip.getDebitMarginInfo");
  public static final String HFC_GET_LOAN_PAYMENT_INFO_MS_VIP_URL = conf.getString("hfc.msVip.getLoanPaymentInfo");
  public static final String HFC_GET_DEPOSITORY_FEE_INFO_MS_VIP_URL = conf.getString("hfc.msVip.getDepositoryFeeInfo");
  public static final String HFC_GET_BLOCK_AGE_INFO_MS_VIP_URL = conf.getString("hfc.msVip.getBlockAgeInfo");

  // MaxS
  public static final String HFC_MAXS_TXN_DETAIL_LEG_URL = conf.getString("hfc.maxs.getTxnDetailLegUrl");
  public static final String HFC_MAXS_EOP_RETAIL_URL = conf.getString("hfc.maxs.eopRetailUrl");
  public static final String HFC_MAXS_EOP_FI_URL = conf.getString("hfc.maxs.eopFiUrl");

  // Stock
  public static final String HFC_STOCK_GET_ALL_SECURITIES_URL = conf.getString("hfc.stock.getAllSecuritiesUrl");

  //order history
  public static final String HFC_GET_ORDER_HISTORIES_URL = conf.getString("hfc.iangel.orderHisUrl");
  public static final String HFC_GET_ORDER_HISTORIES_SE_RETAILS = conf.getString("hfc.iangel.seRetailUrl");
  public static final String HFC_GET_TIP_MONEY = conf.getString("hfc.iangel.tipMoneyIsaveUrl");
  public static final String HFC_GET_INTEREST_MONEY = conf.getString("hfc.iangel.intesrestMoneyIsaveUrl");
  public static final String HFC_GET_INTEREST_MONTHLY_MONEY = conf.getString("hfc.iangel.intesrestMonthMoneyIsaveUrl");
  public static final String HFC_GET_AUM_CUS_URL = conf.getString("hfc.iangel.getAumCustomerUrl");

  //crm
  public static final String HFC_POST_AUTO_NOTI_CRM_URL = conf.getString("hfc.crm.notiCrmUrl");
  public static final String HFC_GET_TOKEN_CRM_URL = conf.getString("hfc.crm.authCRMUrl");
  public static final String HFC_GET_TEMPLATE_CRM_URL = conf.getString("hfc.crm.templateCRMUrl");
  public static final String HFC_SEND_MAIL_CRM_URL = conf.getString("hfc.crm.sendMailCRMUrl");
  public static final String HFC_SEND_MAIL_X_API_KEY = conf.getString("hfc.crm.x-api-key");
  //fin5
  public static final String HFC_GET_ACC_BALANCE_URL = conf.getString("hfc.fin5.url");
  public static final String HFC_GET_ACC_BALANCE_X_API_KEY = conf.getString("hfc.fin5.x-api-key");
  public static final String HFC_GET_FUND_VOL_URL = conf.getString("hfc.fin5.fundVolUrl");
  public static final String HFC_GET_UTTB_VAL_URL = conf.getString("hfc.fin5.uttbValUrl");
  public static final String HFC_GET_ACT_TRANS_URL = conf.getString("hfc.fin5.actTransUrl");
  public static final String HFC_GET_PRICE_COGS_BOND_URL = conf.getString("hfc.fin5.mktsCogsUrl");
  public static final String HFC_GET_MARKETS_COF_URL = conf.getString("hfc.fin5.mktsCofUrl");
  public static final String HFC_GET_ACT_DAILY_TRANS_URL = conf.getString("hfc.fin5.getActDailyTransUrl");
  public static final String HFC_GET_ACCRUED_COUPON_BOND_URL = conf.getString("hfc.fin5.getAccruedCouponBondUrl");
  public static final String HFC_GET_FEE_DAILY_ICOPY = conf.getString("hfc.fin5.getFeeDailyIcopy");

  //cdbl
  public static final String HFC_GET_CDBL_LAST_TRADE_DATE_URL = conf.getString("hfc.cdbl.getLastTradeDateUrl");
  //rm rbo
  public static final String HFC_GET_LIST_RM_RBO_URL = conf.getString("hfc.rmrbo.getListRmRboUrl");

  //amlock
  public static final String HFC_GET_AMLOCK_INFO_URL = conf.getString("hfc.amlock.getAmLockInfoUrl");

  //ixu
  public static final String HFC_GET_BOND_PRO_EXPIRED_URL = conf.getString("hfc.ixu.getBondProExpired");
  public static final String HFC_GET_IXU_FOR_CAMP_URL = conf.getString("hfc.ixu.getIxuForCampUrl");
  //bond
  public static final String HFC_GET_BOND_COUPON_FUTURE_EVENT_URL = conf.getString("hfc.bond.getBondCouponFutureEventUrl");
  public static final String HFC_GET_BOND_COUPON_RECEIVED_URL = conf.getString("hfc.bond.getBondCouponReceivedUrl");

  //focus
  public static final String HFC_GET_COUPON_OTC_URL = conf.getString("hfc.focus.getDataCouponOtcUrl");

  //notification make timeline in queue
  public static final String MAKE_TIMELINE_IN_QUEUE_URL = conf.getString("hfc.icalendar.makeTimelineInQueue");
  public static final String PUSH_NOTIFICATION_URL = conf.getString("hfc.inbox.notificationUrl");
  public static final String X_API_KEY_INBOX = conf.getString("hfc.inbox.x-api-key");
  public static final String RETRY_MAKE_TIMELINE_URL = conf.getString("hfc.icalendar.retryMakeTimeLine");

  //iSail
  public static final String GET_LIST_CUSTOMER_ISAIL = conf.getString("hfc.iSail.getListCustomerISail");

  //iCopy
  public static final String GET_SIMILAR_TRADER_URL = conf.getString("hfc.iCopy.getSimilarTraderUrl");

  //MonthlyStatement
  public static final String GET_MONTHLY_STATEMENT_URL = conf.getString("hfc.monthlyStatement.monthlyStatementUrl");
  //

  //Trader_recommend_forcopier
  public static final String GET_TRADER_RECOMMEND_URL = conf.getString("hfc.iCopy.traderRecommendUrl");
  //
  //risk profile
  public static final String GET_RISK_PROFILE_URL = conf.getString("hfc.riskProfile.riskProfileUrl");

  //Fast mobile
  public static final String GET_TCBF_DATA_URL = conf.getString("hfc.fastMobile.tcbfDataUrl");

  //wso2 services
  public static final String GET_LIQUIDITY_ISAVE_URL = conf.getString("hfc.wso2Server.liquidity.liquidityIsaveUrl");
  public static final String GET_LIQUIDITY_LIMIT_URL = conf.getString("hfc.wso2Server.liquidity.liquidityLoanLimitUrl");
  public static final String GET_LIQUIDITY_DETAIL_URL = conf.getString("hfc.wso2Server.liquidity.liquidityLoanDetailUrl");
  public static final String GET_LIQUIDITY_PAYMENT_URL = conf.getString("hfc.wso2Server.liquidity.liquidityLoanPaymentUrl");
  public static final String GET_LIQUIDITY_BOND_PRO_URL = conf.getString("hfc.wso2Server.liquidity.liquidityBondProUrl");
  public static final String GET_LIQUIDITY_BOND_WARE_HOUSE_URL = conf.getString("hfc.wso2Server.liquidity.liquidityBondWareHouseUrl");
  public static final String GET_LIQUIDITY_BOND_OUT_STANDING_URL = conf.getString("hfc.wso2Server.liquidity.liquidityBondOutstandingUrl");
  public static final String GET_LIQUIDITY_BOND_TRADING_URL = conf.getString("hfc.wso2Server.liquidity.liquidityBondTradingUrl");
  public static final String GET_LIQUIDITY_BOND_COUPON_URL = conf.getString("hfc.wso2Server.liquidity.liquidityBondCouponUrl");
}
