package com.tcbs.automation.stoxplus.stock;

import net.thucydides.core.steps.StepEventBus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.stoxplus.stock.FormatUtils.parseDouble;

public class EntityUtils {
  public static Object setDoubleFieldFromResultSet(Object destination, Map<String, Object> fromDb, Class clazz) {
    for (Field field : clazz.getDeclaredFields()) {
      try {

        field.setAccessible(true);
        if (field.getType().equals(Double.class)) {
          field.set(destination, parseDouble((fromDb.get(field.getName()))));
        }

      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }

    }
    return destination;
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
}