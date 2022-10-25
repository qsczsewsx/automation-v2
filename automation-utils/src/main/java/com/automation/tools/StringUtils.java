package com.automation.tools;

import javax.sql.rowset.serial.SerialClob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtils {
  private static final String REGEX_NUMBER = "^[0-9]$|^\\-[0-9]$|^[1-9][0-9]*$|^\\-[1-9][0-9]*$|^[0-9]\\.[0-9]{1,}$|^\\-[0-9]\\.[0-9]{1,}$|^[1-9][0-9]*\\.[0-9]{1,}$|^\\-[1-9][0-9]*\\.[0-9]{1,}$";
  private static final String REGEX_CURRENCY = "^[0-9]$|^[1-9][0-9]+$|^(?!0\\.00)[1-9][0-9]{0,2}(,[0-9]{3})*(\\.[0-9][0-9])?$|^[1-9][0-9]*(\\.[0-9][0-9])?$|^[0-9](\\.[0-9][0-9])?$";
  private static final String BRACKET_OPEN = "[";
  private static final String BRACKET_CLOSE = "]";
  private static final String FORMAT_CURRENCY_VN = "###,###";
  private static final String FORMAT_CURRENCY = "###,###.##";
  private static final String GET = "get";

  public StringUtils() {
  }

  public static boolean isNumber(String value) {
    return UtilsClass.isNOTNullEmpty(value) ? value.matches(
      "^[0-9]$|^\\-[0-9]$|^[1-9][0-9]*$|^\\-[1-9][0-9]*$|^[0-9]\\.[0-9]{1,}$|^\\-[0-9]\\.[0-9]{1,}$|^[1-9][0-9]*\\.[0-9]{1,}$|^\\-[1-9][0-9]*\\.[0-9]{1,}$") : false;
  }

  public static boolean isInteger(String value) {
    return UtilsClass.isNOTNullEmpty(value) && value.contains(".") ? false : isNumber(value);
  }

  public static boolean isCurrency(String value) {
    return UtilsClass.isNOTNullEmpty(value) ? value.matches(
      "^[0-9]$|^[1-9][0-9]+$|^(?!0\\.00)[1-9][0-9]{0,2}(,[0-9]{3})*(\\.[0-9][0-9])?$|^[1-9][0-9]*(\\.[0-9][0-9])?$|^[0-9](\\.[0-9][0-9])?$") : false;
  }

  public static String formatDate(Date date, String pattern) {
    return UtilsClass.isNOTNullEmpty(pattern) ? (new SimpleDateFormat(pattern)).format(date) : "";
  }

  public static String formatDateExactlyPattern(Date date, String pattern) {
    if (UtilsClass.isNOTNullEmpty(pattern)) {
      DateFormat format = new SimpleDateFormat(pattern);
      format.setLenient(false);
      return format.format(date);
    } else {
      return "";
    }
  }

  public static String cutBracketMark(String value, boolean isFirst, boolean isEnd) {
    if (UtilsClass.isNOTNullEmpty(value)) {
      if (isFirst && "[".equals(value.substring(0, 1))) {
        value = value.substring(1, value.length());
      }

      if (isEnd && "]".equals(value.substring(value.length() - 1))) {
        value = value.substring(0, value.length() - 1);
      }

      return value;
    } else {
      return null;
    }
  }

  public static String addBracketMark(String value, boolean isFirst, boolean isEnd) {
    if (UtilsClass.isNOTNullEmpty(value)) {
      StringBuilder builder = new StringBuilder(value);
      if (isFirst) {
        builder.insert(0, "[");
      }

      if (isEnd) {
        builder.insert(builder.length(), "]");
      }

      return builder.toString();
    } else {
      return value;
    }
  }

  public static String trim(String value) {
    if (UtilsClass.isNOTNullEmpty(value)) {
      value = value.trim();
    }

    return value;
  }

  public static String getValueByKey(Map<?, ?> map, Object key) {
    if (UtilsClass.isNOTNullEmpty(map)) {
      Iterator var2 = map.entrySet().iterator();

      while (var2.hasNext()) {
        Entry<?, ?> entry = (Entry) var2.next();
        if (entry.getKey().equals(key)) {
          return (String) entry.getValue();
        }
      }
    }

    return null;
  }

  public static String currencyToString(String value) {
    if (UtilsClass.isNullOrEmpty(value)) {
      return null;
    } else {
      return isCurrency(value) ? value.replace(",", "") : "0";
    }
  }

  public static String formatCurrencyEdittion(String value) {
    if (UtilsClass.isNOTNullEmpty(value)) {
      return value.contains(".") ? formatCurrency(value) : formatCurrencyVN(value);
    } else {
      return value;
    }
  }

  public static String formatCurrencyVN(BigDecimal value) {
    String money = "";
    if (value != null) {
      DecimalFormat format = new DecimalFormat("###,###");
      money = format.format(value);
    }

    return money;
  }

  public static String formatCurrencyVN(String value) {
    return isNumber(value) ? formatCurrencyVN(UtilsClass.stringToBigDecimal(value)) : value;
  }

  public static String formatCurrency(BigDecimal value) {
    String money = "";
    if (value != null) {
      DecimalFormat format = new DecimalFormat("###,###.##");
      money = format.format(value);
    }

    return money;
  }

  public static String formatCurrency(String value) {
    return isNumber(value) ? formatCurrency(UtilsClass.stringToBigDecimal(value)) : value;
  }

  public static String getGetterByFieldName(String fieldName) {
    StringBuilder builder = new StringBuilder();
    if (UtilsClass.isNOTNullEmpty(fieldName)) {
      builder.append("get").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
    }

    return builder.toString();
  }

  public static String appendStringByChar(String str1, String str2, String charC) {
    StringBuilder builder = new StringBuilder();
    builder.append(str1).append(charC).append(str2);
    return builder.toString();
  }

  public static String getSafeValue(String value) {
    return UtilsClass.isNullOrEmpty(value) ? "" : value.trim();
  }

  public static String clobToString(Clob clobObject) throws IOException, SQLException {
    final StringBuilder sb = new StringBuilder();

    final Reader reader = clobObject.getCharacterStream();
    final BufferedReader br = new BufferedReader(reader);

    int b;
    while (-1 != (b = br.read())) {
      sb.append((char) b);
    }

    br.close();

    return sb.toString();
  }

  public static Clob stringToClob(String source) throws SQLException {
    return new SerialClob(source.toCharArray());
  }
}