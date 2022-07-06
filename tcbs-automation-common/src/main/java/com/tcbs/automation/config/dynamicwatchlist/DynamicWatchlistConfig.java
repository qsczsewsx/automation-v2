package com.tcbs.automation.config.dynamicwatchlist;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class DynamicWatchlistConfig {
  private static final Config conf = new ConfigImpl("dynamicwatchlist").getConf();
  public static final String WATCHLIST_URL = conf.getString("dynamicwatchlist.url");
  public static final String LIGO_WATCHLIST = conf.getString("dynamicwatchlist.ligo-watchlist");
  public static final String PREVIEW_WATCHLIST = conf.getString("dynamicwatchlist.preview");
  public static final String ALL_WATCHLIST = conf.getString("dynamicwatchlist.all");
  public static final String TOKEN = conf.getString("dynamicwatchlist.token");
  public static final String LIGO_WARNING = conf.getString("dynamicwatchlist.ligo-warning");
  public static final String MOKSHA_EXTERNAL_DOMAIN = conf.getString("moksha.external_domain");
  public static final String MOKSHA_GET_NOTIFICATION_PATH = conf.getString("moksha.notification");
  public static final String MOKSHA_RELOAD_CACHE_PATH = conf.getString("moksha.reload-cache");
  public static final String MOKSHA_CONSUME_QUEUE = conf.getString("moksha.consume-queue");
  public static final String LIGO_QUEUE_CW = conf.getString("ligoqueue.cw_queue");
  public static final String LIGO_QUEUE_WATCHLIST = conf.getString("ligoqueue.watchlist_queue");
  public static final String DOMAIN = conf.getString("dynamicwatchlist.domain");
  public static final String POSEIDON = conf.getString("dynamicwatchlist.poseidon");
  public static final String ASSET_HUB = conf.getString("dynamicwatchlist.asset-hub");
  public static final String DOMAIN_CW = conf.getString("dynamicwatchlist.domain_cw");
  public static final String CW = conf.getString("dynamicwatchlist.cw");
  public static final String TEMPLATE = conf.getString("dynamicwatchlist.template");
  public static final String DOMAIN_WATCHLIST = conf.getString("dynamicwatchlist.domain_watchlist");
  public static final String TICKER_SNAPS = conf.getString("dynamicwatchlist.tickerSnaps");
  public static final String CONDITIONS = conf.getString("dynamicwatchlist.conditions");
  public static final String OPTIONS = conf.getString("dynamicwatchlist.options");
  public static final String DEFAULT = conf.getString("dynamicwatchlist.default");
  public static final String ADD_TICKERS = conf.getString("dynamicwatchlist.add-tickers");
  public static final String DOMAIN_POSEIDON = conf.getString("dynamicwatchlist.domain_poseidon");
  public static final String CATEGORY = conf.getString("dynamicwatchlist.category");
  public static final String DATA_BY_TICKER = conf.getString("dynamicwatchlist.data-by-tickers");
  public static final String INBOX = conf.getString("dynamicwatchlist.inbox");
  public static final String MESSAGE = conf.getString("dynamicwatchlist.message");
  public static final String EXPORT = conf.getString("dynamicwatchlist.export-data");

  public static final String ELLIOT = conf.getString("dynamicwatchlist.elliot");
  public static final String MANUAL_RELOAD_CACHE = conf.getString("dynamicwatchlist.manual-reload-cache");
  public static final String ELLIOT_B = conf.getString("dynamicwatchlist.elliot_b");
  public static final String MANUAL_OVERWRITE_ANALYSIS = conf.getString("dynamicwatchlist.manual-overwrite-analysis");
  public static final String GET_CHART = conf.getString("dynamicwatchlist.get-chart");
  public static final String COUNT_TICKERS = conf.getString("dynamicwatchlist.count-tickers");
  public static final String QUEUE_NAME = conf.getString("dynamicwatchlist.queue");
}
