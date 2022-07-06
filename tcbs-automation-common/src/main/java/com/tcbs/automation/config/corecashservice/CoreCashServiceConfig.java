package com.tcbs.automation.config.corecashservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class CoreCashServiceConfig {
  private static final Config conf = new ConfigImpl("corecashservice").getConf();

  //Portfolio
  public static final String CASH_ACCOUNT_HOLD_FOR_BOND = conf.getString("cash.hold-for-bond");
  public static final String CASH_ACCOUNT_UNHOLD_FOR_BOND = conf.getString("cash.unhold-for-bond");
  public static final String CASH_BLOCK_RIGHT = conf.getString("cash.blockright");
  public static final String CASH_GET_BLOCK_RIGHTS = conf.getString("cash.get-blockrights");
  public static final String CASH_UNBLOCK_RIGHT = conf.getString("cash.unblockright");
  public static final String INCREASE_CASH_4IXU = conf.getString("cash.increase-cash");
  public static final String DECREASE_CASH_4IXU = conf.getString("cash.decrease-cash");
  public static final String GET_CASH_PAYMENTS = conf.getString("cash.get-cash-payments");
  public static final String PAYMENT_ON_HOLD = conf.getString("cash.payment-on-hold");
  public static final String INQUIRY_TRANSFER_FUND = conf.getString("cash.inquiryTransferFund");
}
