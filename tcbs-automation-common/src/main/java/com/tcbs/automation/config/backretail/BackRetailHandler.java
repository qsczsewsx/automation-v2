package com.tcbs.automation.config.backretail;

import java.util.UUID;

public class BackRetailHandler {

  public static String genRandomContractCode(String prefix) {
    return prefix + UUID.randomUUID().toString();
  }

  public static final String MERCHANT_KEY = "merchant";
  public static final String BOND_CODE_KEY = "bondCode";
  public static final String PRODUCT_CODE_KEY = "productCode";

  public static final String MAKE_BALANCE_KEY = "makeBalance";
  public static final String BALANCE_KEY = "balance";
  public static final String OFF_BALANCE_KEY = "offBalance";
  public static final String IS_DETAILS_KEY = "isDetails";

  public static final String SHELF_BALANCE_KEY = "shelfBalance";
  public static final String CONFIRMED_KEY = "confirmed";

  public static final String SOURCE_KEY = "source";

  public static final String SETTLEMENT_ACCOUNT_NUBMER = "settlementAccountNumber";
  public static final String TCBS_ID_KEY = "tcbsId";
  public static final String QUANTITY_KEY = "quantity";
  public static final String TYPE_KEY = "type";
  public static final String OUT_KEY = "out";
  public static final String DATE_KEY = "date";
  public static final String AFC_KEY = "afc";
  public static final String DEATAILS_KEY = "details";
  public static final String GOODS_KEY = "goods";
  public static final String BAL_FUTURE_KEY = "balFuture";
  public static final String NOTIFY_KEY = "notify";
  public static final String OUT_FUTURE_KEY = "outFuture";
  public static final String AFC_ONHAND_KEY = "afcOnHand";
  public static final String BAL_MINUS_OUT_FUTURE_KEY = "balMinusOutFuture";
  public static final String ACCOUNT_KEY = "accountId";
  public static final String LATEST_DATE_KEY = "latestDate";
  public static final String INTRANSIT_OUT_KEY = "inTransitOut";
  public static final String MIX_BALANCE_KEY = "mixBalance";
  public static final String AFT_KEY = "aft";
  public static final String ACCOUNT_ITEM_KEY = "accountItem";
  public static final String ACCOUNT_ITEM_ATTR_KEY = "accountingItemAttr";
  public static final String ONHAND_KEY = "ON_HAND";

  public static final String INSERT = "INSERT";
  public static final String ORDER_BACK = "orderback";
  public static final String ORDER = "order";
  public static final String ACTION_APPROVE = "approve";
  public static final String ACTION_CANCEL = "cancel";
  public static final String ACTION_REJECT = "reject";
  public static final String MM_STORE = "MM1011809";
  public static final String LEVEL_PRODUCT = "PRODUCT";

  public static final String MAKE_BALANCE_MM_KEY = "makeBalanceMm";
  public static final String TO_DATE_TIME_KEY = "toDateTime";
  public static final String HOLD_KEY = "hold";
  public static final String IN_KEY = "in";
  public static final String PRODUCTS_KEY = "products";
  public static final String TYPES_KEY = "types";
}
