package com.tcbs.automation.config.paymentprocess;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class PaymentProcessServiceConfig {
  private static final Config conf = new ConfigImpl("paymentprocess").getConf();

  public static final String PAYMENT_PROCESS_BASE_PATH = conf.getString("payment-process.baseUrl");

}
