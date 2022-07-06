package com.tcbs.automation.config.assethub;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class AssetHubConfig {
  private static final Config conf = new ConfigImpl("assethub").getConf();

  public static final String ASSET_SUB_ACCOUNT_URI = conf.getString("assethub.asset.sub-account.uri");
  public static final String TCI3_STOCK_ACCOUNT_URI = conf.getString("assethub.tci3-stock.account.uri");
  public static final String ASSET_BASE_URL = conf.getString("assethub.asset.base-url");
  public static final String ASSET_EXT_URL = conf.getString("assethub.asset.ext-url");
  public static final String TCI3_STOCK_URL = conf.getString("assethub.tci3-stock.base-url");
  public static final String ASSET_STOCK_URL = conf.getString("assethub.asset-stock.account.uri");
  public static final String ASSET_STOCK_BASE_URL = conf.getString("assethub.asset-stock.base-url");
  public static final String BOND_SUMMARY = conf.getString("assethub.summary");
  public static final String AUTHORIZATION = conf.getString("assethub.authorization");
  public static final String BOND_OUTSTANDING = conf.getString("assethub.outstanding");

  // TermDeposit
  public static final String ASSET_SUB_GET_TD_INFO = conf.getString("assethub.asset.get.td.info.uri");
  public static final String ASSET_SUB_TD_PERMISSION = conf.getString("assethub.asset.td.permission.uri");

  //MOUNTEBANK
  public static final String MOUNTEBANK_TD_INFO = conf.getString("mountebank.td-info");

  //Asset Controller
  public static final String STOCK_INFO = conf.getString("controller.stock");
  public static final String BOND_INFO = conf.getString("controller.bond");
  public static final String FUND_INFO = conf.getString("controller.fund");
  public static final String LOAN_INFO = conf.getString("controller.loans");
  public static final String CASH_INFO = conf.getString("controller.cash");
  public static final String ASSET_CONTROLLER = conf.getString("assethub.asset-controller");

  public static final String SE_FLEX = conf.getString("assethub.se-flex");
  public static final String FROM_FUND = conf.getString("assethub.from-fund");
  public static final String SE = conf.getString("controller.se");
  public static final String CASH_INVESTMENTS = conf.getString("controller.cashInvestments");
  public static final String PPSE = conf.getString("controller.ppse");
  public static final String PPSE_FLEX = conf.getString("assethub.ppse_flex");
  public static final String ISTOCK = conf.getString("assethub.istock");
  public static final String ACCOUNTING_ITEM = conf.getString("assethub.accounting_item");
  public static final String BOND_TRADING = conf.getString("assethub.bond-trading");
  public static final String ASSETS = conf.getString("controller.assets");
  public static final String ASSETS_WATCHLIST = conf.getString("assethub.asset-watchlist");
  public static final String WATCHLIST = conf.getString("controller.watchlist");

  public static final String TRADER = conf.getString("controller.trader");
  public static final String COPIERS = conf.getString("controller.copiers");
  public static final String COCO_ACC_RELATION = conf.getString("assethub.coco");

  public static final String ICOPY = conf.getString("controller.icopy");
  public static final String QUEUE_PORTFOLIO = conf.getString("assethub.queue");

  public static final String NFM_3RD = conf.getString("assethub.3rd_nfm");
  public static final String TICKER_COMMON = conf.getString("assethub.ticker_commons");
  public static final String RESTAPI_NOTOKEN = conf.getString("assethub.restapi_notoken");
  public static final String TCBS_PRODUCT = conf.getString("assethub.tcbs_product");

  //derivative
  public static final String DERIVATIVE_INFO = conf.getString("controller.derivative");
  public static final String IF_SERVICE = conf.getString("assethub.if_service");
  public static final String PORTFOLIO = conf.getString("controller.portfolio");
  public static final String POSITION = conf.getString("controller.position");
  public static final String CLOSE = conf.getString("controller.close");
  public static final String STATUS = conf.getString("controller.status");
  public static final String PROFILE_DERIVATIVE = conf.getString("assethub.profile_derivative");

  //from coco
  public static final String COPIERS_API = conf.getString("assethub.copiers");
  public static final String COPY_ASSETS = conf.getString("controller.copy-assets");
  public static final String EQUITY = conf.getString("controller.equity");

  public static final String ISAVE = conf.getString("assethub.isave");
}
