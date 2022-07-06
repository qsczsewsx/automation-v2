package com.tcbs.automation.config.ifutures;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

/**
 * @author ManhNT7
 * Created at 2021-03-15 18:09
 */
public class IFuturesConfig {

  private static final Config conf = new ConfigImpl("ifutures").getConf();

  public static final String TCI3IF_TRADING_DOMAIN = conf.getString("tci3ifutures.iftrading.domain");

  public static final String PLACE_ORDER_API = conf.getString("tci3ifutures.iftrading.apiUrl.place_order");

  public static final String CANCEL_ORDER_API = conf.getString("tci3ifutures.iftrading.apiUrl.cancel_order");

  public static final String CANCEL_ORDER_ALL_API = conf.getString("tci3ifutures.iftrading.apiUrl.cancel_order_all");

  public static final String ORDER_CHANGE_API = conf.getString("tci3ifutures.iftrading.apiUrl.order_change");

  public static final String CASH_BALANCE_API = conf.getString("tci3ifutures.iftrading.apiUrl.cash_balance");

  public static final String STOCK_INFO_API = conf.getString("tci3ifutures.iftrading.apiUrl.stock_info");

  public static final String GET_LIST_CASH_TRANSFER_ALL_API = conf.getString("tci3ifutures.iftrading.apiUrl.get_list_cash_transfer");

  public static final String ACCOUNT_STATUS_API = conf.getString("tci3ifutures.iftrading.apiUrl.account_status");

  public static final String PORTFOLIO_STATUS_API = conf.getString("tci3ifutures.iftrading.apiUrl.portfolio_status");

  public static final String CASH_DEPOSIT_UPDATE_API = conf.getString("tci3ifutures.iftrading.apiUrl.cash_deposit_update");

  public static final String CASH_WITHDRAW_UPDATE_API = conf.getString("tci3ifutures.iftrading.apiUrl.cash_withdraw_update");

  public static final String ACCOUNT_LIST_API = conf.getString("tci3ifutures.iftrading.apiUrl.account_list");

  public static final String ORDER_MATCH_DETAIL_API = conf.getString("tci3ifutures.iftrading.apiUrl.order_match_detail");

  public static final String ORDER_IN_DAY_API = conf.getString("tci3ifutures.iftrading.apiUrl.order_in_day");

  public static final String CASH_TRANSACTION_API = conf.getString("tci3ifutures.iftrading.apiUrl.cash_transaction");

  public static final String CASH_ACCOUNT_INFO_API = conf.getString("tci3ifutures.iftrading.apiUrl.cash_account_info");

  public static final String ACCOUNT_BENEFICIARY_LIST_API = conf.getString("tci3ifutures.iftrading.apiUrl.account_beneficiary_list");

  public static final String ORDER_LIST_API = conf.getString("tci3ifutures.iftrading.apiUrl.order_list");

  public static final String FO_DOMAIN = conf.getString("tci3ifutures.fo.domain");

  public static final String FO_URL = conf.getString("tci3ifutures.fo.apiUrl");

  public static final String FO_X_API_KEY = conf.getString("tci3ifutures.fo.x_api_key");
}
