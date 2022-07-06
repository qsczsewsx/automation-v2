package com.tcbs.automation.config.conditionalorder;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ConditionalOrderConfig {
  private static final Config conf = new ConfigImpl("conditionalorder").getConf();

  //Conditional Order
  public static final String CONDITIONAL_DOMAIN = conf.getString("conditional.domain");
  public static final String CONDITIONAL_ORDERS = conf.getString("conditional.orders");
  public static final String ANATTA = conf.getString("conditional.anatta");
  public static final String ANICCA_V1 = conf.getString("conditional.anicca");
  public static final String BULK_UPDATE = conf.getString("conditional.bulkupdate");
  public static final String STOP_ORDER = conf.getString("conditional.stoporder");

  public static final String SAMSARA_FLEX_TRANSACTION_QUEUE = conf.getString("conditional.samsara.flex-transaction-queue");
  public static final String CONDITION_ENGINE_DOMAIN = conf.getString("condition_engine.domain");
  public static final String ANICCA = conf.getString("condition_engine.anicca");
  public static final String REINDEXING = conf.getString("condition_engine.reindexing");

  public static final String STOCK_REF_PRICE = conf.getString("stock_ref_price.url");
  public static final String SAMSARA_PUSH = conf.getString("samsara.url");
  public static final String SAMSARA_PE = conf.getString("samsara.url-get-PE");

  public static final String BACK_STOP_DETAILS = conf.getString("conditional.back_stop_details");
  public static final String BACK_MULTI_DETAILS = conf.getString("conditional.back_multi_details");

  public static final String BACK_ALL = conf.getString("conditional.back_all");

  public static final String BACK_STOP_VALIDATION = conf.getString("conditional.back_stop_validation");
  public static final String BACK_MULTI_VALIDATION = conf.getString("conditional.back_multi_validation");

  public static final String BACK_STOP = conf.getString("conditional.back_stop");
  public static final String BACK_MULTI = conf.getString("conditional.back_multi");

  public static final String MULTI_CONDITION_ORDER = conf.getString("conditional.multiorder");
  public static final String SAMSARA_API_KEY = conf.getString("samsara.api-key");
  public static final String SAMSARA_EVENT_KEY = conf.getString("samsara.event-indexing-key");
  public static final String CRAWL_FLEX_DATA = conf.getString("conditional.crawlflexdata");

  public static final String DRAFT_ORDER = conf.getString("conditional.draftorder");

  public static final String PRE_ORDER = conf.getString("conditional.preorder");
  public static final String STOCK_INFO = conf.getString("stock_info.url");
  public static final String STOCK_NOTOKEN = conf.getString("stock_notoken.url");
  public static final String PPSE = conf.getString("ppse");

  public static final String WARNING = conf.getString("conditional.warning");

  public static final String MANUAL_START_CRAWL = conf.getString("conditional.manualcrawl");
  public static final String NOTIFY_INSTANCES_FLEX_READY = conf.getString("conditional.instancesflexready");

  public static final String WHITE_LIST_IP = conf.getString("whitelist");
}
