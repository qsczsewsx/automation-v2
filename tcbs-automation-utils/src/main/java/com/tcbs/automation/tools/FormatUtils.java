package com.tcbs.automation.tools;

import com.beust.jcommander.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.NumberFormat;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FormatUtils {
  public static final double MARGIN_VALUE = 0.0000000001d;
  public static final String UTF8_ENCODE_FORMAT = "UTF-8";
  private static final Logger logger = LoggerFactory.getLogger(FormatUtils.class);

  /* Lam tron xuong n chu so thap phan */
  public static Double mathFloor(Double number, int decimal) {
    return Math.floor((number + MARGIN_VALUE) * Math.pow(10, decimal)) / Math.pow(10, decimal);
  }

  /* Lam tron theo quy tac ke toan n chu so thap phan */
  public static Double mathRound(Double number, int decimal) {
    if (number == null) {
      return null;
    } else {
      return Math.round(number * Math.pow(10, decimal)) / Math.pow(10, decimal);
    }
  }

  /* Lam tron theo quy tac ke toan n chu so thap phan */
  public static Float mathRoundFloat(Float number, int decimal) {
    if (number == null) {
      return null;
    } else {
      return (float) (Math.round(number * Math.pow(10, decimal)) / Math.pow(10, decimal));
    }
  }

  /**
   * @param input:
   * @param pattern: decimal format: ex: #.#
   * @return
   */
  public static String roundAndFormat(Object input, String pattern) {
    if (input == null) {
      return null;
    }
    DecimalFormat df = new DecimalFormat(pattern);
    df.setRoundingMode(RoundingMode.HALF_UP);
    return df.format(Double.parseDouble(input.toString()));
  }

  public static BigDecimal roundAndFormat(String input, int decimal) {
    if (input == null || StringUtils.equalsIgnoreCase("NULL", input)) {
      return null;
    }
    return new BigDecimal(input).setScale(decimal, RoundingMode.HALF_UP);
  }

  /* Format object */
  public static Object formatNumber(Object object) throws IllegalArgumentException, IllegalAccessException {
    Class<?> objectClass = object.getClass();

    for (Field field : objectClass.getDeclaredFields()) {
      try {

        field.setAccessible(true);
        if (field.get(object) != null && field.isAnnotationPresent(NumberFormat.class)) {
          double value = Double.valueOf((field.get(object)).toString());
          value = Math.round(value * 1000.0) / 1000.0;
          field.set(object, value);
        } else if (field.get(object) != null && field.isAnnotationPresent(Nullable.class)) {
          double value = Math.round(Double.valueOf((field.get(object)).toString()));
          field.set(object, value);

        } else if (field.get(object) != null && field.getType().equals(Double.class)) {
          double value = Double.valueOf((field.get(object)).toString());
          value = Math.round(value * 10.0) / 10.0;
          field.set(object, value);

        }

      } catch (Exception e) {
        logger.error(e.getMessage(), e.getStackTrace());
      }

    }
    return object;
  }

  public static Object roundingToNoPointer(Object object) throws IllegalArgumentException, IllegalAccessException {
    Class<?> objectClass = object.getClass();

    for (Field field : objectClass.getDeclaredFields()) {
      try {

        field.setAccessible(true);

        if (field.get(object) != null && field.isAnnotationPresent(NumberFormat.class)) {
          double value = Double.valueOf((field.get(object)).toString());
          value = Math.round(value * 1000.0) / 1000.0;
          field.set(object, value);
        } else if (field.get(object) != null && field.getType().equals(Double.class)) {
          double value = Math.round(Double.valueOf((field.get(object)).toString()));
          field.set(object, value);

        }

      } catch (Exception e) {
        logger.error(e.getMessage(), e.getStackTrace());
      }

    }
    return object;
  }

  public static Double parseDouble(Object obj) {
    try {
      return Double.parseDouble(obj.toString());
    } catch (Exception e) {
      return null;
    }
  }

  public static Long parseLong(Object obj) {
    try {
      return (long) Double.parseDouble(obj.toString());
    } catch (Exception e) {
      return null;
    }
  }

  public static String syncData(String data) {
    if (data == null) {
      return null;
    }
    if (data.equalsIgnoreCase("empty")) {
      return "";
    }
    if (data.equalsIgnoreCase("null")) {
      return null;
    }
    return data;
  }

  public static String commasNumber(Object obj) {
    try {
      DecimalFormat formatter = new DecimalFormat("#,###");
      return formatter.format(Double.parseDouble(obj.toString()));
    } catch (Exception e) {
      return null;
    }

  }
}
