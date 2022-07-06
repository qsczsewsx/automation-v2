package com.tcbs.automation.coco;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReflexData {
  public static <T> List<T> convertResultSetToObj(List<Object> resultSet, Class<T> type) throws IllegalAccessException, ParseException, NoSuchMethodException, InvocationTargetException, InstantiationException {
    Constructor<?> ctr = type.getConstructor();
    List<Pair<Field, ColumnIndex>> fields = getFieldWithAnnotation(type, ColumnIndex.class);

    List<T> list = new ArrayList<>();
    for (Object rs : resultSet) {
      T obj = (T) ctr.newInstance();

      if (rs instanceof Object[]) {
        Object[] rsMap = (Object[]) rs;
        for (Pair<Field, ColumnIndex> p : fields) {
          if (rsMap.length > p.getRight().index()) {
            setValueToField(obj, p.getLeft(), rsMap[p.getRight().index()], null, p.getRight().ignoreIfNull());
          }
        }
      }
      list.add(obj);
    }
    return list;
  }

  public static <A extends Annotation> List<Pair<Field, A>> getFieldWithAnnotation(Class<?> type, Class<A> annoType) {
    final List<Field> fields = FieldUtils.getFieldsListWithAnnotation(type, annoType);

    List<Pair<Field, A>> rs = new ArrayList<>();
    for (Field field : fields) {
      A anno = field.getAnnotation(annoType);
      rs.add(Pair.of(field, anno));
    }

    return rs;
  }

  public static void setValueToField(Object obj, Field field, Object fieldValue, String fmtIfDate,
                                     boolean ignoreIfNull) throws IllegalAccessException, ParseException {
    if (fieldValue != null) {
      setValueToField(obj, field, fieldValue);
    } else if (!ignoreIfNull) {
      Class t = field.getType();
      if (t.isPrimitive()) {
        String message = String.format("Field %s Can't set null value", field.getName());
        throw new IllegalAccessException(message);
      } else {
        // Set null to field
        field.setAccessible(true);
        field.set(obj, null);
      }
    }
    // With ignoreIfNull is true. Keep value of field
  }

  public static void setValueToField(Object obj, Field field, Object fieldValue)
    throws IllegalAccessException, ParseException {
    field.setAccessible(true);

    if (field.getType() == Long.class) {
      Double db = Double.valueOf(fieldValue.toString());
      field.set(obj, db.longValue());
    } else if (field.getType() == Double.class) {
      Double db = Double.valueOf(fieldValue.toString());
      field.set(obj, db);
    } else if (field.getType() == Integer.class) {
      Double db = Double.valueOf(fieldValue.toString());
      field.set(obj, db.intValue());
    } else if (field.getType() == String.class) {
      field.set(obj, fieldValue.toString());
    } else {
      field.set(obj, fieldValue);
    }
  }
}
