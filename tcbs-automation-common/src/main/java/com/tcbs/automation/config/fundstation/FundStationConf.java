package com.tcbs.automation.config.fundstation;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class FundStationConf {
  private static final Config conf = new ConfigImpl("fundstation").getConf();
  //  =============== DOMAIN ===================== //
  public static final Boolean IS_DATA_POWER = Boolean.parseBoolean(conf.getString("isDataPower"));
  // Domain IP
  public static final String CHANEL_IP = conf.getString("ip-domain");
  //LoginWSO2
  public static final String CLIENT_ID = conf.getString("client_id");
  public static final String CLIENT_SECRET = conf.getString("secret");
  // Other Service
  public static final String WSO2_GET_TOKEN = conf.getString("wso2.getToken");
  public static final String API_INTERNAL = conf.getString("api-internal");
  public static final String API_PUBLIC = conf.getString("api-public");
  public static final String FM_DOMAIN_IP = conf.getString("fm-domain-ip");
  //  ============================================ //
  public static final String DSS_DOMAIN = conf.getString("dss-domain");
  public static final String FM_RELOAD = conf.getString("fm.reload-cache");
  public static final String FM_PORTFOLIO_FUND = conf.getString("fm.portfolio-fund");
  //  CRUD
  public static final String FM_ADD_PORTFOLIO = conf.getString("fm.add-portfolio");
  public static final String FM_GET_PORTFOLIO_BY_ID = conf.getString("fm.get-portfolio-by-id");
  public static final String FM_GET_PORTFOLIO_BY_CODE = conf.getString("fm.get-portfolio-by-code");
  public static final String FM_UPDATE_PORTFOLIO = conf.getString("fm.update-portfolio");
  public static final String FM_GET_ALL_PORTFOLIO = conf.getString("fm.get-all-portfolio");
  public static final String FM_IMPORT_TRANSACTION = conf.getString("fm.import-transaction");
  public static final String FM_ADD_ACCOUNT = conf.getString("fm.add-account");
  public static final String FM_GET_ACCOUNT_BY_ID = conf.getString("fm.get-account-by-id");
  public static final String FM_GET_ACCOUNT_BY_USER = conf.getString("fm.get-account-by-user");
  public static final String FM_GET_ALL_ACCOUNT = conf.getString("fm.get-all-account");
  public static final String FM_UPDATE_ACCOUNT = conf.getString("fm.update-account");
  public static final String FM_ADD_USER = conf.getString("fm.add-user");
  public static final String FM_GET_USER_BY_ID = conf.getString("fm.get-user-by-id");
  public static final String FM_GET_USER_BY_USER = conf.getString("fm.get-user-by-user");
  public static final String FM_GET_ALL_USER = conf.getString("fm.get-all-user");
  public static final String FM_UPDATE_USER = conf.getString("fm.update-user");
  public static final String FM_GET_ALL_INDUSTRY = conf.getString("fm.get-all-industry");
  public static final String FM_GET_ALL_GROUP = conf.getString("fm.get-all-group");
  public static final String FM_GET_COMPANY = conf.getString("fm.get-company");
  public static final String FM_ADD_COMPANY = conf.getString("fm.add-company");
  public static final String FM_ADD_BROKER = conf.getString("fm.add-broker");
  public static final String FM_GET_BROKER_BY_ID = conf.getString("fm.get-broker-by-id");
  public static final String FM_GET_ALL_BROKER = conf.getString("fm.get-all-broker");
  public static final String FM_ADD_COUNTER_PARTY = conf.getString("fm.add-counter-party");
  public static final String FM_GET_COUNTER_PARTY_BY_ID = conf.getString("fm.get-counter-party-by-id");
  public static final String FM_GET_ALL_COUNTER_PARTY = conf.getString("fm.get-all-counter-party");
  //  SYNC
  public static final String FM_SYNC_COMPANIES = conf.getString("fm.sync-companies");
  public static final String FM_SYNC_STOCKS = conf.getString("fm.sync-stocks");
  public static final String FM_SYNC_BONDS = conf.getString("fm.sync-bonds");
  public static final String FM_SYNC_FUNDS = conf.getString("fm.sync-funds");
  //  REPORT
  public static final String FM_REPORT_TRANS = conf.getString("fm.report-trans");
  public static final String FM_REPORT_MTM = conf.getString("fm.report-mtm");
  public static final String FM_GET_ALL_MKT_PRICE = conf.getString("fm.get-all-mkt-price");
  public static final String FM_IMPORT_MKT_PRICE = conf.getString("fm.import-mkt-price");
  public static final String FM_MIGRATE_LISTED_PRICE = conf.getString("fm.migrate-listed-price");
  //  CONF
  public static final String FM_CONF_PORTFOLIO_TYPE = conf.getString("fm.conf-portfolio-type");
  //  ALLOCATION
  public static final String FM_ADD_ALLOCATION = conf.getString("fm.add-allocation");
  public static final String FM_GET_ALLOCATION = conf.getString("fm.get-allocation");
  public static final String FM_GET_ALLOCATION_PUBLIC = conf.getString("fm.get-allocation-public");
  public static final String FM_GET_SUGGEST_PORTFOLIO = conf.getString("fm.get-suggest-portfolio");
  public static final String FM_GET_SUGGEST_PORTFOLIO_PUBLIC = conf.getString("fm.get-suggest-portfolio-public");
  public static final String FM_LIST_SUGGEST_PORTFOLIO = conf.getString("fm.list-suggest-portfolio");
  public static final String FM_LIST_SUGGEST_PORTFOLIO_PUBLIC = conf.getString("fm.list-suggest-portfolio-public");
  // PUBLIC
  public static final String FM_GET_ALLOCATION_PORTFOLIO_TYPE_PUB = conf.getString("fm.get-allocation-portfolio-type-pub");
  //  INVEST PORTFOLIO
  public static final String FM_ADD_INVEST_PORTFOLIO = conf.getString("fm.add-invest-portfolio");
  public static final String FM_GET_INVEST_PORTFOLIO = conf.getString("fm.get-invest-portfolio");
  public static final String FM_GET_LIST_PORTFOLIO_MANAGER = conf.getString("fm.get-list-portfolio-manager");
  //  DSS
  public static final String DSS_STOCK_INFO = conf.getString("dss.company-info");
  public static final String DSS_BOND_LISTED_PRICE = conf.getString("dss.bond-listed-price");
  //  BOND
  public static final String BS_BONDS_INFO = conf.getString("bond.bonds-info");
  //  FUND TRADING
  public static final String FUND_TRADING_DOMAIN = conf.getString("fund-trading");
  public static final String FT_GET_HOLIDAY = conf.getString("ft.get-holiday");
  public static final String FT_GET_PRODUCT = conf.getString("ft.get-product");
  // TRANS
  public static final String FM_AUTO_GEN_TRANS = conf.getString("fm.auto-gen-trans");
  public static final String FM_GET_TRANS_GEN = conf.getString("fm.get-trans-gen");
  public static final String FM_GET_TRANS_NUMBER = conf.getString("fm.get-trans-number");
  // POLICY
  public static final String FM_POLICY_CALCULATE = conf.getString("fm.policy-calculate");
  // PROJECTION
  public static final String FM_PROJECTION_GET_ALL = conf.getString("fm.projection-getAll");
  public static final String FM_PROJECTION_A_DATE = conf.getString("fm.projection-a-date");
  public static final String FM_PROJECTION_GEN_TRANS = conf.getString("fm.projection-gen-trans");
  public static final String FM_PROJECTION_CALCULATE_RATE = conf.getString("fm.projection-calculate-rate");
  // NET INFLOW
  public static final String FM_ADD_NET_INFLOW = conf.getString("fm.add-net-inflow");
  public static final String FM_GET_NET_INFLOW = conf.getString("fm.get-net-inflow");
  public static final String FM_GET_NET_INFLOW_INFO = conf.getString("fm.get-net-inflow-info");
  //SUB AND RED
  public static final String SAVE_CASH_IN_TRANSIT = conf.getString("fm.save-cash-in-transit");
  // Channel
  public static final String FM_ADD_CHANNEL = conf.getString("fm.add-channel");
  public static final String FM_GET_CHANNEL = conf.getString("fm.get-channel");
  public static final String FM_GET_CHANNELS = conf.getString("fm.get-channels");
  public static final String FM_DELETE_CHANNEL = conf.getString("fm.delete-channel");
  public static final String FM_UPDATE_CHANNEL = conf.getString("fm.update-channel");
  public static final String FM_GET_LIST_ALL_CHANNEL = conf.getString("fm.list-all-channel");
  public static final String FM_GET_CHANNEL_DETAIL = conf.getString("fm.get-channel-detail");
  public static final String FM_GET_CHANNEL_WITH_TYPE = conf.getString("fm.get-channel-list-by-type");
  // PLANNING
  public static final String FM_ADD_PLAN = conf.getString("fm.add-plan");
  public static final String FM_GET_LIST_PLAN = conf.getString("fm.get-list-plan");
  public static final String FM_GET_PLAN_DETAIL = conf.getString("fm.get-plan-detail");
  public static final String FM_UPDATE_PLAN = conf.getString("fm.update-plan");
  public static final String FM_DELETE_PLAN = conf.getString("fm.delete-plan");
  public static final String FM_GET_LIST_PLAN_TRANSACTION = conf.getString("fm.ts-plan-transaction-list");
  public static final String FM_GET_BOND_TEMP_DETAIL_FOR_GET_TRANSACTION = conf.getString("fm.product-get-bond-case-detail");
  public static final String CONF_TS_PLAN_DETAIL_GET_FREE_BOND_TEMP = conf.getString("fm.ts-plan-detail-get-free-bond-temp");
  public static final String CONF_TS_PLAN_DETAIL_ADD = conf.getString("fm.ts-plan-detail-add");
  public static final String TS_PLAN_SAVE_ISSUE = conf.getString("fm.ts-plan-save-issue");
  public static final String TS_PLAN_MOVEMENT_ADD_POPUP_TRANSACTION = conf.getString("fm.plan-movement-add-popup-transactions");
  public static final String TS_PLAN_SAVE_DATA_MOVEMENT_RETURN = conf.getString("fm.save-plan-movement-add-popup-transactions");
  public static final String TS_PLAN_GET_DATA_MOVEMENT_RETURN = conf.getString("fm.get-plan-movement-add-popup-transactions");
  // BOND TEMP
  public static final String FM_SAVE_BOND_TEMP = conf.getString("fm.save-bond-temp");
  public static final String FM_BOND_TEMP_DETAIL = conf.getString("fm.get-detail-bond-temp");
  public static final String FM_BOND_TEMP_HISTORY = conf.getString("fm.get-bond-temp-history");
  public static final String FM_LIST_ALL_BOND_TEMP = conf.getString("fm.get-list-all-bond-temp");
  public static final String CONF_API_B_V1_PRODUCT_DELETE_BOND_TEMP = conf.getString("product-delete-bond-temp ");
  //MOVEMENT
  public static final String CONF_PRODUCT_MOVEMENT_LIST_BOND_TEMP = conf.getString("product-movement-list-bond-temp");
  public static final String CONF_API_B_V1_PRODUCT_GET_BOND_TEMP = conf.getString("product-get-bond-temp");
  // FEE
  public static final String FEE_ADD = conf.getString("fm.fee-add");
  public static final String FEE_UPDATE = conf.getString("fm.fee-update");
  public static final String FEE_DELETE = conf.getString("fm.fee-delete");
  public static final String FEE_GET = conf.getString("fm.fee-get");
  public static final String FEE_GET_DETAIL = conf.getString("fm.fee-get-detail");
  //REBALANCE
  public static final String AUTO_REBALANCE_PORTFOLIO_MANAGEMENT_LIST = conf.getString("fm.ar_portfolio_management_list");
  private static final String ROUTER_FS = "/asset-management";
  public static final String INT_DOMAIN = IS_DATA_POWER ? conf.getString("int-domain") + ROUTER_FS : CHANEL_IP + "/i";
  public static final String EXT_DOMAIN = IS_DATA_POWER ? conf.getString("ext-domain") + ROUTER_FS : CHANEL_IP + "/e";
  public static final String PUB_DOMAIN = IS_DATA_POWER ? conf.getString("pub-domain") + ROUTER_FS : CHANEL_IP + "/p";
  public static final String BACK_DOMAIN = IS_DATA_POWER ? conf.getString("back-domain") + ROUTER_FS : CHANEL_IP + "/b";
  public static final String THIRD_DOMAIN = IS_DATA_POWER ? conf.getString("third-domain") + ROUTER_FS : CHANEL_IP + "/t";
  public static final String FM_DOMAIN = fmDomain(false);

  private static String fmDomain(boolean isPublic) {
    if (Boolean.parseBoolean(conf.getString("isDataPower"))) {
      if (isPublic) {
        return API_PUBLIC + ROUTER_FS;
      } else {
        return API_INTERNAL + ROUTER_FS;
      }
    } else {
      return FM_DOMAIN_IP;
    }
  }
}
