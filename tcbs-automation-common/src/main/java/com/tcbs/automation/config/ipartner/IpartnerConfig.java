package com.tcbs.automation.config.ipartner;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IpartnerConfig {
  public static final Double TAX_THRESHOLD_AMOUNT = 0D;
  public static final Double COEFFICIENT_AFTER_TAX = 0.9;
  private static final Config conf = new ConfigImpl("ipartner").getConf();
  public static final String TCI3ACCOUNTPRO_REFERRALS_JOB_UPDATE_STATUS_API = conf.getString("ipartner.referrals-job-update-status-url");
  public static final String PARTNER_GET_SEARCH_CUSTOMER_OF_IWP = conf.getString("ipartner.get-search-customer-of-iwp-url");
  public static final String PARTNER_POST_TCBS_EMPLOYEE_API = conf.getString("ipartner.post-tcbs-employee-url");
  public static final String PARTNER_GET_TCBS_EMPLOYEE_API = conf.getString("ipartner.get-tcbs-employee-url");
  public static final String PARTNER_PUT_RELATION_CANCEL_API = conf.getString("ipartner.put-relation-cancel-url");
  public static final String SEARCH_REFERRAL_API = conf.getString("ipartner.search-referral-url");
  public static final String IBOND_TRADING_API = conf.getString("ipartner.ibond-trading-url ");
  public static final String GET_RM_API = conf.getString("ipartner.get-search-customer-of-iwp-url");
  public static final String PARTNER_CONFIRM_REGISTER_IWP_API = conf.getString("ipartner.post-confirm-register-iwp-url");
  public static final String IMPORT_IWP_API = conf.getString("ipartner.import-iwp-back-url");
  public static final String TCI3ACCOUNTPRO_CHECK_AND_STORE_PERMISSION_API = conf.getString("ipartner.referrals-check-and-store-permission-url");
  public static final String FLEX_SERVICE = conf.getString("flexService");
  public static final String TCI3ACCOUNTPRO_REFERRALS_GET_STOCK_FIRST_ORDER_API = conf.getString("ipartner.referrals-get-stock-first-order-url");
  public static final String TCI3ACCOUNTPRO_REFERRALS_GET_STOCK_REMAIN_INCENTIVE_API = conf.getString("ipartner.referrals-get-stock-remain-incentive-url");
  public static final String CHANGE_AFTYPE_API = conf.getString("ipartner.change-aftype");
  public static final String BACK_SEQUENCE_JOB_API = conf.getString("ipartner.post-back-sequence-job-url");
  public static final String BACK_SEQUENCE_HISTORY_JOB_API = conf.getString("ipartner.post-back-sequence-history-job-url");
  public static final String BACK_AFTYPE_ACTION_HISTORY_JOB_API = conf.getString("ipartner.post-back-aftype-action-history-job-url");
  public static final String TCI3ACCOUNTPRO_INPUT_REFERRAL_CODE_API = conf.getString("ipartner.referrals-input-referral-code-url");
  public static final String ICONNECT_GET_REFERRAL_INFO_BY_REFERRAL_CODE = conf.getString("ipartner.iconnect-get-referral-info-by-referral-code");
  public static final String ICONNECT_NEW_REFERRAL_BOOKING = conf.getString("ipartner.iconnect-new-referral-booking");
  public static final String ICONNECT_NEW_REFERRAL_MATCHING = conf.getString("ipartner.iconnect-new-referral-matching");
  public static final String ICONNECT_NEW_REFERRAL_CANCEL = conf.getString("ipartner.iconnect-new-referral-cancel");
  public static final String ICONNECT_NEW_REFERRAL_EXPIRE = conf.getString("ipartner.iconnect-new-referral-expire");
  public static final String TCI3ACCOUNTPRO_REFERRALS_GET_REFERRAL_LINK_API = conf.getString("ipartner.referrals-get-referral-link-url");
  public static final String IXU_ODIN_GET_REFERRAL_TRANSACTION_API = conf.getString("ipartner.ixu-get-referral-transaction-url");
  //Report
  public static final String REFERRAL_TOP_STOCK_API = conf.getString("ipartner.referral-top-stock-url");
  public static final String REFERRAL_TOP_BOND_API = conf.getString("ipartner.referral-top-bond-url");
  public static final String REFERRAL_TOP_FUND_API = conf.getString("ipartner.referral-top-fund-url");
  public static final String REFERRAL_HISTORY_STOCK_API = conf.getString("ipartner.referral-history-stock-url");
  public static final String REFERRAL_HISTORY_BOND_API = conf.getString("ipartner.referral-history-bond-url");
  public static final String REFERRAL_HISTORY_FUND_API = conf.getString("ipartner.referral-history-fund-url");
  public static final String REFERRAL_ACHIEVEMENT_API = conf.getString("ipartner.referral-achievement-url");
  public static final String REFERRAL_TOP_XU_API = conf.getString("ipartner.referral-top-xu-url");
  public static final String REFERRAL_ACHIEVEMENT_XU_API = conf.getString("ipartner.referral-achievement-xu-url");
  //Exchange Xu
  public static final String REFERRAL_EXCHANGE_XU_RETRY_API = conf.getString("ipartner.referral-exchange-xu-retry-url");

  public static final String TCI3ACCOUNTPRO_NET_INCENTIVE_TO_IXU_IAG_486_API = conf.getString("ipartner.net-incentive-to-ixu-iag486-url");
}
