package com.tcbs.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SqlUtils {
  private static Logger logger = LoggerFactory.getLogger(SqlUtils.class);

  public static String getTcbsidIn(List<String> tcbsIds) {
    String tcbsIN = "";
    for (int i = 0; i < tcbsIds.size(); i++) {
      if (i == tcbsIds.size() - 1) {
        tcbsIN += "'" + tcbsIds.get(i) + "'";
      } else {
        tcbsIN += "'" + tcbsIds.get(i) + "'" + ",";

      }
    }

    return tcbsIN;
  }

  public static String getTcbsidListForSql(List<String> tcbsIds, String tableName) {
    String tcbs_id = "";
    int size = tcbsIds.size();
    for (int i = 0; i < size; i++) {
      if (i != size - 1) {
        tcbs_id += "a." + tableName + " = '" + tcbsIds.get(i) + "' OR ";
      } else {
        tcbs_id += "a." + tableName + " = '" + tcbsIds.get(i) + "' ";
      }
    }

    return tcbs_id;
  }

  public static String getTcbsidListForSqlWithKey(List<String> tcbsIds, String tableName, String key) {
    String tcbs_id = "";
    int size = tcbsIds.size();
    for (int i = 0; i < size; i++) {
      if (i != size - 1) {
        tcbs_id += key + "." + tableName + " = '" + String.valueOf(tcbsIds.get(i)) + "' OR ";
      } else {
        tcbs_id += key + "." + tableName + " = '" + String.valueOf(tcbsIds.get(i)) + "' ";
      }
    }

    return tcbs_id;
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
