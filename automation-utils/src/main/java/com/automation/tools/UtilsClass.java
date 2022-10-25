package com.automation.tools;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UtilsClass {
  public static final String COMMA = ",";
  public static final String DOT = ".";
  private static final String TRUE = "TRUE";
  private static final String NUMBER_1 = "1";
  private static final String FORMAT_DATE_TIME_CUSTOM = "yyyyMMddHHmmss";

  public UtilsClass() {
  }

  public static List<String> stringToList(String value, boolean cutBracketFirst, boolean cutBracketEnd) {
    List<String> values = new ArrayList();
    if (isNOTNullEmpty(value)) {
      value = value.trim();
      if (cutBracketFirst) {
        value = cutBracketMark(value, true, false);
      }

      if (cutBracketEnd) {
        value = cutBracketMark(value, false, true);
      }

      String[] arr = splitByCharacter(value, ",");
      values = arrayToList(arr, true);
    }

    return (List) values;
  }

  public static String cutBracketMark(String value, boolean isFirst, boolean isEnd) {
    return StringUtils.cutBracketMark(value, isFirst, isEnd);
  }

  public static String addBracketMark(String value, boolean isFirst, boolean isEnd) {
    return StringUtils.addBracketMark(value, isFirst, isEnd);
  }

  public static String[] splitByCharacter(String value, String character) {
    String[] arr = null;
    if (isNOTNullEmpty(value) && character != null) {
      arr = value.split(character);
    }

    return arr;
  }

  public static List<String> arrayToList(String[] arr, boolean isTrim) {
    List<String> values = new ArrayList();
    if (isNOTNullEmpty((Object[]) arr)) {
      if (isTrim) {
        arr = trimElementArray(arr);
      }

      for (int i = 0; i < arr.length; ++i) {
        values.add(arr[i]);
      }
    }

    return values;
  }

  public static String[] trimElementArray(String[] arr) {
    if (isNOTNullEmpty((Object[]) arr)) {
      for (int i = 0; i < arr.length; ++i) {
        arr[i] = arr[i] == null ? arr[i] : arr[i].trim();
      }
    }

    return arr;
  }

  public static String trim(String value) {
    return StringUtils.trim(value);
  }

  public static List<?> addElementToList(List<?> values, Object element) {
    List<Object> objects = null;
    if (isNOTNullEmpty(values)) {
      objects = new ArrayList(values);
      objects.add(element);
    }

    return objects;
  }

  public static List<?> addElementToListNotCheck(List<?> values, Object element) {
    List<Object> objects = null;
    if (isNOTNullEmpty(values)) {
      objects = new ArrayList(values);
    } else {
      objects = new ArrayList();
    }

    objects.add(element);
    return objects;
  }

  public static List<?> addAllListToList(List<?> fromList, List<?> toList, boolean duplicate) {
    List<Object> objects = new ArrayList();
    if (toList != null) {
      objects = new ArrayList(toList);
      if (isNOTNullEmpty(fromList)) {
        boolean isDuplicate;
        if (duplicate) {
          objects.addAll(fromList);
        } else {
          for (Iterator var4 = fromList.iterator(); var4.hasNext(); isDuplicate = false) {
            Object objFrom = var4.next();
            isDuplicate = false;
            Iterator var7 = toList.iterator();

            while (var7.hasNext()) {
              Object objTo = var7.next();
              if (objFrom.equals(objTo)) {
                isDuplicate = true;
                break;
              }
            }

            if (!isDuplicate) {
              objects.add(objFrom);
            }
          }
        }
      }
    }

    return objects;
  }

  public static boolean isNOTNullEmpty(String value) {
    return value != null && !value.trim().isEmpty();
  }

  public static boolean isNullOrEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  public static boolean isNOTNullEmpty(List<?> values) {
    return values != null && !values.isEmpty();
  }

  public static boolean isNOTNullEmpty(Object o) {
    if (o != null) {
      if (o instanceof String) {
        return !((String) o).trim().isEmpty();
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public static boolean isNOTNullEmpty(Object[] values) {
    return values != null && values.length > 0;
  }

  public static boolean isNOTNullEmptyCheckFirstElement(Object[] values) {
    return values != null && values.length > 0 && values[0] != null;
  }

  public static boolean isNOTNullEmpty(Map<?, ?> map) {
    return map != null && !map.isEmpty();
  }

  public static Boolean isNOTNullEmptyAllElement(Object object) throws Exception {
    if (object == null) {
      return false;
    } else {
      Class c = object.getClass();

      try {
        PropertyDescriptor[] var2 = Introspector.getBeanInfo(c, Object.class).getPropertyDescriptors();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
          PropertyDescriptor propertyDescriptor = var2[var4];
          Method method = propertyDescriptor.getReadMethod();
          if (method.getReturnType().equals(String.class) && isNOTNullEmpty((String) method.invoke(object))) {
            return true;
          }
        }
      } catch (Exception var7) {
        throw new Exception("Error isNOTNullEmptyAllElement : " + var7, var7);
      }

      return false;
    }
  }

  public static boolean isExistsIn(Map<?, ?> map, Object value, boolean compareToKey, boolean compareToValue) {
    boolean hasKey = false;
    boolean hasValue = false;
    if (isNOTNullEmpty(map) && value != null) {
      hasKey = compareToKey ? isExistsInKey(map, value) : false;
      hasValue = compareToValue ? isExistsInValue(map, value) : false;
      if (compareToKey && compareToValue) {
        return hasKey & hasValue;
      }

      if (compareToKey) {
        return hasKey;
      }

      if (compareToValue) {
        return hasValue;
      }
    }

    return false;
  }

  public static boolean isExistsInKey(Map<?, ?> map, Object value) {
    if (isNOTNullEmpty(map) && value != null) {
      Iterator var2 = map.entrySet().iterator();

      Entry entry;
      do {
        if (!var2.hasNext()) {
          return false;
        }

        entry = (Entry) var2.next();
      } while (!value.equals(entry.getKey()));

      return true;
    } else {
      return false;
    }
  }

  public static boolean isExistsInValue(Map<?, ?> map, Object value) {
    if (isNOTNullEmpty(map) && value != null) {
      Iterator var2 = map.entrySet().iterator();

      Entry entry;
      do {
        if (!var2.hasNext()) {
          return false;
        }

        entry = (Entry) var2.next();
      } while (!value.equals(entry.getValue()));

      return true;
    } else {
      return false;
    }
  }

  public static String getValueByKey(Map<?, ?> map, Object key) {
    return StringUtils.getValueByKey(map, key);
  }

  public static <T> List<T> getValuesByKey(Map<?, List<T>> map, Object key) {
    if (isNOTNullEmpty(map)) {
      Iterator var2 = map.entrySet().iterator();

      while (var2.hasNext()) {
        Entry<?, List<T>> entry = (Entry) var2.next();
        if (entry.getKey().equals(key)) {
          return (List) entry.getValue();
        }
      }
    }

    return null;
  }

  public static boolean stringToBoolean(String value) {
    if (isNOTNullEmpty(value)) {
      return "TRUE".equalsIgnoreCase(value) || "1".equals(value);
    } else {
      return false;
    }
  }

  public static boolean isString(Object object) {
    return object instanceof String;
  }

  public static boolean isNumber(String value) {
    return StringUtils.isNumber(value);
  }

  public static boolean isCurrency(String value) {
    return StringUtils.isCurrency(value);
  }

  public static BigDecimal currencyToBigDecimal(String value) {
    return isCurrency(value) ? new BigDecimal(value.replace(",", "")) : new BigDecimal("0");
  }

  public static String currencyToString(String value) {
    return StringUtils.currencyToString(value);
  }

  public static String formatCurrencyEdittion(String value) {
    return StringUtils.formatCurrencyEdittion(value);
  }

  public static String formatCurrencyVN(BigDecimal value) {
    return StringUtils.formatCurrencyVN(value);
  }

  public static String formatCurrencyVN(String value) {
    return StringUtils.formatCurrencyVN(value);
  }

  public static String formatCurrency(BigDecimal value) {
    return StringUtils.formatCurrency(value);
  }

  public static String formatCurrency(String value) {
    return StringUtils.formatCurrency(value);
  }

  public static BigDecimal stringToBigDecimal(String value) {
    return isNumber(value) ? new BigDecimal(value) : new BigDecimal("0");
  }

  public static BigDecimal add(BigDecimal value1, String value2) {
    return value1.add(stringToBigDecimal(value2));
  }

  public static void sortByField(List<?> list, String fieldName, boolean isDesc, String dateTimeFormatOrigin) {
    sort(list, fieldName, isDesc, dateTimeFormatOrigin);
  }

  public static void sortByField(Object[] arr, String fieldName, boolean isDesc, String dateTimeFormatOrigin) {
    List<?> list = null;
    if (isNOTNullEmpty(arr)) {
      list = Arrays.asList(arr);
    }

    sort(list, fieldName, isDesc, dateTimeFormatOrigin);
  }

  public static void sort(List<?> list, String fieldName, final boolean isDesc, final String dateTimeFormatOrigin) {
    if (isNOTNullEmpty(list) && isNOTNullEmpty(fieldName)) {
      final String getter = getGetterByFieldName(fieldName);
      final boolean isCompareDateString = isNOTNullEmpty(dateTimeFormatOrigin);
      final SimpleDateFormat dateFormatCustom = new SimpleDateFormat("yyyyMMddHHmmss");
      Collections.sort(list, new Comparator<Object>() {
        public int compare(Object object1, Object object2) {
          try {
            if (object1 != null && object2 != null) {
              object1 = object1.getClass().getMethod(getter).invoke(object1);
              object2 = object2.getClass().getMethod(getter).invoke(object2);
              if (isCompareDateString && object1 != null && object2 != null) {
                try {
                  SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormatOrigin);
                  object1 = dateFormatCustom.format(dateFormat.parse(object1.toString()));
                  object2 = dateFormatCustom.format(dateFormat.parse(object2.toString()));
                } catch (ParseException var4) {
                  throw new RuntimeException("Format date time error: " + object1 + " and " + object2 + " :" + var4);
                }
              }
            }

            return object1 == null ? -1 : (object2 == null ? 1 : (isDesc ? -1 * ((Comparable) object1).compareTo(object2) : ((Comparable) object1).compareTo(object2)));
          } catch (Exception var5) {
            throw new RuntimeException("Cannot compare " + object1 + " with " + object2 + " on " + getter + " :" + var5, var5);
          }
        }
      });
    }

  }

  public static String getGetterByFieldName(String fieldName) {
    return StringUtils.getGetterByFieldName(fieldName);
  }

  public static boolean isDuplicateInList(List<?> list, Object object) {
    if (isNOTNullEmpty(list) && object != null) {
      Iterator var2 = list.iterator();

      while (var2.hasNext()) {
        Object obj = var2.next();
        if (obj.equals(object)) {
          return true;
        }
      }
    }

    return false;
  }

  public static <T> List<T> asList(T[] source) {
    List<T> objects = null;
    if (isNOTNullEmpty(source)) {
      objects = new LinkedList(Arrays.asList(source));
    }

    return objects;
  }

  public static <T> List<T> asList(T[] source, boolean checkFirstElement) {
    List<T> objects = new LinkedList();
    if (isNOTNullEmpty(source)) {
      if (checkFirstElement) {
        if (source[0] != null) {
          objects = new LinkedList(Arrays.asList(source));
        }
      } else {
        objects = new LinkedList(Arrays.asList(source));
      }
    }

    return objects;
  }

  public static String appendStringByChar(String str1, String str2, String charC) {
    return StringUtils.appendStringByChar(str1, str2, charC);
  }

  public static Double stringToDouble(String value) {
    return isNumber(value) ? Double.valueOf(value) : null;
  }

  public static boolean existCharAtLast(String value, char c) {
    return isNOTNullEmpty(value) && value != null && value.charAt(value.length() - 1) == c;
  }

  public static Document parseXmlToDocument(String xml) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
    dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
    DocumentBuilder db = dbf.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    return db.parse(is);
  }

  public static JSONArray appendArray(JSONArray arr1, JSONArray arr2) {
    JSONArray jsonArray = new JSONArray();
    appendArrayToSource(jsonArray, arr1);
    appendArrayToSource(jsonArray, arr2);
    return jsonArray;
  }

  public static JSONArray appendArrayToSource(JSONArray source, JSONArray extra) {
    if (source != null && extra != null) {
      Iterator var2 = extra.iterator();

      while (var2.hasNext()) {
        Object o = var2.next();
        source.put(o);
      }
    }

    return source;
  }

  public static org.jsoup.nodes.Document parseStringToDocument(String html) throws Exception {
    org.jsoup.nodes.Document document = null;
    if (isNOTNullEmpty(html)) {
      document = Jsoup.parse(html);
    }

    return document;
  }

  public static boolean isNull(Integer value) {
    if (value == null || value <= 0) {
      return true;
    }
    return false;
  }

  public static boolean isNull(String value) {
    if (value == null || value.trim().equals("")) {
      return true;
    }
    return false;
  }

  public static boolean isNull(Float value) {
    if (value == null || value <= 0.0) {
      return true;
    }
    return false;
  }


  public static boolean isNull(List<?> value) {
    if (value == null || value.isEmpty()) {
      return true;
    }
    return false;
  }
}