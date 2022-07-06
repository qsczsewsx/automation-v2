package com.tcbs.automation.config.poseidon;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class PoseidonConfig {
  private static final Config conf = new ConfigImpl("poseidon").getConf();
  public static final String CREATE_TICKER_NOTE = conf.getString("poseidon.createTickerNote");
  public static final String UPDATE_TICKER_NOTE = conf.getString("poseidon.updateTickerNote");
  public static final String DELETE_TICKER_NOTE = conf.getString("poseidon.deleteTickerNote");
  public static final String GET_TICKER_RECENTLY_SEARCH = conf.getString("poseidon.getTickerSearchRecently");
  public static final String DELETE_TICKER_RECENTLY_SEARCH = conf.getString("poseidon.deleteTickerSearchRecently");
  public static final String GET_ALL_TICKER_NOTE = conf.getString("poseidon.getAllTickerNote");

  public static final String CATEGORIES = conf.getString("poseidon.categories");
  public static final String CATEGORY = conf.getString("poseidon.category");
  public static final String CATEGORY_INTERNAL = conf.getString("poseidon.categoryInt");
  public static final String CATEGORIES_INTERNAL = conf.getString("poseidon.categoriesInternal");
  public static final String CATEGORY_NAME = conf.getString("poseidon.categoryName");
  public static final String CATEGORY_CODE = conf.getString("poseidon.categoryCode");
  public static final String CATEGORY_CODE_INTERNAL = conf.getString("poseidon.categoryCodeInt");
  public static final String X_API_KEY = conf.getString("poseidon.xApiKey");

  public static final String PROFILES_LOGIN = conf.getString("profiles.login");

}
