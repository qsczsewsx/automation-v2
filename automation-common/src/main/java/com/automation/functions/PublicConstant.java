package com.automation.functions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;

public class PublicConstant {
  public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sssZ");
  public static final SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
  public static final SimpleDateFormat datePickerFormat = new SimpleDateFormat("dd/MM/yyyy");
  public static final NumberFormat NUMBER_RATE_FORMAT = new DecimalFormat("#,###.####");
  public static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,###");
  public static final int DEFAULT_TIMEOUT = 3;
  public static final String DEFAULT_PASSWORD = "abc123";
  public static final String WRONG_PASSWORD = "wrong_passwd";
  public static final String DATA_LIST_CUSTOMER_FILE = System.getProperty("user.dir") + "/src/test/resources/data/starwars/iplan/listCustomerUserName.txt";
  public static final String DATA_CUSTOMER_CREATE_PLAN_REQUEST_BODY = System.getProperty("user.dir") + "/src/test/resources/data/starwars/iplan/customerCreateIplanRequestData.txt";
  public static final String DATA_CUSTOMER_CREATE_PLAN_CSV = System.getProperty("user.dir") + "/src/test/resources/data/starwars/customerCreateIplan.csv";
  public static final String DATA_INVALID_EDIT_TIMELINE = System.getProperty("user.dir") + "/src/test/resources/data/starwars/BondConversion/DataInvalidEditTimeline.csv";
  public static final String DATA_INVALID_GENERATE_TIMELINE = System.getProperty("user.dir") + "/src/test/resources/data/starwars/BondConversion/DataInvalidGenerateTimeline.csv";
  public static final String DATA_CUSTOMER_CREATE_TNOD = System.getProperty("user.dir") + "/src/test/resources/data/starwars/iplan/tnodCreateData.json";
  public static final String DATA_CUSTOMER_CREATE_VLAG = System.getProperty("user.dir") + "/src/test/resources/data/starwars/iplan/vlagCreateData.json";
  // state of cbf
  public static final String CBF_STATE_DRAFTED = "DRAFTED";
  public static final String CBF_STATE_NOT_STARTED = "NOT_STARTED";
  public static final String CBF_STATE_RUNNING = "RUNNING";
  public static final String CBF_STATE_SOLD_OUT = "SOLD_OUT";
  public static final String CBF_STATE_STOPPED = "STOPPED";
  public static final String CBF_STATE_COMPLETED = "COMPLETED";
  public static final String CBF_STATE_REJECTED = "REJECTED";
  public static final String CBF_ACTION_DELETE = "DELETE";
  public static final HashMap<String, String> PRICING_VERIFY_OPTIONS_FOR_BUY = new HashMap<String, String>() {
    {
      put("TotalReceivedAmount", "TotalReceivedAmount-totalReceivedAmount");
      put("TotalReinvestAmount", "TotalReinvestAmount-reinvestmentAmountToMaturity");
      put("Principal", "Principal-totalInvestAmount");
      put("InvestmentRateWithReinvest", "InvestmentRateWithReinvest-ytmReinvested");
      put("UnitPrice", "UnitPrice-unitPrice");
      put("BuyTransactionFee", "BuyTransactionFee-transactionFee"); //TransactionFee
      put("InvestRate", "InvestRate-yieldToMaturity");
    }
  };
  public static final HashMap<String, String> PRICING_VERIFY_OPTIONS_FOR_SELL = new HashMap<String, String>() {
    {
      put("UnitPrice", "UnitPrice-unitPrice");
      put("PIT", "PIT-transactionTax");
      put("NetSellPrice", "NetSellPrice-totalReceivedAmount");
      put("SellTransactionFee", "SellTransactionFee-transactionFee");
    }
  };
  public static final String EXCEL_PRICING_FILE = "src/test/resources/data/iconnect/PRICING_ICONNECT_BB_20191118_ROUNDED_IT_FINAL.xlsx";
  public static final String EXCEL_PRICING_SHEET = "Thong tin giao dich";
  public static final String EXCEL_PRICING_SHEET_PWD = "qwerty12#$";
  public static final String NOT_EQUAL_PURCHASER = "testprecancelOkTrueFundPartyNotEqualPurchaser";
  public static final String EQUAL_PURCHASER = "testprecancelOkTrueFundPartyEqualPurchaser";
  public static final String CANCEL_OK_FALSE = "testprecancelOkFalse";
  public static final String ALL_POLICIES = "failPerformAllPolicies";
  public static final String EMPTY_INPUT = "failPerformEmptyInput";
  public static final String POLICIES_OK_ALL = "successPerformPoliciesOkAll";
  public static final String TRADING_DYNALOG_FILE_PATH = "/src/test/resources/tradingdynalog/performdynalog/";
  public static final String ORDER_SERVICE_FILE_PATH = "/src/test/resources/data/orderservice/";
  public static final String UPDATE_ORDER_FILE_PATH = "/src/test/resources/data/orderservice/updateorderrequest/";
  public final static String EDCM_API_TOKEN_NAME = "token";
  public final static String EDCM_API_USER_NAME_NAME = "username";
  public static final String EDCM_AT_USER_NAME = "edcmATUser";
  public static final String UPDATE_ACCOUNT_INPUT_FILE_PATH = "/src/test/resources/input";
  public static final String ROOT_PATH = System.getProperty("user.dir");

  static {
    isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  static {
    isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  public final SimpleDateFormat dateRequestParamFormat = new SimpleDateFormat("yyyy-MM-dd");

  {
    NUMBER_RATE_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
  }

  {
    NUMBER_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
  }
}
