package com.tcbs.automation.hfcdata.icalendar.utils;

import com.tcbs.automation.tools.DateUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.tcbs.automation.tools.DateUtils.TIMEZONE_VN;

public class IcalendarServiceUtils {
  public static void formatRuleFromTo(String timeFrame, StringBuilder fromDate, StringBuilder toDate) throws ParseException {
    Date from = DateUtils.toDate(fromDate.toString(), DateUtils.ISO_DATE_FORMAT);
    Date to = DateUtils.toDate(toDate.toString(), DateUtils.ISO_DATE_FORMAT);
    if (Constants.TIME_FRAME_1Y.equalsIgnoreCase(timeFrame)) {
      from = getStartYear(from);
      to = getEndYear(from);
    } else if (Constants.TIME_FRAME_1M.equalsIgnoreCase(timeFrame)) {
      from = getStartMonth(from);
      to = getEndMonth(from);
    } else if (Constants.TIME_FRAME_1W.equalsIgnoreCase(timeFrame)) {
      from = getStartWeek(from);
      to = getEndWeek(from);
    } else if (Constants.TIME_FRAME_1D.equalsIgnoreCase(timeFrame)) {
      to = from;
    }
    fromDate.delete(0, fromDate.length());
    toDate.delete(0, toDate.length());
    fromDate.append(DateUtils.toString(from, null));
    toDate.append(DateUtils.toString(to, null));
  }

  public static Date getStartYear(Date from) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_VN));
    cal.setTime(from);
    int year = cal.get(Calendar.YEAR);
    LocalDate localDate = LocalDate.of(year, 1, 1);
    ZoneId defaultZoneId = ZoneId.systemDefault();
    return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
  }

  public static Date getEndYear(Date from) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_VN));
    cal.setTime(from);
    int year = cal.get(Calendar.YEAR);
    LocalDate localDate = LocalDate.of(year, 12, 31);
    ZoneId defaultZoneId = ZoneId.systemDefault();
    return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
  }

  public static Date getStartMonth(Date from) {
    LocalDate localDate = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    int year = localDate.getYear();
    int month = localDate.getMonthValue();
    int res = getLastDayOfMonth(from);
    LocalDate localDates = LocalDate.of(year, month, 1);
    ZoneId defaultZoneId = ZoneId.systemDefault();
    return Date.from(localDates.atStartOfDay(defaultZoneId).toInstant());
  }

  public static Date getEndMonth(Date from) {
    LocalDate localDate = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    int year = localDate.getYear();
    int month = localDate.getMonthValue();
    int res = getLastDayOfMonth(from);
    LocalDate localDateResult = LocalDate.of(year, month, res);
    ZoneId defaultZoneId = ZoneId.systemDefault();
    return Date.from(localDateResult.atStartOfDay(defaultZoneId).toInstant());
  }

  public static Date getStartWeek(Date from) {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_VN));
    calendar.setTime(from);
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
      calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
    }
    return calendar.getTime();
  }

  public static Date getEndWeek(Date from) {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_VN));
    calendar.setTime(from);
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
      calendar.add(Calendar.DATE, 1); // Substract 1 day until first day of week.
    }
    return calendar.getTime();
  }

  public static int getLastDayOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.getActualMaximum(Calendar.DATE);
  }

  public static String converListToIn(List<String> stringList) {
    String result = "";
    for (String item : stringList) {
      result += "'" + item + "',";
    }
    result = result.substring(0, result.length() - 1);
    result = result + "";
    return result;
  }

  public static void main(String[] args) throws ParseException {
    String timeFrame = "1W";
    StringBuilder fromDate = new StringBuilder("2021-10-04");
    StringBuilder toDate = new StringBuilder("2021-04-03");
    formatRuleFromTo(timeFrame, fromDate, toDate);
    System.out.println(fromDate);
    System.out.println(toDate);

    List<String> listString = new ArrayList<>();
    listString.add("A");
    listString.add("B");

    System.out.println(converListToIn(listString));
  }
}
