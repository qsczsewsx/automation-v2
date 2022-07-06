package com.tcbs.automation.stoxplus.stock;

import com.beust.jcommander.internal.Nullable;
import net.thucydides.core.steps.StepEventBus;
import org.springframework.format.annotation.NumberFormat;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FormatUtils {
  public static Object formatNumber(Object object) {
    Class<?> objectClass = object.getClass();
    for (Field field : objectClass.getDeclaredFields()) {
      try {

        field.setAccessible(true);
        if (field.get(object) != null && field.isAnnotationPresent(NumberFormat.class)) {
          DecimalFormat formater = new DecimalFormat();
          formater.setRoundingMode(RoundingMode.HALF_UP);
          formater.applyPattern("#.###");
          String formatValue = formater.format(field.get(object));
          formatValue = formatValue.replaceAll("^-(?=0(\\.0*)?$)", "");
          field.set(object, Double.parseDouble(formatValue));
        } else if (field.get(object) != null && field.isAnnotationPresent(Nullable.class)) {
          double value = Math.round(Double.valueOf((field.get(object)).toString()));
          field.set(object, value);

        } else if (field.get(object) != null && field.getType().equals(Double.class)) {
          double value = Double.valueOf((field.get(object)).toString());
          value = (field.getName().equals("equityOnTotalAsset")) ? Math.round(value * 1000.0d) / 1000.0d : Math.round(value * 10.0) / 10.0;
          field.set(object, value);

        }

      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }

    }
    return object;
  }

  public static Object roundingToNoPointer(Object object) {
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

      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
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

  public static Integer parseInt(Object obj) {
    try {
      return Integer.parseInt(obj.toString());
    } catch (Exception e) {
      return null;
    }
  }
}
