package com.tcbs.automation.config.fundstation;

public class FundStationConstants {
  //  PRODUCT TYPE
  public static final String PRODUCT_TYPE_FUND = "FUND";
  public static final String PRODUCT_TYPE_CORP_BOND = "CORP_BOND";
  public static final String PRODUCT_TYPE_STOCK = "STOCK";
  public static final String PRODUCT_TYPE_CD = "CD";
  public static final String PRODUCT_TYPE_TD = "TERM_DEP";
  public static final String PRODUCT_TYPE_GOV_BOND = "GOV_BOND";
  public static final String PRODUCT_TYPE_BOND = "BOND";
  public static final String PRODUCT_TYPE_MONETARY = "MONETARY";
  public static final String TYPE_OTHER = "Other";
  public static final String PRODUCT_TYPE_CASH = "CASH";
  public static final String METHOD_FIXED = "FIXED";
  public static final String METHOD_FLOAT = "FLOAT";

  public static final String TOTAL = "TOTAL";
  public static final String ASSETS_TOTAL = "ASSETS_TOTAL";
  public static final String MKT_OTC = "MKT_OTC";
  public static final String MKT_TOTAL = "MKT_TOTAL";
  public static final String KEY_COMPANY = "COMPANY_";
  public static final String KEY_GROUP = "GROUP_";
  public static final String TOTAL_TICKER_IN_GROUP = "TOTAL_TICKER_";
  public static final String RATE_GROUP = "RATE_GROUP_";

  public static final String FUNCTION_MAX = "MAX";
  public static final String FUNCTION_MIN = "MIN";
  public static final String FUNCTION_SUM = "SUM";

  public static final Double MAX_RATE_OTC = 0.1; //10%
  public static final Double MAX_RATE_COMPANY = 0.2; //20%
  public static final Double MAX_RATE_GROUP = 0.3; //30%

  //  ACTION
  public static final int ACTION_BUY = 1;
  public static final int ACTION_SELL = 2;
  public static final int ACTION_PRINCIPAL = 3;
  public static final int ACTION_DIVIDEND = 4;
  public static final int ACTION_WITHDRAWAL = 5;
  public static final int ACTION_PLACEMENT = 6;
  public static final int ACTION_CASH = 7;

  public static final int COUPON_PAYMENT_TYPE_END_OF_PERIOD = 6;

  //  LISTED STATUS
  public static final int PRODUCT_STATUS_NA = -1;
  public static final int PRODUCT_STATUS_OTC = 0;
  public static final int PRODUCT_STATUS_LISTED = 1;

  //  CONF
  public static final String CONF_CONVENTION = "CONVENTION";
  public static final String CONF_JOB_PORTFOLIO = "JOB_PORTFOLIO";
  public static final String CONF_TRANSACTION_ACTION = "TRANSACTION_ACTION";
  public static final String PRODUCT_GLOBAL_STATUS = "PRODUCT_GLOBAL_STATUS";
  public static final String CONF_OBJECT_CLASS = "OBJECT_CLASS";
  public static final String CONF_PUBLIC = "PUBLIC";
  public static final String CONF_POLICY_TYPE = "POLICY_TYPE";

  //  STATUS
  public static final String ACTIVE = "ACTIVE";
  public static final String DRAFT = "DRAFT";

  //  ACCESS TYPE
  public static final String OWNER = "OWNER";
  public static final String TRUSTED = "TRUSTED";

  //  ACCOUNT_TYPE
  public static final String PORTFOLIO_TYPE_DEMO = "DEMO";

  public static final String USER_TYPE_INTERNAL = "INTERNAL";
  public static final String USER_TYPE_CUSTOMER = "CUSTOMER";
  public static final String CLASS_TYPE_INDIVIDUAL = "Individual";
  public static final String CLASS_TYPE_COOPERATION = "Cooperation";

  //  URL
  public static final String URL_FOLDER_DATA = "/src/test/resources/File Data";
  public static final String URL_ALLOCATION = URL_FOLDER_DATA + "/PortfolioList.xlsx";

  // STANDARD ERROR
  public static final Double STANDARD_ERROR = 0.000001;

  // NULL CHECK
  public static final Integer ALL_LIST_IS_NOT_NULL = 0;
  public static final Integer LIST_ONE_NULL = 1;
  public static final Integer LIST_TWO_NULL = 2;
  public static final Integer ALL_LIST_NULL = 3;

  // POLICY
  public static final Integer POLICY_TYPE_COMPANY = 0;
  public static final Integer POLICY_TYPE_GROUP = 1;
  public static final Integer POLICY_TYPE_PRODUCT = 2;
  public static final Integer POLICY_TYPE_BROKER = 3;
  public static final Integer POLICY_TYPE_TICKER_BIG = 4;

  // PROJECTION
  public static final String PERIOD_WEEK = "WEEK";
  public static final String PERIOD_MONTH = "MONTH";

  // INVEST TYPE
  public static final String INVEST_TYPE_CURRENT = null;
  public static final Integer INVEST_TYPE_FUTURE = 1;

  // ERROR MESSAGE
  public static final String MSG_SUCCESS = "Success";

  //  AUTHORIZATION
  public static final String ADMIN_USER = "admin02";
  public static final String ADMIN_PASS = "admin02";
  public static final String ACCOUNT_DEFAULT = "TCC_AUTO";
  public static final String MK_TC3_DEFAULT = "abc123";
  public static final String AUTHORIZATION_KEY = "Bearer eyJ4NXQiOiJaalJtWVRNd05USmpPV1U1TW1Jek1qZ3pOREkzWTJJeU1tSXlZMkV6TWpkaFpqVmlNamMwWmciLCJraWQiOiJaalJtWVRNd05USmpPV1U1TW1Jek1qZ3pOREkzWTJJeU1tSXlZMkV6TWpkaFpqVmlNamMwWmdfUlMyNTYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ3c28yaXNhZG1pbiIsImF1ZCI6IkJyb0ZVbTFrZjZXdnRXSjdNaE1BSk9uZGxWY2EiLCJuYmYiOjE1OTA5ODU4NzQsImF6cCI6IkJyb0ZVbTFrZjZXdnRXSjdNaE1BSk9uZGxWY2EiLCJzY29wZSI6Im9wZW5pZCIsImlzcyI6Imh0dHBzOlwvXC93c28yaXMtc2l0LnRjYnMuY29tLnZuXC9vYXV0aDJcL3Rva2VuIiwiZ3JvdXBzIjpbIkFwcGxpY2F0aW9uXC9CQVlNQVhfVEVTVEVSIiwiQXBwbGljYXRpb25cL0NEX0FETUlOIiwiQXBwbGljYXRpb25cL1VTRVJfQ0QiLCJJbnRlcm5hbFwvZXZlcnlvbmUiLCJBcHBsaWNhdGlvblwvQkFZTUFYX1ZJRVdFUiIsIkFwcGxpY2F0aW9uXC9VU0VSX0JFIiwiQXBwbGljYXRpb25cL0ZNX0FETUlOIiwiQXBwbGljYXRpb25cL0JBWU1BWF9BRE1JTiIsIkFwcGxpY2F0aW9uXC9VU0VSX0ZNIl0sImV4cCI6MTU5MDk4OTQ3NCwiaWF0IjoxNTkwOTg1ODc0LCJqdGkiOiIxY2QyYjZlYS1hYTE4LTQxNTctYjI1Zi02MDE1ZjU2OTBmZDEifQ.BfYQCV3XBVsaX1icmosLSvprsC20Y073gALgwEZZdy-29rY650AVhd2MAkO4_Xoz3LYc4f_pZk_0sfuO0laVZlAQZDk8sMjb_Rrqik8HEE1bfa7_ve3u0oj035wY1aWWeTUtqpMGNWAT7OlUpjEDMXgsVfwySg8JLPbygG4vQi1MvmjpR5m4wDzn8gtdjIR8BkKjsy9k3hQyHGdR_GgyRdDRb0QLhWza6RO-tWSjMxV_QyQhiOGyee-narttZoK9X4_HeByg7gvuQagSqLscrlyF2vcPbKhpcDb8iS5hZX1i_yF5li9m1aEPcLvGoJgCs5Xw8nCjXZhjiqEDMEQM6w";
  public static final String X_API_KEY = "x-api-key";
  public static final String AUTHORIZATION = "Authorization";
  public static final String X_API_KEY_FS = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiAiaHR0cHM6Ly9kYXRhcG93ZXIudGNicy5jb20udm4iLCAic3ViIjogImFzc2V0cy1tYW5hZ2VtZW50IiwgImlhdCI6IDE2MDI2NjQyNTAsICJleHAiOiAxOTE4MDI0MjUwfQ.P1ipiCZAuz9_YHY4UtP5-Ho6M-f4OXpv_WH98K-Xa8Hk-EaYeEhSCOTPJUMNa3Oavyd2IKud2PElyYtaYRcU5r3LsHejjJGficnS1ftBy5Q0WtORC6PgfHZ6BgVSMAlnh-wkt9Rol933wxg7zpXxCqJc8L4KjBP1WN8VCuZAmbohsQEXFPwSQd_aM2Nz8oM0713YywmLdcGufQkrDNR1cvdIVBjN5hrCOuSbthfQn1HVz6HloG84di7ELRf2CAYNNTMlm8SMYRFfRV8xgG94cS0c6z1D8qR6orUQxntZZNtRJqLQ4MYn1mGvmH1e-fO0Dawpo6UmoEumT5-HO5K0CQ";
}
