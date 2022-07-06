package com.tcbs.automation.config.coco;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class CocoServiceConfig {
  private CocoServiceConfig() {}
  private static final Config conf = new ConfigImpl("coco").getConf();

  public static final String COCO_X_API_KEY_ACCOUNT_B = conf.getString("coco.x-api-key-account-B");
  public static final String MONGO_DB_NAME = conf.getString("coco.mongo-db-name");
  public static final String COCO_INTERNAL_X_API_KEY = conf.getString("coco.internal-x-api-key");

  //coco
  public static final String COCO_URL = conf.getString("coco.domain");
  public static final String COCO_URL_2 = conf.getString("coco.domain2");
  public static final String COCO_URL_3 = conf.getString("coco.domain3");

  public static final String COCO_PERF_NGIN = conf.getString("coco.perf-ngin");
  public static final String COCO_PERF_NGIN_INTERNAL = conf.getString("coco.perf-ngin-internal");
  public static final String COCO_CUSTOMERS = conf.getString("coco.customers");
  public static final String COCO_SHARE_ACCOUNT = conf.getString("coco.share-account");
  public static final String COCO_ORDERS = conf.getString("coco.orders");
  public static final String COCO_ACCOUNT_ORDERS = conf.getString("coco.account-orders");
  public static final String COCO_HISTORY = conf.getString("coco.history");
  public static final String COCO_SUMMARY = conf.getString("coco.summary");

  public static final String COCO_STATS = conf.getString("coco.stats");
  public static final String FUT = conf.getString("coco.performance-customers-future");
  public static final String RETURN = conf.getString("coco.return");
  public static final String RISK = conf.getString("coco.risk");

  public static final String COCO_PERFORMANCE = conf.getString("coco.performance");
  public static final String COCO_INDICATORS = conf.getString("coco.indicators");
  public static final String COCO_PNL = conf.getString("coco.pnl");
  public static final String COCO_SUMMARY_METRICS = conf.getString("coco.summary-metrics");

  public static final String COCO_PORTFOLIO = conf.getString("coco.portfolio");
  public static final String COCO_ACCOUNT_PORTFOLIO = conf.getString("coco.account-portfolio");

  public static final String COCO_PUBLIC_STATUS = conf.getString("coco.public-status");
  public static final String COCO_ENABLE = conf.getString("coco.enable");
  public static final String COCO_DISABLE = conf.getString("coco.disable");
  public static final String COCO_ACCOUNT_INDICES = conf.getString("coco.account-indices");
  public static final String COCO_ACCOUNT_PERFORMANCE = conf.getString("coco.account-performance");

  public static final String COCO_ACCOUNT_RELATION_EXTERNAL = conf.getString("coco.account-relation-external");
  public static final String COCO_ACCOUNT_RELATION_INTERNAL = conf.getString("coco.account-relation-internal");
  public static final String COCO_ACCOUNT_RELATION_BACKEND = conf.getString("coco.account-relation-backend");

  public static final String COCO_SHARE = conf.getString("coco.share");
  public static final String COCO_COPIERS = conf.getString("coco.copiers");
  public static final String COCO_COPY = conf.getString("coco.copy");
  public static final String COCO_TRADERS = conf.getString("coco.traders");
  public static final String COCO_COPIER = conf.getString("coco.copier");

  public static final String COCO_COPYING_ASSET = conf.getString("coco.copying-asset");
  public static final String COCO_COPYING_HISTORY = conf.getString("coco.copying-history");
  public static final String COCO_FOLLOWING_TRADERS = conf.getString("coco.following-traders");
  public static final String COCO_CHART = conf.getString("coco.chart");
  public static final String COCO_TOPS = conf.getString("coco.tops");
  public static final String COCO_BASIC_INFO = conf.getString("coco.basic-info");
  public static final String COCO_PROFILE_INDEX = conf.getString("coco.profile-index");
  public static final String COCO_PROFILE_SUMMARY = conf.getString("coco.profile-summary");
  public static final String COCO_VIEW_INDEX = conf.getString("coco.view-index");
  public static final String COCO_FILTER = conf.getString("coco.filter");
  public static final String COCO_VERIFY_COPY = conf.getString("coco.verify-copy");
  public static final String COCO_CLEAR_CACHE = conf.getString("coco.clear-cache");
  public static final String COCO_STRATEGY = conf.getString("coco.strategy");
  public static final String COCO_VERIFY_STOP = conf.getString("coco.verify-stop");
  public static final String COCO_ACTIVATE_TRADER = conf.getString("coco.activate-trader");
  public static final String COCO_RATING = conf.getString("coco.rating");
  public static final String COCO_FEE_CONFIG = conf.getString("coco.fee-config");
  public static final String COCO_CONFIGS = conf.getString("coco.configs");

  public static final String COCO_ORDER_HISTORIES = conf.getString("coco.order-histories");
  public static final String COCO_CRITERIA_GROUPS = conf.getString("coco.criteria-groups");
  public static final String COCO_RANKING = conf.getString("coco.ranking");
  public static final String COCO_RANKS = conf.getString("coco.ranks");

  public static final String FLEX_URL = conf.getString("coco.flex-domain");
  public static final String FLEX_NOTOKEN_URL = conf.getString("coco.flex-notoken-domain");
  public static final String FLEX_ACCOUNTS = conf.getString("coco.accounts");
  public static final String FLEX_CASH_INVESTMENTS = conf.getString("coco.cash-investments");
  public static final String FLEX_SE = conf.getString("coco.se");

  public static final String KREMA_URL = conf.getString("coco.krema-domain");

  public static final String COCO_WHITELIST = conf.getString("coco.whitelist");
  public static final String COCO_RELOAD_CACHE = conf.getString("coco.reload-cache");

  public static final String HFC_URL = conf.getString("coco.hfc-domain");
  public static final String HFC_TRANS_HIST = conf.getString("coco.trans-hist");
  public static final String CASH_STATEMENTS = conf.getString("coco.cash-statements");
  public static final String STOCK_STATEMENTS = conf.getString("coco.stock-statements");
  public static final String CASH_BOD_BALANCE = conf.getString("coco.cash-bod-balance");
  public static final String ACCOUNTS = conf.getString("coco.accounts");
  public static final String SE = conf.getString("coco.se");
  public static final String CASH_INVESTMENT = conf.getString("coco.cash-investment");

  public static final String COCO_KAFKA_SERVER = conf.getString("coco.kafka.server");
  public static final String COCO_KAFKA_USER = conf.getString("coco.kafka.user");
  public static final String COCO_KAFKA_PASS = conf.getString("coco.kafka.pass");

  public static final String COCO_TRADER_UTILS_JOIN_TIME = conf.getString("coco.trader-utils-join-time");
}
