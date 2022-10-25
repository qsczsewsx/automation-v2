package com.automation.tools;

import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

public class IcalendarUtils {
  public static void checkInputStringParam(RequestSpecification request, String name, String value) {
    if (!value.equalsIgnoreCase("NULL")) {
      request.queryParam(name, value);
    }
  }

  public static void checkInputIntegerParam(RequestSpecification request, String name, Integer value) {
    if (!String.valueOf(value).equalsIgnoreCase("NULL")) {
      request.queryParam(name, value);
    }
  }

  public static void checkBodyStringParam(HashMap<String, Object> request, String name, String value) {
    if (!value.equalsIgnoreCase("NULL")) {
      request.put(name, value);
    }
  }

  public static Integer checkDefaultIntegerParam(String input, Integer defaultValue) {
    Integer output = StringUtils.equalsIgnoreCase("NULL", input) || StringUtils.isEmpty(input) ? defaultValue : Integer.parseInt(input);
    return output;
  }

}