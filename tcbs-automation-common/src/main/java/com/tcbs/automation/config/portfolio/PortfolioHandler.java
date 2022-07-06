package com.tcbs.automation.config.portfolio;

import java.util.UUID;

public class PortfolioHandler {
  public static final double AFC_VOLUME = 95000;
  public static final double AFM_VOLUME = 1000000;
  public static final double STORE_VOLUME = 300000;
  public static final double QUANTITY = 1000;
  public static final String PARTY_ID = "116724601";
  public static final String ISSUER_ID = "0001443939";
  public static final String MARKETMAKER_ID = "MM1011809";
  public static final String FI_ID = "DF81447488";

  public static final String TCBS_PT = "A001011809";
  public static final String TREASURY = "TR96877551";
  public static final String RETAIL_SOURCE = "RS69336995";
  public static final String FUND_ID = "0001666688";
  public static final String TCBS_ID = "0001011809";
  public static final String ICONNECT_ID = "IC21873509";
  public static final String CUSTOMER_ID = "0001633050";
  public static final String STORE = "ST59539357";
  public static final String SHELF = "SH56995958";
  public static final String UNDERLYING_CODE = "KT25";
  public static final String OTC = "OTC";
  public static final String LISTED = "LISTED";
  public static final String ICONNECT = "ICONNECT";
  public static final String IBOND = "IBOND";
  public static final String NORMALIZED = "NORMALIZED";
  public static final String BUY = "BUY";
  public static final String SELL = "SELL";
  public static final String DONE = "DONE";
  public static final String HOLD = "HOLD";
  public static final String UNHOLD = "UNHOLD";
  public static final String PRE_SELL = "PRE_SELL";
  public static final String CANCELLED_SELLBACK = "CANCELLED_SELLBACK";
  public static final String PAID = "PAID";
  public static final String CANCELLED_PAID = "CANCELLED_PAID";
  public static final String CONFIRMING = "CONFIRMING";
  public static final String CANCELLED_CONFIRMED = "CANCELLED_CONFIRMED";
  public static final String MATCHING = "MATCHING";
  public static final String TRANSFERRED = "TRANSFERRED";
  public static final String MATCHED = "MATCHED";
  public static final String CANCELLED_MATCH = "CANCELLED_MATCH";
  public static final String IN_TRANSIT_OUT = "IN_TRANSIT_OUT";
  public static final String BALANCE = "BALANCE";
  public static final String OFF_BALANCE = "OFF_BALANCE";
  public static final String IN_TRANSIT_IN = "IN_TRANSIT_IN";
  public static final String PRODUCT_CODE = "Test-" + UNDERLYING_CODE;
  public static final String SUCCESSFUL = "SUCCESSFUL";
  public static final String EXCEPTION = "EXCEPTION";
  public static final String DUPLICATED = "DUPLICATED";
  public static final String CONTRACT_VOL = "CONTRACT_VOL";
  public static final String BOND_BACK = "BOND_BACK";
  public static final String BPM = "BPM";
  public static final String TCBSID = "0001026688";
  public static final String PRODCODE = "DPQ05202202";
  public static final String SELL_OFF_BALANCE = "SELL_OFF_BALANCE";

  public static final String HB_RETAIL_SOURCE = "HB502503";
  public static final String HB_SHELF = "HBSH545294";
  public static final String HB_ACCOUNT = "10000028342";
  public static final String HB_ACCOUNT_SIT = "0001002503";
  public static final String HB_ICONNECT = "HBIC13133";
  public static final String TCS = "TCS";
  public static final String HB = "HB";
  public static final String TCS_ICONNECT = "TCS_ICONNECT";
  public static final String ACC_FOR_HB = "10000000103";

  public static final String CANCELLED_MATCHED = "CANCELLED_MATCHED";

  public static final String ACCOUNT_ID_KEY = "accountId";
  public static final String DEBIT = "DEBIT";
  public static final String CREDIT = "CREDIT";
  public static final String VOLUME_STRING = "1000";
  public static final String COUNTER_PARTY = "COUNTER_PARTY";
  public static final String PARTY = "PARTY";

  public static final String FAILURE = "FAILURE";
  public static final String DUPLICATED_MSG = "order event already exits";
  public static final String SUCCESS = "SUCCESS";
  public static final String DEFAULT_SERIAL_CONTRACTCODE = "DEFAULT_SERIAL";
  public static final String TRADING_TYPE_IBOND_PREBOOK = "IBOND_PREBOOK";
  public static final String GOODS_ONHAND_ID_T_2 = "378";
  public static final String GOODS_ONHAND_ID_T_1 = "379";
  public static final String GOODS_ONHAND_ID_T = "380";
  public static final String GOODS_ONHAND_ID_T1 = "383";
  public static final String GOODS_ONHAND_ID_T2 = "384";
  public static final String GOODS_ONHAND_ID_T3 = "385";
  public static final String GOODS_FUTURE_ID_T_1 = "480";
  public static final String GOODS_FUTURE_ID_T = "481";
  public static final String GOODS_FUTURE_ID_T1 = "482";
  public static final String GOODS_FUTURE_ID_T2 = "483";
  public static final String GOODS_FUTURE_ID_T3 = "484";
  public static final String GOODS_FUTURE_ID_T4 = "485";
  public static final String GOODS_FUTURE_ID_T5 = "486";
  public static final String GOODS_FUTURE_ID_T6 = "487";
  public static final String GOODS_FUTURE_ID_T7 = "488";
  public static final String GOODS_FUTURE_ID_T_2 = "490";

  public static final String FORECAST = "FORECAST";
  public static final String FUTURE = "FUTURE";
  public static final String ON_HAND = "ON_HAND";
  public static final String FAIL = "FAIL";

  public static final String MESSAGE_RESP = "get accounting items should return success";
  public static final String MAKE = "MAKE";
  public static final String BREAK = "BREAK";
  public static final String OUT = "OUT";
  public static String genRandomContractCode(String prefix) {
    return prefix + UUID.randomUUID().toString();
  }
}
