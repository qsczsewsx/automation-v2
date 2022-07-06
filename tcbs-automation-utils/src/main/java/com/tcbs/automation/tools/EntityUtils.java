package com.tcbs.automation.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.tcbs.automation.tca.tcdata.TcDataStox;

import static com.tcbs.automation.tools.FormatUtils.parseDouble;
import static com.tcbs.automation.tools.FormatUtils.parseLong;

public class EntityUtils {
  private static final Logger logger = LoggerFactory.getLogger(EntityUtils.class);
  private static final String SELECT = " SELECT ";
  private static final String FROM = " FROM ";
  private static final String WHERE = " WHERE ";
  private static final String EXCHANGE = "ExchangeID";
  private static final String UPCOM = "upcom";
  private static final String EQUAL_TICKER = " = (:ticker) ";
  private static final String LIKE_TICKERS = " IN (:tickers) ";
  private static final String PARAM_TICKER = "ticker";
  private static final String PARAM_ORGAN = "organcode";
  private static final String PARAM_DATE = "trade_date";
  private static final String PARAM_QUARTER = "length_report";
  private static final String PARAM_YEAR = "year_report";
  private static final String COMPANY_INFO = "stx_cpf_Organization";
  private static final String CASH_DIVIDEND = "stx_cpa_CashDividendPayout";
  private static final String TREASURY_STOCK = "stx_cpa_TreasuryStock";
  private static final String SHARE_ISSUE = "stx_cpa_ShareIssue";

  public static Object setDoubleFieldFromResultSet(Object destination, Map<String, Object> fromDb, Class clazz) {
    for (Field field : clazz.getDeclaredFields()) {
      try {
        field.setAccessible(true);
        if (field.getType().equals(Double.class)) {
          field.set(destination, parseDouble((fromDb.get(field.getName()))));
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e.getStackTrace());
      }
    }
    return destination;
  }

  public static Object setFieldFromResultSet(Object destination, Map<String, Object> fromDb, Class clazz) {
    for (Field field : clazz.getDeclaredFields()) {
      try {
        field.setAccessible(true);
        if (fromDb.get(field.getName()) == null) {
          continue;
        }
        if (field.getType().equals(Double.class)) {
          field.set(destination, parseDouble((fromDb.get(field.getName()))));
        } else if (field.getType().equals(Long.class)) {
          field.set(destination, parseLong((fromDb.get(field.getName()))));
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e.getStackTrace());
      }
    }
    return destination;
  }

  public static Object setDoubleFieldFromResultSetWithoutNull(Object destination, Map<String, Object> fromDb) {
    for (Field field : destination.getClass().getDeclaredFields()) {
      try {
        field.setAccessible(true);
        if (field.getType().equals(Double.class) && fromDb.get(field.getName()) != null) {
          field.set(destination, parseDouble((fromDb.get(field.getName()))));
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
    return destination;
  }

  public static void devidedToBillion(Object destination) {
    for (Field field : destination.getClass().getDeclaredFields()) {
      try {
        field.setAccessible(true);
        if (field.getType().equals(Double.class)) {
          Double value = (Double) field.get(destination);
          if (value != null) {
            BigDecimal bd = BigDecimal.valueOf(value / 1000000000d).setScale(0, RoundingMode.HALF_UP);
            field.set(destination, bd.doubleValue());
          }
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

  public static Map<Long, Object> toMapUseQuarterAndYear(List<?> fromDb) throws IllegalAccessException,
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    Class clazz = fromDb.get(0).getClass();
    Long year;
    Long quarter;
    Map<Long, Object> map = new HashMap<>();
    for (Object report : fromDb) {
      year = (Long) clazz.getMethod("getYear").invoke(report);
      quarter = (Long) clazz.getMethod("getQuarter").invoke(report);
      map.put(year * quarter, report);
    }
    return map;
  }

  public static Double calculateGrowth(Number recent, Number past) {
    return (past != null && recent != null && past.doubleValue() > 0 && recent.doubleValue() > 0)
      ? ((recent.doubleValue() / past.doubleValue()) - 1) : (null);
  }

  /**
   * @param obj
   * @param <T>
   * @return another object with given object's values
   */
  public static <T> T cloneObject(T obj) {
    try {
      T clone = (T) obj.getClass().newInstance();
      for (Field field : obj.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        field.set(clone, field.get(obj));
      }
      return clone;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * @param obj
   * @param <T>
   * @return another object with given object's values not null
   */

  public static <T> T setValuesOf(T obj) {
    try {
      T clone = (T) obj.getClass().newInstance();
      for (Field field : obj.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        if (field.get(obj) != null) {
          field.set(clone, field.get(obj));
        }
      }
      return clone;
    } catch (Exception e) {
      return null;
    }
  }

  public static <T> List<T> convertToModel(List<Map<String, Object>> listMap, Class<T> clazz) {
    List<T> out = new ArrayList<>();
    try {
      for (Map<String, Object> map : listMap) {
        out.add((T) setFieldFromResultSet(clazz.newInstance(), map, clazz));
      }
    } catch (Exception e) {
      logger.error("convert error", e);
    }
    return out;
  }

  public static List<String> stringToList(String in) {
    if (in.trim().equals("")) {
      return new ArrayList<>();
    }
    String delimiter = ",";
    Set<String> validated = new HashSet<>(Arrays.asList(in.replaceAll("\\s+", "").split(delimiter)));
    return new ArrayList<>(validated);
  }

  public static List<Integer> stringToListInt(String in) {
    if (in.trim().equals("")) {
      return new ArrayList<>();
    }
    String delimiter = ",";
    Set<String> validated = new HashSet<>(Arrays.asList(in.replaceAll("\\s+", "").split(delimiter)));
    return validated.stream()
      .map(Integer::parseInt)
      .collect(Collectors.toList());
  }

  public static List<HashMap<String, Object>> getPriceInfo(String ticker, List<String> fields, String from, String to) {
    try {
      String tableName = "";
      String exchange = "";
      List<Map<String, Object>> tickerInfo = TcDataStox.getTickerInfo(ticker);
      if (CollectionUtils.isEmpty(tickerInfo)) {
        return new ArrayList<>();
      }
      if (tickerInfo.get(0).get(EXCHANGE).toString().equals("0")) {
        tableName = "stx_mrk_HoseStock";
        exchange = "hose";
      } else if (tickerInfo.get(0).get(EXCHANGE).toString().equals("1")) {
        tableName = "stx_mrk_HnxStock";
        exchange = "hnx";
      } else if (tickerInfo.get(0).get(EXCHANGE).toString().equals("3")) {
        tableName = "stx_mrk_UpcomStock";
        exchange = UPCOM;
      }
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      Map<String, Object> storage = toStorage(storages);
      fields = validateFields(fields, storage);
      String sql = SELECT + hashToString(getFields(fields, storage))
        + ", (:exchange) as exchange " + FROM + tableName + WHERE
        + storage.get(PARAM_TICKER) + EQUAL_TICKER + " AND "
        + storage.get(PARAM_DATE) + " BETWEEN (:from) AND (:to) ";
      return formatTradeDate(TcDataStox.getStockPrice(sql, ticker, exchange.toUpperCase(), from, to));
    } catch (Exception ex) {
      logger.error("Error while query price info ", ex);
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getMarketInfo(String exchange, List<String> fields
    , String from, String to) {
    try {
      String tableName = "";
      String comgroupcode = "";
      if (exchange.equals("hose")) {
        tableName = "stx_mrk_HoseIndex";
        comgroupcode = "VNINDEX";
      } else if (exchange.equals("hnx")) {
        tableName = "stx_mrk_HnxIndex";
        comgroupcode = "HNXIndex";
      } else if (exchange.equals(UPCOM)) {
        tableName = "stx_mrk_UpcomIndex";
        comgroupcode = "UpcomIndex";
      }
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      Map<String, Object> storage = toStorage(storages);
      fields = validateFields(fields, storage);
      String sql = SELECT + hashToString(getFields(fields, storage))
        + FROM + tableName
        + WHERE + storage.get(PARAM_DATE) + " BETWEEN (:from) AND (:to) "
        + "and comgroupcode = '" + comgroupcode + "' ;";

      Map<String, Object> params = new HashMap<>();
      params.put(FROM, from);
      params.put("to", to);
      return formatTradeDate(TcDataStox.getMarketInfo(sql, from, to));
    } catch (Exception ex) {
      logger.error("Error while query index info ", ex);
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getForeignTrade(String ticker, List<String> fields, String from, String to) {
    try {
      String tableName = "";
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      Map<String, Object> storage = toStorage(storages);
      fields = validateFields(fields, storage);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      String exchange = "";
      List<Map<String, Object>> tickerInfo = TcDataStox.getTickerInfo(ticker);
      if (CollectionUtils.isEmpty(tickerInfo)) {
        return new ArrayList<>();
      }
      if (tickerInfo.get(0).get(EXCHANGE).toString().equals("0")) {
        tableName = "stx_mrk_HoseStock";
        exchange = "hose";
      } else if (tickerInfo.get(0).get(EXCHANGE).toString().equals("1")) {
        tableName = "stx_mrk_HnxStock";
        exchange = "hnx";
      } else if (tickerInfo.get(0).get(EXCHANGE).toString().equals("3")) {
        tableName = "stx_mrk_UpcomStock";
        exchange = UPCOM;
      }
      storage = toStorage(TcDataStox.getFieldsMap(tableName));
      String sql = SELECT + hashToString(getFields(fields, storage))
        + ", (:exchange) as exchange " + FROM + tableName
        + WHERE + storage.get(PARAM_TICKER) + EQUAL_TICKER
        + " AND " + storage.get(PARAM_DATE) + " BETWEEN (:from) AND (:to) ;";
      return formatTradeDate(TcDataStox.getStockPrice(sql, ticker, StringUtils.upperCase(exchange), from, to));
    } catch (Exception ex) {
      logger.error("Error while query foreign trade ", ex);
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getCompanyInfo(List<String> tickers, List<String> fields) {
    try {
      String tableName = COMPANY_INFO;
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      Map<String, Object> storage = toStorage(storages);
      fields = validateFields(fields, storage);
      String sql = SELECT + hashToString(getFields(fields, storage))
        + FROM + tableName
        + WHERE + storage.get(PARAM_TICKER) + LIKE_TICKERS;
      return TcDataStox.getCompanyInfo(sql, tickers);
    } catch (Exception ex) {
      logger.error("Error while query company info ", ex);
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getCashDividend(List<String> tickers, List<String> fields) {
    try {
      String tableName = CASH_DIVIDEND;
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      Map<String, Object> storage = toStorage(storages);
      fields = validateFields(fields, storage);
      String sql = SELECT + hashToString(getFields(fields, storage))
        + FROM + tableName
        + WHERE + storage.get(PARAM_ORGAN) + LIKE_TICKERS;
      return formatTradeDate(TcDataStox.getCompanyInfo(sql, tickers));
    } catch (Exception ex) {
      logger.error("Error while query cash dividend info ", ex);
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getTreasuryStock(List<String> tickers, List<String> fields) {
    try {
      String tableName = TREASURY_STOCK;
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      Map<String, Object> storage = toStorage(storages);
      fields = validateFields(fields, storage);
      String sql = SELECT + hashToString(getFields(fields, storage))
        + FROM + tableName
        + WHERE + storage.get(PARAM_ORGAN) + LIKE_TICKERS;
      return formatTradeDate(TcDataStox.getCompanyInfo(sql, tickers));
    } catch (Exception ex) {
      logger.error("Error while query treasury stock info ", ex);
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getShareIssue(List<String> tickers, List<String> fields) {
    try {
      String tableName = SHARE_ISSUE;
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      Map<String, Object> storage = toStorage(storages);
      fields = validateFields(fields, storage);
      String sql = SELECT + hashToString(getFields(fields, storage))
        + FROM + tableName
        + WHERE + storage.get(PARAM_ORGAN) + LIKE_TICKERS;
      return formatTradeDate(TcDataStox.getCompanyInfo(sql, tickers));
    } catch (Exception ex) {
      logger.error("Error while query share issue ", ex);
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getFundamentalStatement(String ticker, List<String> fields, int from
    , int to, boolean isYearly, String tableName, String industry) {
    try {
      String equalSign = !isYearly ? "<" : "=";
      List<Map<String, Object>> storages = TcDataStox.getFieldsMap(tableName);
      if (CollectionUtils.isEmpty(storages)) {
        return new ArrayList<>();
      }
      Map<String, Object> storage = toStorage(storages);
      String quarterParam = storage.get(PARAM_QUARTER).toString();
      String yearParam = storage.get(PARAM_YEAR).toString();
      fields = validateFields(fields, storage);
      String sql = buildFundamentalQuery(fields, tableName, quarterParam, equalSign, yearParam, storage);
      return TcDataStox.getFundamentalStatement(sql, ticker, industry, from, to);
    } catch (Exception ex) {
      logger.error("Error while query fundamental ", ex);
    }
    return new ArrayList<>();
  }

  public static String buildFundamentalQuery(List<String> fields, String tableName, String quarterParam, String equalSign, String yearParam, Map<String, Object> storage) {
    return SELECT + hashToString(getFields(fields, storage))
      + FROM + tableName
      + WHERE + quarterParam + " " + equalSign + " 5 AND "
      + storage.get(PARAM_TICKER) + EQUAL_TICKER
      + " AND ((" + yearParam + " * 10 +" + quarterParam + " >= (:from)) "
      + " AND (" + yearParam + " * 10 +" + quarterParam + " <= (:to) ))";
  }

  private static List<String> validateFields(List<String> fields, Map<String, Object> storage) {
    Set<String> validated = new HashSet<>(fields);
    if (CollectionUtils.isEmpty(validated)) {
      validated = new HashSet<>(storage.keySet());
    }
    validated.add(PARAM_TICKER);
    validated.add(PARAM_DATE);
    validated.add(PARAM_QUARTER);
    validated.add(PARAM_YEAR);
    return new ArrayList<>(validated);
  }

  public static String hashToString(Map<String, String> hashed) {
    String format = "%s as [%s]";
    StringBuilder sb = new StringBuilder();
    String delimiter = "";
    for (Entry<String, String> entry : hashed.entrySet()) {
      sb.append(delimiter);
      sb.append(String.format(format, entry.getValue(), entry.getKey()));
      delimiter = ",";
    }
    return sb.toString();
  }

  public static Map<String, String> getFields(List<String> fields, Map<String, Object> storage) {
    Map<String, String> stox_fields = new HashMap<>();
    for (String field : fields) {
      Object key = storage.get(field);
      if (key == null) {
        continue;
      }
      stox_fields.put(field, key.toString());
    }
    return stox_fields;
  }

  public static Map<String, Object> toStorage(List<Map<String, Object>> storages) {
    Map<String, Object> storage = new HashMap<>();
    for (Map<String, Object> item : storages) {
      storage.put(item.get("field_name").toString(), item.get("stox_name"));
    }
    return storage;
  }

  public static List<HashMap<String, Object>> formatTradeDate(List<HashMap<String, Object>> storages) {
    String pattern = "MMM d, yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    for (Map<String, Object> rs : storages) {
      for (Entry<String, Object> item : rs.entrySet()) {
        if ((item.getKey().contains("date") || item.getKey().contains("time")) && item.getValue() != null) {
          item.setValue(simpleDateFormat.format((Date) item.getValue()));
        }
      }
    }
    return storages;
  }
}