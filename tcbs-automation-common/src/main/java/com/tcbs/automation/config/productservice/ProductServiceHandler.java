package com.tcbs.automation.config.productservice;

import java.time.Instant;
import java.time.Period;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ProductServiceHandler {
  public static final String FINANCIAL_TERM_CODE = "iBondProTest190";
  public static final String UNDERLYING_CODE = "TCS112020";
  public static final String PRODUCT_NAME = FINANCIAL_TERM_CODE + "." + UNDERLYING_CODE;
  public static final int BROKERAGE = 1;
  public static final String BROKERAGE_TIME = "RRULE:FREQ=MONTHLY;INTERVAL=6";
  public static final String BUY_IN_BROKERAGE = "TCBS";
  public static final String DESCRIPTION = "Test by Tung";
  public static final String UNDERLYING_CLASS = "BOND";
  public static final String NOT_USED_YET = "NOT_USED_YET";
  public static final String BUNDLE = "Normal";
  public static final int ACTIVE = 1;
  public static final int INACTIVE = 0;
  public static final String FINANCIAL_ID_KEY = "financialId";
  public static final String PRODUCT_ID_KEY = "productId";
  public static final String DEFAULT_ACCOUNT = "105C448356";

  public static String openSellDate(int period) {
    return Instant.now().plus(Period.ofDays(period)).toString().substring(0, 10);
  }

  public static String productNameRandom() {

    return FINANCIAL_TERM_CODE + UNDERLYING_CODE + String.format("%07d", ThreadLocalRandom.current().nextInt(100000, 9000000 + 1));
  }

  public static HashMap<String, Object> setDefaultValue() {
    HashMap<String, Object> defaultValue = new HashMap<>();
    defaultValue.put("maxTimeInvest", "60");
    defaultValue.put("maxInvestValue", null);
    defaultValue.put("minTimeInvest", "1");
    defaultValue.put("minInvestValue", "1000000000");
    defaultValue.put("isCoupon", "1");
    defaultValue.put("couponPayment", "3");
    defaultValue.put("sellbackMethod", "2");
    defaultValue.put("brokerFee", null);
    defaultValue.put("brokerFeeDate", null);
    defaultValue.put("fixBrokerFee", null);
    defaultValue.put("fixBrokerDate", null);
    defaultValue.put("totalSellback", null);
    defaultValue.put("totalBuyback", null);
    defaultValue.put("isDefault", "1");
    defaultValue.put("applyRateDate", null);
    defaultValue.put("expiredRateDate", null);
    defaultValue.put("limitValue", "10000000000");
    defaultValue.put("totalLimitValue", null);
    defaultValue.put("cancelSell", "1");
    defaultValue.put("cancelBuy", "1");
    return defaultValue;
  }

}
