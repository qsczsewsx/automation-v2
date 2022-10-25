package com.automation.tools;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
  public static final String ISO_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
  public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
  public static final String ISO_MONTH_FORMAT = "yyyy-MM";
  public static final String ISO_MONTH_FORMAT_2 = "MM/yyyy";
  public static final String SHORT_DATE_FORMAT = "dd/MM/yy";
  public static final String SHORT_DATE_FORMAT_2 = "dd/MM/yyyy";
  public static final String SHORT_DATE_FORMAT_3 = "yyyyMMdd";
  public static final String SHORT_DATE_FORMAT_4 = "dd-MM-yyyy";
  public static final String ISO_DATE_NO_TIMEZONE = "yyyy-MM-dd HH:mm:ss";
  public static final String TIMEZONE_VN = "Asia/Ho_Chi_Minh";
  public static final String ORACLE_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
  public static final String DATE_WITH_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final String FORMAT_FLOAT_NUMBER_REGEX = "[+-]?([0-9]*[.])?[0-9]+";

  public static Date getNowDateWithoutTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }

  public static boolean isStartDateOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_MONTH) == 1;
  }

  public static Date toDate(String source, String format) throws ParseException {
    if (StringUtils.isEmpty(format)) {
      format = ISO_DATE_FORMAT;
    }

    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.parse(source);
  }

  public static String toISOStringWithoutTime(Date date, String format) {
    if (date == null) {
      return null;
    } else {
      if (StringUtils.isEmpty(format)) {
        format = ISO_DATE_FORMAT;
      }

      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
    }
  }

  public static String toString(Date date, String format) {
    if (date == null) {
      return null;
    } else {
      if (StringUtils.isEmpty(format)) {
        format = ISO_DATE_FORMAT;
      }

      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
    }
  }

  public static Date getStartDateOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    return calendar.getTime();
  }

  public static String toMonthString(Date date, String format) {
    if (date == null) {
      return null;
    } else {
      if (StringUtils.isEmpty(format)) {
        format = ISO_MONTH_FORMAT;
      }

      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
    }
  }

  /**
   * add year
   *
   * @param date   start date
   * @param amount year amount
   * @return Date
   */
  public static Date addYear(Date date, int amount) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.YEAR, amount);
    return c.getTime();
  }

  /**
   * add month
   *
   * @param date   start date
   * @param amount month amount
   * @return Date
   */
  public static Date addMonth(Date date, int amount) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MONTH, amount);
    return c.getTime();
  }


  /**
   * add date
   *
   * @param date   start date
   * @param amount date amount
   * @return Date
   */
  public static Date addDate(Date date, int amount) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.DATE, amount);
    return c.getTime();
  }

  public static int getMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.MONTH) + 1; // zero based
  }

  public static int getHour(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.HOUR_OF_DAY);
  }

  public static Date getEndOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    return cal.getTime();
  }

  public static Date getEndOfMonth(String month) throws ParseException {
    Date startDateOfMonth = toDate(month + "-01", DateUtils.ISO_DATE_FORMAT);
    return DateUtils.getEndOfMonth(startDateOfMonth);
  }

  public static Date getEndTimeOfDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }

  public static Date removeTime(Date date) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  public static Date removeMs(Date date) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }


}
