package com.automation.tools;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CompareUtils {
  public static boolean equalLists(List<String> a, List<String> b) {
    // Check for sizes and nulls
    if (a == null && b == null) {
      return true;
    }
    if ((a == null && b != null) || (a != null && b == null) || (a.size() != b.size())) {
      return false;
    }
    // Sort and compare the two lists
    Collections.sort(a);
    Collections.sort(b);
    return a.equals(b);
  }

  public static final boolean equalsWithNulls(Object a, Object b) {
    if (a == null && b == null) {
      return true;
    }
    if ((a == null && b != null) || (a != null && b == null)) {
      return false;
    }
    return a.equals(b);
  }

  public static boolean isNOTNullEmpty(Object value) {
    if (value == null || value.equals("")) {
      return false;
    } else {
      return true;
    }
  }

  public static boolean isNullOrEmpty(Object value) {
    return !isNOTNullEmpty(value);
  }

  public static boolean equalsString(String value1, String value2) {
    return value1.equals(value2);
  }

  public static boolean isObjEmpty(Object object) {
    for (Field field : object.getClass().getDeclaredFields()) {
      try {
        field.setAccessible(true);
        if (field.get(object) != null) {
          return false;
        }
      } catch (Exception e) {
        System.out.println("Exception occurred in processing");
      }
    }
    return true;
  }

  public static final boolean equalsStringWithNulls(Object a, Object b) {
    if (a == null && b == null) {
      return true;
    }
    if ((a == null && b != null) || (a != null && b == null)) {
      return false;
    }
    return a.toString().equals(b.toString());
  }

  public static boolean compareMap(HashMap<String, Object> actualObj, HashMap<String, Object> expectObj, String... keys) {
    return (equalsStringWithNulls(actualObj.get(keys[0]), expectObj.get(keys[0])) && equalsStringWithNulls(actualObj.get(keys[1])
      , expectObj.get(keys[1])) && equalsStringWithNulls(actualObj.get(keys[2]), expectObj.get(keys[2])));
  }
}
