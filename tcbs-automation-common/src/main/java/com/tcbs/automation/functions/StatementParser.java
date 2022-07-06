package com.tcbs.automation.functions;

import com.tcbs.automation.config.istock.IStockConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatementParser {
  public static HashMap parse(String statement) {
    return parse(statement, "\\|");
  }

  public static HashMap parse(String statement, String delimiter) {
    HashMap result = new HashMap();
    if (StringUtils.isBlank(statement)) {
      return result;
    }

    String[] items = statement.split(delimiter);
    for (String item : items) {
      String[] pairs = item.split("=");
      result.put(pairs[0].trim(), pairs[1].trim());
    }
    return result;
  }

  public static List<Triple<String, String, String>> parseWithOperator(String statement) {
    return parseWithOperator(statement, "\\|");
  }

  public static List<Triple<String, String, String>> parseWithOperator(String statement, String delimiter) {
    List<Triple<String, String, String>> result = new ArrayList<>();
    if (StringUtils.isBlank(statement)) {
      return result;
    }

    String[] items = statement.split(delimiter);
    for (String item : items) {
      pairs(result, item);
    }
    return result;
  }

  static void pairs(List<Triple<String, String, String>> result, String item) {
    String[] pairs;

    if (item.contains(IStockConstants.OPERATOR_LESS_EQUAL)) {
      pairs = item.split(IStockConstants.OPERATOR_LESS_EQUAL);
      result.add(Triple.of(pairs[0].trim(), IStockConstants.OPERATOR_LESS_EQUAL, pairs[1].trim()));
    }
    if (item.contains(IStockConstants.OPERATOR_GREATER_EQUAL)) {
      pairs = item.split(IStockConstants.OPERATOR_GREATER_EQUAL);
      result.add(Triple.of(pairs[0].trim(), IStockConstants.OPERATOR_GREATER_EQUAL, pairs[1].trim()));
    }
    if (item.contains(IStockConstants.OPERATOR_EQUAL)) {
      pairs = item.split(IStockConstants.OPERATOR_EQUAL);
      result.add(Triple.of(pairs[0].trim(), IStockConstants.OPERATOR_EQUAL, pairs[1].trim()));
    }
    if (item.contains(IStockConstants.OPERATOR_GREATER)) {
      pairs = item.split(IStockConstants.OPERATOR_GREATER);
      result.add(Triple.of(pairs[0].trim(), IStockConstants.OPERATOR_GREATER, pairs[1].trim()));
    }
    if (item.contains(IStockConstants.OPERATOR_LESS)) {
      pairs = item.split(IStockConstants.OPERATOR_LESS);
      result.add(Triple.of(pairs[0].trim(), IStockConstants.OPERATOR_LESS, pairs[1].trim()));
    }
    if (item.contains(IStockConstants.OPERATOR_CONTAINS)) {
      pairs = item.split(IStockConstants.OPERATOR_CONTAINS);
      result.add(Triple.of(pairs[0].trim(), IStockConstants.OPERATOR_CONTAINS, pairs[1].trim()));
    }
    if (item.contains(IStockConstants.OPERATOR_COUNT)) {
      pairs = item.split(IStockConstants.OPERATOR_COUNT);
      result.add(Triple.of(pairs[0].trim(), IStockConstants.OPERATOR_COUNT, pairs[1].trim()));
    }
  }
}
