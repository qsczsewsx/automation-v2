package com.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SqlUtils {
  private static Logger logger = LoggerFactory.getLogger(SqlUtils.class);

  public static String getxxxxidIn(List<String> xxxxIds) {
    String xxxxIN = "";
    for (int i = 0; i < xxxxIds.size(); i++) {
      if (i == xxxxIds.size() - 1) {
        xxxxIN += "'" + xxxxIds.get(i) + "'";
      } else {
        xxxxIN += "'" + xxxxIds.get(i) + "'" + ",";

      }
    }

    return xxxxIN;
  }

  public static String getxxxxidListForSql(List<String> xxxxIds, String tableName) {
    String xxxx_id = "";
    int size = xxxxIds.size();
    for (int i = 0; i < size; i++) {
      if (i != size - 1) {
        xxxx_id += "a." + tableName + " = '" + xxxxIds.get(i) + "' OR ";
      } else {
        xxxx_id += "a." + tableName + " = '" + xxxxIds.get(i) + "' ";
      }
    }

    return xxxx_id;
  }

  public static String getxxxxidListForSqlWithKey(List<String> xxxxIds, String tableName, String key) {
    String xxxx_id = "";
    int size = xxxxIds.size();
    for (int i = 0; i < size; i++) {
      if (i != size - 1) {
        xxxx_id += key + "." + tableName + " = '" + String.valueOf(xxxxIds.get(i)) + "' OR ";
      } else {
        xxxx_id += key + "." + tableName + " = '" + String.valueOf(xxxxIds.get(i)) + "' ";
      }
    }

    return xxxx_id;
  }

  /*Function convert String to Date format*/
  public static Date convertStringToDate(String strDate, String formatDate) {
    try {
      return new SimpleDateFormat(formatDate).parse(strDate);
    } catch (ParseException e) {
      logger.error(e.toString());
      return null;
    }
  }
}
