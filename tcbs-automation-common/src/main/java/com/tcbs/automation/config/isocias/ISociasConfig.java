package com.tcbs.automation.config.isocias;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ISociasConfig {

  private static final Config conf = new ConfigImpl("isocias").getConf();
  /**
   *  api internal isocias
   */
  public static final String ISOCIAS_TRANSFER = conf.getString("api.iSociasTransfer");
  public static final String ISOCIAS_INQUIRY = conf.getString("api.iSociasInquiry");
  public static final String ISOCIAS_BANKINFO_LIST = conf.getString("api.iSociasBankInfoList");
  public static final String ISOCIAS_INQUIRY_ACC_INFO = conf.getString("api.iSociasInquiryAccInfo");
  public static final String ISOCIAS_REPAYMENT_INQUIRY = conf.getString("api.iSociasRepaymentInquiry");
  public static final String ISOCIAS_REPAYMENT_REINQUIRY = conf.getString("api.iSociasRepaymentReInquiry");
  public static final String ISOCIAS_RECONCILE_TRANSACTION = conf.getString("api.iSociasReconcileTransaction");

  public static final String ISOCIAS_RECONCILE_PROCESS = conf.getString("api.iSociasReconcileProcess");
  public static final String ISOCIAS_RECONCILE_UPLOAD = conf.getString("api.iSociasReconcileUpload");
  public static final String X_API_KEY = conf.getString("x-api-key");

  // open API
  public static final String ISOCIAS_STOCK_ORDER_REQUEST = conf.getString("api.stockOrderRequest");
  public static final String ISOCIAS_STOCK_ORDER_DELETE = conf.getString("api.stockOrderDelete");
  public static final String ISOCIAS_STOCK_ORDER_UPDATE = conf.getString("api.stockOrderUpdate");
  public static final String ISOCIAS_STOCK_ORDER_GET = conf.getString("api.stockOrderGet");
  public static final String ISOCIAS_STOCK_ORDER_GET_HISTORY = conf.getString("api.stockOrderGetHistory");
  public static final String ISOCIAS_STOCK_GET_PPSE = conf.getString("api.stockGetPPSE");
  public static final String ISOCIAS_STOCK_GET_PPSE_CHANGE_ORDER = conf.getString("api.stockGetPPSEChangeOrder");
  public static final String ISOCIAS_STOCK_GET_SE = conf.getString("api.stockGetSE");
  public static final String ISOCIAS_PROFILE_GET_SUBACCOUNT = conf.getString("api.profileGetSubacc");


  // connect IA with CTG
  public static final String ISOCIAS_NOTIFY_REG_FROM_CTG = conf.getString("api.notifyRegFromCTG");
  public static final String ISOCIAS_NOTIFY_UNREG_FROM_CTG = conf.getString("api.notifyUnRegFromCTG");
  public static final String ISOCIAS_HOLD_IA_FROM_CASH_SERVICE = conf.getString("api.holdIAFromCashService");
  public static final String ISOCIAS_UNHOLD_IA_FROM_CASH_SERVICE = conf.getString("api.unholdIAFromCashService");
  public static final String ISOCIAS_CHANGE_HOLD_IA_FROM_CASH_SERVICE = conf.getString("api.changeHoldIAFromCashService");
  public static final String ISOCIAS_PAYMENT_FOR_BUY_FROM_CASH_SERVICE = conf.getString("api.paymentForBuyFromCashService");
  public static final String ISOCIAS_PAYMENT_FOR_SELL_FROM_CASH_SERVICE = conf.getString("api.paymentForSellFromCashService");
  public static final String ISOCIAS_INQUIRY_TRANSACTION_FROM_CASH_SERVICE = conf.getString("api.inquiryTransactionFromCashService");
  public static final String ISOCIAS_INQUIRY_BALANCE_FROM_CASH_SERVICE = conf.getString("api.inquiryBalanceFromCashService");
  public static final String ISOCIAS_INQUIRY_CONNECT_IA_FROM_PROFILE = conf.getString("api.inquiryConnectIAFromProfile");
  public static final String ISOCIAS_UNSUBSCRIBE_CONNECT_IA_FROM_PROFILE = conf.getString("api.unsubscribeConnectIAFromProfile");



  public static final String ISOCIAS_UPLOAD_IA_RECONCILE = conf.getString("api.uploadIAReconcile");
  public static final String ISOCIAS_PROCESS_IA_RECONCILE = conf.getString("api.processIAReconcile");
  public static final String ISOCIAS_INQUIRY_FROM_CTG= conf.getString("api.inquiryfromCTG");


  //  ACH HB TCC
  public static final String ISOCIAS_ACH_HB_TCC_GET_ACCOUNT_INFO = conf.getString("api.getAccountInfoACHHBTCC");

  // FPT
  public static final String ISOCIAS_CALL_RESULT_VOICE_CHAT = conf.getString("api.isociasCallResultVoiceChat");
  public static final String CRM_CALL_RESULT_VOICE_CHAT = conf.getString("api.crmCallResultVoiceChat");

}
