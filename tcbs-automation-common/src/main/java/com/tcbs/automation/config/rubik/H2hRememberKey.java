package com.tcbs.automation.config.rubik;

public class H2hRememberKey {
  public static final String TRANSACTION_SINGLE = "TRANSACTION_SINGLE";
  public static final String TRANSACTION_SINGLE_RETRY = "TRANSACTION_SINGLE_RETRY";
  public static final String TRANSACTION_SINGLE_FAST = "TRANSACTION_SINGLE_FAST";
  public static final String TRANSACTION_BATCH = "TRANSACTION_BATCH";
  public static final String TRANSACTION_BATCH_REALTIME = "TRANSACTION_BATCH_REALTIME";
  public static final String TRANSACTION_BATCH_RETRY = "TRANSACTION_BATCH_RETRY";
  public static final String TRANSACTION_FE = "TRANSACTION_FE";
  public static final String TRANSACTION_REVIEW = "TRANSACTION_REVIEW";
  public static final String TRANSACTION_REVIEW_FT = "TRANSACTION_REVIEW_FT";
  public static final String LIST_REVIEWER = "LIST_REVIEWER";
  public static final String LIST_LOG = "LIST_LOG";
  public static final String LIST_RECONCILE = "LIST_RECONCILE";
  public static final String LIST_RECONCILE_FE = "LIST_RECONCILE_FE";
  public static final String LIST_TRANSACTION = "LIST_TRANSACTION";
  public static final String RECONCILE_EXPORT_EXCEL = "RECONCILE_EXPORT_EXCEL";
  public static final String TRANSACTION_EXPORT_EXCEL = "TRANSACTION_EXPORT_EXCEL";
  public static final String INTERNAL_FUND_TRANSFE_REAL_TIME_REMEMBER = "INTERNAL_FUND_TRANSFE_REAL_TIME_REMEMBER";
  public static final String RETRY_INTERNAL_FUND_TRANSFE_REAL_TIME_REMEMBER = "RETRY_INTERNAL_FUND_TRANSFE_REAL_TIME_REMEMBER";

  public static final String BBS_INQUIRY_RESPONSE = "BBS_INQUIRY_RESPONSE";
  public static final String TXN_SYNC_RESPONSE = "TXN_SYNC_RESPONSE";
  public static final String STOCK_RECONCILE_RESPONSE = "STOCK_RECONCILE_RESPONSE";
  public static final String RERUN_INQUIRY_BY_TXNID_RESPONSE = "RERUN_INQUIRY_BY_TXNID_RESPONSE";
  public static final String FLEX_RECONCILE_CONTENT = "FLEX_RECONCILE_CONTENT";
  public static final String FLEX_RECONCILE_SUMMARY = "FLEX_RECONCILE_SUMMARY";
  public static final String LIST_RECONCILE_IN_FLEXREMEMBER = "LIST_RECONCILE_IN_FLEXREMEMBER";
  public static final String GET_LIST_TRANSACTION_PAY_COUPON_REMEMBER = "GET_LIST_TRANSACTION_PAY_COUPON";
  public static final String SEARCH_FOR_MONEY_TRANSFER_SELL_TO_CUSTOMERS_REMEMBER = "SEARCH_FOR_MONEY_TRANSFER_SELL_TO_CUSTOMERS";
  public static final String ANI_BATCH_TRANSFER_RESPONSE = "ANI_BATCH_TRANSFER_RESPONSE";
  public static final String ANI_BATCH_TRANSFER_RETRY_RESPONSE = "ANI_BATCH_TRANSFER_RETRY_RESPONSE";
  public static final String REINQUIRY_STATUS_WITH_TRANSACTION_LIST_REMEMBER = "REINQUIRY_STATUS_WITH_TRANSACTION_LIST_REMEMBER";
  public static final String CHANGE_STATUS_ERROR_TRANSACTION_REMEMBER = "CHANGE_STATUS_ERROR_TRANSACTION_REMEMBER";
  public static final String BBK_ADD_PATTERN_RESPONSE = "BBK_ADD_PATTERN_RESPONSE";
  public static final String BBK_GET_BY_PATTERN_RESPONSE = "BBK_GET_BY_PATTERN_RESPONSE";
  public static final String GET_ACCOUNT_INFOR_DWH_REMEMBER = "GET_ACCOUNT_INFOR_DWH_REMEMBER";
  public static final String TRUSTED_ACCOUNT_WITH_CREATE_ACCOUNT_REMEMBER = "TRUSTED_ACCOUNT_WITH_CREATE_ACCOUNT_REMEMBER";
  public static final String TRANSFER_MONEY_FROM_FUTURE_OUT_REMEMBER = "TRANSFER_MONEY_FROM_FUTURE_OUT_REMEMBER";
  public static final String TRANSFER_MONEY_PAYMENT_INTERNAL_FROM_CAPITAL_TO_OUT_REMEMBER = "TRANSFER_MONEY_PAYMENT_INTERNAL_FROM_CAPITAL_TO_OUT_REMEMBER";
  public static final String TRANSFER_MONEY_FROM_FUTURE_TO_CAPITAL_MARKET_REMEMBER = "TRANSFER_MONEY_FROM_FUTURE_TO_CAPITAL_MARKET_REMEMBER";
  public static final String TRANSFER_MONEY_PAYMENT_TCI3_FROM_DERIVATIVE_TO_OUT_REMEMBER = "TRANSFER_MONEY_PAYMENT_TCI3_FROM_DERIVATIVE_TO_OUT_REMEMBER";
  public static final String INQUIRY_TRANSACTION_FORFLOW_EXTENAL_FUTURE_AND_CAPITAL_MARKET_REMEMBER = "INQUIRY_TRANSACTION_FORFLOW_EXTENAL_FUTURE_AND_CAPITAL_MARKET_REMEMBER";
  public static final String UPDATE_RESULT_RECONCILE_REMEMBER = "UPDATE_RESULT_RECONCILE_REMEMBER";
  public static final String ADD_TRANSACTION_RECONCILE_REMEMBER = "ADD_TRANSACTION_RECONCILE_REMEMBER";
  public static final String MAKER_VIEW_TRANSACTION_MANUAL_IMPACT_HISTORY_REMEMBER = "MAKER_VIEW_TRANSACTION_MANUAL_IMPACT_HISTORY_REMEMBER";
  public static final String BATCH_TRANSFER_ISAVE_REMEMBER = "BATCH_TRANSFER_ISAVE_REMEMBER";
  public static final String BATCH_TRANSFER_ANI_HB_REMEMBER = "BATCH_TRANSFER_ANI_HB_REMEMBER";
  public static final String TRANSACTION_BATCH_ACH_TCS_REMEMBER= "TRANSACTION_BATCH_ACH_TCS_REMEMBER";
  public static final String BATCH_TRANSFER_ANI_SELL_BOND_HB_REMEMBER  = "BATCH_TRANSFER_ANI_SELL_BOND_HB_REMEMBER";
  public static final String RETRY_BATCH_TRANSFER_ANI_SELL_BOND_HB_REMEMBER  = "RETRY_BATCH_TRANSFER_ANI_SELL_BOND_HB_REMEMBER";
  // silkgate remember key
  public static final String HOLD_MONEY_RESPONSE = "HOLD_MONEY_RESPONSE";
  public static final String UNHOLD_MONEY_RESPONSE = "UNHOLD_MONEY_RESPONSE";
  public static final String CHECK_CASH_OUT_AMOUNT_REMEMBER = "CHECK_CASH_OUT_AMOUNT_REMEMBER";

  // Cash remember key
  public static final String INFORMATION_CLIENT_FROM_CASH_REMEMBER = "INFORMATION_CLIENT_FROM_CASH_REMEMBER";
  public static final String HOLD_MONEY_OF_CAPITAL_MARKET_REMEMBER = "HOLD_MONEY_OF_CAPITAL_MARKET_REMEMBER";
  public static final String CUT_PAYMENT_FEE_BPM_REMEMBER = "CUT_PAYMENT_FEE_BPM_REMEMBER";
  public static final String INQUIRY_CUT_PAYMENT_FEE_BPM_REMEMBER = "INQUIRY_CUT_PAYMENT_FEE_BPM_REMEMBER";
  public static final String INQUIRY_LOAN_HOLDING_BUY_BOND_REMEMBER = "INQUIRY_LOAN_HOLDING_BUY_BOND_REMEMBER";
  public static final String HOLDING_MONEY_ACCOUNT_CONNECT_AI_REMEMBER = "HOLDING_MONEY_ACCOUNT_CONNECT_AI_REMEMBER";
  public static final String UNHOLDING_MONEY_ACCOUNT_CONNECT_AI_REMEMBER = "UNHOLDING_MONEY_ACCOUNT_CONNECT_AI_REMEMBER";
  public static final String INQUIRY_HOLD_AND_UNHOLDING_MONEY_ACCOUNT_CONNECT_AI_REMEMBER = "INQUIRY_HOLD_AND_UNHOLDING_MONEY_ACCOUNT_CONNECT_AI_REMEMBER";
  public static final String RECEIVE_NOTIFY_BOND_SALE_ORDER_REMEMBER = "RECEIVE_NOTIFY_BOND_SALE_ORDER_REMEMBER";
  public static final String RECEIVE_NOTIFY_FLEX_SALE_ORDER_REMEMBER = "RECEIVE_NOTIFY_FLEX_SALE_ORDER_REMEMBER";
  public static final String ADDITIONAL_HOLD_FOR_BOND_LOAN_REMEMBER = "ADDITIONAL_HOLD_FOR_BOND_LOAN_REMEMBER";
  public static final String GET_LIST_TRANSACTION_LOAN_REMEMBER = "GET_LIST_TRANSACTION_LOAN_REMEMBER";
  public static final String INQUIRY_RESULT_COLLECTION_REMEMBER = "INQUIRY_RESULT_COLLECTION_REMEMBER";
  public static final String INQUIRY_LIST_SUBACCOUNTTRANSACTION_REMEMBER = "INQUIRY_LIST_SUBACCOUNTTRANSACTION_REMEMBER";
  public static final String INQUIRY_LIST_SUBACCOUNT_TRANSACTION_LOG_REMEMBER = "INQUIRY_LIST_SUBACCOUNT_TRANSACTION_LOG_REMEMBER";
  public static final String INCREASE_SUBACCOUNT_FROM_IVOUCHER_REMEMBER = "INCREASE_SUBACCOUNT_FROM_IVOUCHER_REMEMBER";
  public static final String DECREASE_SUBACCOUNT_FROM_IVOUCHER_REMEMBER = "DECREASE_SUBACCOUNT_FROM_IVOUCHER_REMEMBER";
  public static final String INCREASE_SUBACCOUNT_FROM_ISAVE_REMEMBER = "INCREASE_SUBACCOUNT_FROM_ISAVE_REMEMBER";
  public static final String DECREASE_SUBACCOUNT_FROM_ISAVE_REMEMBER = "DECREASE_SUBACCOUNT_FROM_ISAVE_REMEMBER";
  public static final String INQUIRY_INCREASE_DECREASE_ISAVE_REMEMBER = "INQUIRY_INCREASE_DECREASE_ISAVE_REMEMBER";
  public static final String BATCH_TRANSFER_INTEREST_PAYMENT_ISAVE_REMEMBER = "BATCH_TRANSFER_INTEREST_PAYMENT_ISAVE_REMEMBER";
  public static final String BATCH_TRANSFER_DECREASE_ISAVE_REMEMBER = "BATCH_TRANSFER_DECREASE_ISAVE_REMEMBER";
  public static final String BATCH_INQUIRY_BALANCE_ACCOUNT_REMEMBER = "BATCH_INQUIRY_BALANCE_ACCOUNT_REMEMBER";

}