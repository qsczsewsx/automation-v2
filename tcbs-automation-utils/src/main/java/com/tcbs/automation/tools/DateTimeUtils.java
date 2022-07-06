package com.tcbs.automation.tools;

import com.tcbs.automation.tcbond.Coupon;
import com.tcbs.automation.tcinvest.InvHoliday;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {
  public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_VN = "dd/MM/yyyy";
  public static final String DATE_FORMAT_VN_SHORT = "dd/MM/yy";
  public static final String DATE_FORMAT_ETL = "yyyyMMdd";
  public static final String ISO_STANDARD_DATE = "yyyy-MM-dd";
  public static final String ISO_STANDARD_DATE_VN = "dd-MM-yyyy";
  public static final String INTRADAY_DATE_ONLY = "dd/MM";
  public static final String INTRADAY_DATE_TIME = "dd/MM HH:mm";
  public static final String INTRADAY_TIME_ONLY = "HH:mm";
  public static final String DATE_FORMAT_WITHOUT_DAY = "yyyyMM";
  public static final String ISO_DATE_WITH_TIME = "dd/MM/yy HH:mm";
  public static final String INTRADAY_TIME_WITH_SECOND = "HH:mm:00";
  public static final String DATE_ISO_FORMAT_NO_MIL_STR = "yyyy-MM-dd'T'HH:mm:ssXXX";
  public static final String DATE_ISO_FORMAT_LOCAL_STR = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String DATE_ISO_FORMAT_T_LOCAL_STR = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String DATE_ISO_FORMAT_LOCAL_NO_MIL = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String DATE_ISO_FORMAT_LOCAL_ZERO = "yyyy-MM-dd'T'00:00:00";
  public static final String DATE_ISO_FORMAT_ZERO = "yyyy-MM-dd' '00:00:00.0";
  public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final String RFC_822_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  public static final String ISO_8601_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final String ISO_8601_FORMAT_ZERO_MILI = "yyyy-MM-dd'T'HH:mm:ss.000'Z'";
  public static final String RFC_822_FORMAT_ZERO_HOUR = "yyyy-MM-dd'T'00:00:00'Z'";
  public static final String DATE_FORMAT_NO_MIL = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  public static final String ISO_8601_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX'Z'";
  public static final String DATE_FORMAT_WITHOUT_SPLIT = "ddMMyyyy";
  public static final String ISO_STANDARD_DATE_US = "MM-dd-yyyy";
  public static final String DATE_FORMAT_VN_WITH_TIME = "dd/MM/yyyy HH:mm";

  public static final String VN_TIME_ZONE = "GMT+7";
  public static final long SECOND_IN_DAY = 86400;
  public static final TimeZone vnTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
  private static final Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);
  private static final List<InvHoliday> listHoliday = new InvHoliday().listHoliday();


  public static Date convertDateToDate(Date needToConvert, String newDatePattern) {
    if (needToConvert != null) {
      return convertStringToDate(convertDateToString(needToConvert, newDatePattern), newDatePattern);
    }
    return null;
  }

  /**
   * @return the nearest business date from current date
   */
  public static Date getNearestBusinessDate() {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    Date date = calendar.getTime();
    if (!isWorkingDay(date)) {
      date = addBusinessDate(date, 1);
    }
    return date;
  }

  /**
   * @param bondId
   * @return couponDate that couponDate -1 and couponDate +1 is business date
   */
  public static Date getNextPerfectCouponFixDate(Integer bondId) {
    Date couponFixDate;
    Coupon coupon = new Coupon();
    List<Coupon> listCoupon = coupon.getListCouponDataFromBondId(Long.valueOf(bondId));
    for (Coupon couponData : listCoupon) {
      couponFixDate = couponData.getCouponDate();
      if (getNearestBusinessDate().after(couponFixDate)
        || !isWorkingDay(addNormalDate(couponFixDate, -1))
        || !isWorkingDay(addNormalDate(couponFixDate, 1))) {
        continue;
      }
      return couponFixDate;
    }
    return null;
  }

  public static Date getNextNormalHoliday() {
    for (InvHoliday holidayData : listHoliday) {
      Date holidayDate = holidayData.getTimestamp();
      if (getCurrentDate().after(holidayDate)
        || isWeekend(holidayDate)) {
        continue;
      } else {
        return holidayDate;
      }
    }
    return null;
  }

  public static Date getNextNormalDayOfWeek(Integer day) {
    Calendar calendar = Calendar.getInstance();
    while (calendar.get(Calendar.DAY_OF_WEEK) != day
      || isHoliday(calendar.getTime())) {
      calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
    }
    return calendar.getTime();
  }

  //I don't know what this function means, it seems like it's very pointless
  public static Date getCurrentDate() {
    return new Date();
  }

  /**
   * @param inputDate:       the date that want to increase
   * @param numBusinessDays: number of business date (working date) that want to add
   * @return the date value after being increased
   */
  public static Date addBusinessDate(Date inputDate, int numBusinessDays) {
    int numberDate = 0;
    int flagAdd = 1;
    SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_STANDARD_DATE);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(inputDate);
    if (numBusinessDays < 0) {
      flagAdd = -1;
    }
    while (numberDate < numBusinessDays * flagAdd) {
      calendar.add(Calendar.DATE, flagAdd);
      if (isWorkingDay(calendar.getTime())) {
        numberDate++;
      }
    }
    return calendar.getTime();
  }

  //Verify the date is working day (is not holiday and not weekend)
  public static boolean isWorkingDay(Date date) {
    return !isHoliday(date) && !isWeekend(date);
  }

  //Verify the date is weekend
  public static boolean isWeekend(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
  }

  //Verify the date is holiday
  public static boolean isHoliday(Date date) {
    for (InvHoliday holiday : listHoliday) {
      if (holiday.getTimestamp().compareTo(date) == 0) {
        return true;
      }
    }
    return false;
  }

  //Verify the date is the working of week
  public static boolean isWorkingOfWeek(Date date, String workingOfWeek) {
    return workingOfWeek.contains(new SimpleDateFormat("EEE").format(date).toUpperCase());
  }

  //Verify the date is the end of the month
  public static boolean isEndOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    return day == maxDayOfMonth;
  }

  //Verify day of month to date is before day ò month from date
  public static boolean isBeforeByDayOfMonth(Date toDate, Date fromDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fromDate);
    int dayOfFromDate = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.setTime(toDate);
    int dayOfToDate = calendar.get(Calendar.DAY_OF_MONTH);
    return dayOfFromDate > dayOfToDate;
  }

  //Function calculate next working day
  public static Date workingDateNearest(Date fromDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fromDate);
    while (!isWorkingDay(calendar.getTime())) {
      calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
    }
    return calendar.getTime();
  }

  //Function calculate next working day
  public static Date nextWorkingDay(Date fromDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fromDate);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
    while (!isWorkingDay(calendar.getTime())) {
      calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
    }
    return calendar.getTime();
  }

  //Function calculate next date
  public static Date calculateNextNormalDate(Date fromDate, int numberMonths, int numberDays) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fromDate);
    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + numberMonths);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + numberDays);
    return calendar.getTime();
  }

  //The function to find the nearest weekend
  public static Date findWeekendNearest(Date fromDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fromDate);
    while (!isWeekend(calendar.getTime())) {
      calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
    }
    return calendar.getTime();
  }

  /**
   * Tìm ngày cuối tuần gần nhất so với ngày hiện tại
   */
  public static String findWeekend() {
    Calendar cl = Calendar.getInstance();
    while ((cl.get(Calendar.DAY_OF_MONTH) != Calendar.SATURDAY) || (cl.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)) {
      cl.set(Calendar.DAY_OF_MONTH, cl.get(Calendar.DAY_OF_MONTH) + 1);
    }
    return new SimpleDateFormat(ISO_STANDARD_DATE).format(cl.getTime());
  }

  /*Function get max day of month*/
  public static Date calculateMaxDayOfMonth(int months) {
    Calendar calendar = Calendar.getInstance();

    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + months - 1);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR_OF_DAY, 00);
    calendar.set(Calendar.MINUTE, 00);
    calendar.set(Calendar.SECOND, 00);
    return calendar.getTime();
  }

  /*Function convert String to Date format*/
  public static Date convertStringToDate(String strDate, String formatDate) {
    try {
      return new SimpleDateFormat(formatDate).parse(strDate);
    } catch (ParseException e) {
      logger.error(e.getMessage(), e.getStackTrace());
      return null;
    }
  }

  /*Function convert String to Date and Date to String with format date*/
  public static String convertStringToDateAndViceVersa(String strDate, String formatDate) {
    try {
      Date strToDate = new SimpleDateFormat(formatDate).parse(strDate);
      return new SimpleDateFormat(formatDate).format(strToDate);
    } catch (ParseException e) {
      logger.error(e.toString());
      return null;
    }
  }

  public static String convertTimestampToString(Timestamp timestamp, String formatDate) {
    Date date = new Date();
    date.setTime(timestamp.getTime());
    return new SimpleDateFormat(formatDate).format(date);
  }

  public static String convertTimestampToStringWithTimezone(Timestamp timestamp, String formatDate, String timezone) {
    Date date = new Date();
    date.setTime(timestamp.getTime());
    SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
    sdf.setTimeZone(TimeZone.getTimeZone(timezone));
    return sdf.format(date);
  }

  /*Function convert String to Timestamp format*/
  public static Timestamp convertStringToTimestamp(String strDate, String formatDate) {
    try {
      Date date = new SimpleDateFormat(formatDate).parse(strDate);
      return new Timestamp(date.getTime());
    } catch (ParseException e) {
      logger.error(e.toString());
      return null;
    }
  }

  /*Function convert Date to String with format date*/
  public static String convertDateToString(Date strDate, String formatDate) {
    return new SimpleDateFormat(formatDate).format(strDate);
  }

  /**
   * @param fromPattern
   * @param toPattern
   * @return by days
   */
  public static Long getPeriod(Date fromPattern, Date toPattern) {
    if (fromPattern == null || toPattern == null) {
      return null;
    }
    long diffInMillies = fromPattern.getTime() - toPattern.getTime();
    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    return diff;
  }

  /*Function change format date*/
  public static String formatDateStringToString(String date, String inputFormat, String outputFormat) {
    DateFormat format = new SimpleDateFormat(inputFormat);
    format.setLenient(false);
    Date inputDate = null;
    try {
      inputDate = format.parse(date);
    } catch (ParseException e) {
      logger.error(e.toString());
    }
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(outputFormat);
    return simpleDateFormat.format(inputDate);
  }

  //The function verify format date
  public static boolean isValidFormat(String format, String value) {
    LocalDateTime ldt = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);

    try {
      ldt = LocalDateTime.parse(value, formatter);
      String result = ldt.format(formatter);
      return result.equals(value);
    } catch (DateTimeParseException e) {
      try {
        LocalDate ld = LocalDate.parse(value, formatter);
        String result = ld.format(formatter);
        return result.equals(value);
      } catch (DateTimeParseException exp) {
        try {
          LocalTime lt = LocalTime.parse(value, formatter);
          String result = lt.format(formatter);
          return result.equals(value);
        } catch (DateTimeParseException e2) {
          return false;
        }
      }
    }
  }

  /*Function get date  buy day and week */
  public static Date getDateOfDay(String day, int addWeek) {
    int addDay = 0;
    switch (day) {
      case "monday":
        addDay = Calendar.MONDAY;
        break;
      case "friday":
        addDay = Calendar.FRIDAY;
        break;
      case "saturday":
        addDay = Calendar.SATURDAY;
        break;
      case "sunday":
        addDay = Calendar.SUNDAY;
        break;
      default:
        // code block
    }
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_WEEK, addDay);
    c.add(Calendar.WEEK_OF_YEAR, addWeek);
    return c.getTime();
  }

  /**
   * @param inputDate
   * @param numNormalDays
   * @return date with ISO_STANDARD_DATE format
   */
  public static Date addNormalDate(Date inputDate, int numNormalDays) {
    return addNormalDate(inputDate, numNormalDays, ISO_STANDARD_DATE);
  }

  public static Date addHoursToJavaUtilDate(Date date, int hours) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.HOUR_OF_DAY, hours);
    return calendar.getTime();
  }

  public static String addHourCurrentTime(int addHours) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.HOUR_OF_DAY, addHours);
    return calendar.get(Calendar.HOUR_OF_DAY) + "h" + calendar.get(Calendar.MINUTE);
  }

  /**
   * @param inputDate
   * @param numNormalDays
   * @param format
   * @return date with given format
   */
  public static Date addNormalDate(Date inputDate, int numNormalDays, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(inputDate);
    calendar.add(Calendar.DATE, numNormalDays);
    String businessDate = dateFormat.format(calendar.getTime());
    Date date = new Date();
    try {
      date = dateFormat.parse(businessDate);
    } catch (ParseException e) {
      logger.error(e.getMessage(), e.getStackTrace());
    }
    return date;
  }

  public static Date getToday() {
    return convertDateToDate(new Date(), ISO_STANDARD_DATE);
  }

  public static boolean isBetweenTwoDate(Date fromDate, Date toDate, Date date) {
    return DateUtils.isSameDay(fromDate, date) || DateUtils.isSameDay(toDate, date) || (fromDate.before(date) && date.before(toDate));
  }

  public static boolean isLessThanOrEqualToDate(Date date, Date date2) {
    return date.before(date2) || DateUtils.isSameDay(date, date2);
  }

  public static int dayBetween(Date fromDate, Date toDate) {
    return Days.daysBetween(new DateTime(fromDate), new DateTime(toDate)).getDays();
  }

  public static String setTimeConditionalOrder(int addDay) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, addDay);
    String timeString = new SimpleDateFormat(ISO_STANDARD_DATE).format(calendar.getTime());
    return timeString;
  }

  public static String setTimeConditionalOrderWithFormat(int addDay, String format) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, addDay);
    String timeString = new SimpleDateFormat(format).format(calendar.getTime());
    return timeString;
  }

  public static int calculateNotifyDateForEvent(Integer notifyDate) {
    Integer plusDate;
    Date endDateAdj = DateTimeUtils.addNormalDate(DateTimeUtils.getCurrentDate(), notifyDate);
    Calendar cal = Calendar.getInstance();
    cal.setTime(endDateAdj);
    if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
      plusDate = notifyDate - 1;
    } else {
      plusDate = notifyDate;
    }
    return plusDate;
  }

  public static Date toShortDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 00);
    cal.set(Calendar.MINUTE, 00);
    cal.set(Calendar.SECOND, 00);
    cal.set(Calendar.MILLISECOND, 000);
    return cal.getTime();
  }

  public static Date preWorkingDayIncludeHoliday(Date date) {
    Calendar workCal = Calendar.getInstance();
    workCal.setTime(date);
    do {
      workCal.add(Calendar.DAY_OF_MONTH, -1);
    } while (DateTimeUtils.isWeekend(workCal.getTime()));
    return workCal.getTime();
  }

  public static Date preWorkingDay(Date date) {
    Calendar workCal = Calendar.getInstance();
    workCal.setTime(date);
    while (!DateTimeUtils.isWorkingDay(workCal.getTime())) {
      workCal.add(Calendar.DAY_OF_MONTH, -1);
    }
    return workCal.getTime();
  }

  public static String convertDateStrFromVnTimeToGMT(Date dateVnTime, String dateFormat) {
    SimpleDateFormat sdfGMT = new SimpleDateFormat(dateFormat);
    sdfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    return sdfGMT.format(dateVnTime);

  }

  public static String convertDateStrFromGmtToVnTime(Date originalTime, String dateFormat) {
    SimpleDateFormat sdfGMT = new SimpleDateFormat(dateFormat);
    sdfGMT.setTimeZone(TimeZone.getTimeZone(VN_TIME_ZONE));
    return sdfGMT.format(originalTime);
  }

  public static long timestampAtLunchStart() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    now.set(Calendar.HOUR_OF_DAY, 11);
    now.set(Calendar.MINUTE, 30);
    now.set(Calendar.SECOND, 0);

    return now.getTimeInMillis() / 1000;
  }

  public static long timestampAtLunchEnd() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    now.set(Calendar.HOUR_OF_DAY, 13);
    now.set(Calendar.MINUTE, 00);
    now.set(Calendar.SECOND, 00);

    return now.getTimeInMillis() / 1000;
  }

  public static long timestampAtOpenning() {
    Calendar now = Calendar.getInstance(vnTimeZone);
    now.set(Calendar.HOUR_OF_DAY, 8);
    now.set(Calendar.MINUTE, 45);
    now.set(Calendar.SECOND, 0);

    return now.getTimeInMillis() / 1000;
  }

  public static long timestampAtClosing() {
    Calendar now = Calendar.getInstance(vnTimeZone);
    now.set(Calendar.HOUR_OF_DAY, 15);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);

    return now.getTimeInMillis() / 1000;
  }

  public static long timestampPresent() {
    Calendar now = Calendar.getInstance(vnTimeZone);
    return now.getTimeInMillis() / 1000;
  }

  public static String isoLocalDateTimeFormat(Date date) {
    SimpleDateFormat localDateTimeIsoFormat = new SimpleDateFormat(ISO_8601_FORMAT_1);
    return localDateTimeIsoFormat.format(date);
  }

  public static String setTimeInbox(int addDay) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, addDay);
    String timeString = new SimpleDateFormat(ISO_8601_FORMAT_1).format(calendar.getTime());
    return timeString;
  }

  public static String setTimeGetInbox(int addDay) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, addDay);
    String timeString = new SimpleDateFormat(ISO_8601_FORMAT_ZERO_MILI).format(calendar.getTime());
    return timeString;
  }

  public static String setTimeAttribute(int addDay) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, addDay - 1);
    calendar.set(Calendar.HOUR_OF_DAY, 17);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    String timeString = new SimpleDateFormat(ISO_8601_FORMAT_ZERO_MILI).format(calendar.getTime());
    return timeString;
  }

  public static String convertOldFormatDateToNewFormat(String oldFormat, String dateInput, String newFormat) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(oldFormat);
    Date date = formatter.parse(dateInput);
    String timeString = new SimpleDateFormat(newFormat).format(date);
    return timeString;
  }

  public static String timeStampToTimeFormat(Timestamp timestamp) {
    if (timestamp == null) {
      return "";
    }
    SimpleDateFormat localDateTimeIsoFormat = new SimpleDateFormat(ISO_8601_FORMAT_1);
    localDateTimeIsoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return localDateTimeIsoFormat.format(timestamp);
  }

  public static Long numDayBeforeAtOpenning(int numDayBefore) {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone(VN_TIME_ZONE));
    now.set(Calendar.HOUR_OF_DAY, 9);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);

    Date currentDate = now.getTime();
    while (numDayBefore > 0) {
      currentDate = preWorkingDayIncludeHoliday(currentDate);
      numDayBefore--;
    }

    now.setTime(currentDate);
    return now.getTimeInMillis() / 1000;
  }

  public static Long numDayBeforeAtClose(int numDayBefore) {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone(VN_TIME_ZONE));
    now.set(Calendar.HOUR_OF_DAY, 15);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);

    Date currentDate = now.getTime();
    while (numDayBefore > 0) {
      currentDate = preWorkingDayIncludeHoliday(currentDate);
      numDayBefore--;
    }

    now.setTime(currentDate);
    return now.getTimeInMillis() / 1000;
  }

  public static long preMonthAtOpening() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone(VN_TIME_ZONE));
    now.set(Calendar.HOUR_OF_DAY, 9);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);

    now.add(Calendar.MONTH, -1);

    while (!isWorkingDay(now.getTime())) {
      now.add(Calendar.DAY_OF_MONTH, 1);
    }

    return now.getTimeInMillis() / 1000;
  }

  public static long preYearAtFirstDay() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone(VN_TIME_ZONE));
    now.set(Calendar.HOUR_OF_DAY, 0);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);

    now.add(Calendar.YEAR, -1);
    now.set(Calendar.DAY_OF_MONTH, 1);

    return now.getTimeInMillis() / 1000;
  }

  public static String getCurrentDateWithoutTime() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone(VN_TIME_ZONE));
    Timestamp ts = new Timestamp(now.getTimeInMillis());
    Date d = new Date(ts.getTime());
    SimpleDateFormat sdf = new SimpleDateFormat(ISO_STANDARD_DATE);
    sdf.setTimeZone(TimeZone.getTimeZone(VN_TIME_ZONE));
    return sdf.format(d);
  }


  public static long timestampStartTrading() {
    Calendar now = Calendar.getInstance(vnTimeZone);
    now.set(Calendar.HOUR_OF_DAY, 9);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);

    return now.getTimeInMillis() / 1000;
  }


  public static long timestampStartLOTrading(String exchangeId) {
    if ("HOSE".equalsIgnoreCase(exchangeId) || "VN30".equalsIgnoreCase(exchangeId)) {
      return timestampStartTrading() + 15 * 60;
    } else {
      return timestampStartTrading();
    }
  }

  public static long timestampEndLOTrading(String exchangeId) {
    if ("UPCOM".equalsIgnoreCase(exchangeId)) {
      return timestampAtClosing();
    } else {
      return timestampAtClosing() - 30 * 60;
    }
  }

  /**
   * without detect holiday
   *
   * @param exchangeId
   * @param input
   * @return
   */
  public static boolean inTradingTime(String exchangeId, long input) {
    long startDay = input - (input % 86400);   // 86400 = 24 * 3600 = 1 ngay
    long startTradingTime = startDay + 7200;    // => 2h (9h GTM+7)
    long timestampAtLunchStart = startDay + 16200; // => 4h 30' (11h 30 GTM+7)
    long timestampAtLunchEnd = startDay + 21600; // => 4h 30' (11h 30 GTM+7)
    long endTradingTime = startDay + 28800; // => 8h (15h GTM+7)
    if ("HOSE".equals(exchangeId)) {
      endTradingTime -= 15 * 60; // => // => 14h45m GTM+7
    }
    return (!isWeekend(input) && ((input >= startTradingTime && input <= timestampAtLunchStart)
      || (input >= timestampAtLunchEnd && input <= endTradingTime)));
  }

  public static boolean isWeekend(long input) {
    long saturdayAt = 18265; // 04/01/2020
    long secondsInDay = 86400;
    long dayNo = input / secondsInDay;
    return ((dayNo - saturdayAt) % 7 == 0 || (dayNo - saturdayAt) % 7 == 1);
  }

  public static boolean inTradingTime(long input) {
    long startDay = (input / 86400) * 86400;   // 86400 = 24 * 3600 = 1 ngay
    long startTradingTime = startDay + 7200;    // => 2h (9h GTM+7)
    long timestampAtLunchStart = startDay + 16200; // => 4h 30' (11h 30 GTM+7)
    long timestampAtLunchEnd = startDay + 21600; // => 4h 30' (11h 30 GTM+7)
    long endTradingTime = startDay + 28800;

    return (!isWeekend(new Date(input * 1000)) && (input >= startTradingTime && input < timestampAtLunchStart)
      || (input >= timestampAtLunchEnd && input < endTradingTime));
  }

  //Add MONTH
  public static String addMonth(String dateValue, Integer month) {
    java.sql.Date date = java.sql.Date.valueOf(dateValue);
    SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_STANDARD_DATE);
    Calendar c1 = Calendar.getInstance();
    c1.setTime(date);
    c1.add(Calendar.MONTH, month);
    return dateFormat.format(c1.getTime());
  }

  //Add Year
  public static String addYear(Date dateValue, Integer year, String timezone, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone(timezone));
    c1.setTime(dateValue);
    c1.add(Calendar.YEAR, year);
    return dateFormat.format(c1.getTime());
  }

  //Convert String to TimeStamp
  public static Timestamp stringToTimeStamp(String dateValue) {
    String oldFormat = ISO_STANDARD_DATE;
    String newFormat = DATE_ISO_FORMAT_LOCAL_STR;
    String newDateString;

    DateFormat formatter = new SimpleDateFormat(oldFormat);
    Date d = null;
    try {
      d = formatter.parse(dateValue);
    } catch (ParseException e) {
      System.out.println(e);
    }
    ((SimpleDateFormat) formatter).applyPattern(newFormat);
    newDateString = formatter.format(d);

    Timestamp ts = Timestamp.valueOf(newDateString);
    return ts;
  }

  public static String calculatePeriod(int day, Date latestDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(latestDate);

    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - day);
    calendar.set(Calendar.HOUR_OF_DAY, 00);
    calendar.set(Calendar.MINUTE, 00);
    calendar.set(Calendar.SECOND, 00);

    if (isWorkingDay(calendar.getTime())) {
      return convertDateToString(calendar.getTime(), ISO_STANDARD_DATE);
    }
    return StringUtils.EMPTY;
  }

  public static String addHoursUTC(int hours) {
    Instant instant = Instant.now();
    instant = instant.plus(hours, ChronoUnit.HOURS);
    Date date = Date.from(instant);
    SimpleDateFormat df = new SimpleDateFormat(ISO_DATETIME_FORMAT);
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    String timeString = df.format(date);
    return timeString;
  }

  public static String addMonthUTC(int months, int days) {
    Instant instant = Instant.now();
    Date date = Date.from(instant);
    date = addMonthAndDays(date, months,-1);
    SimpleDateFormat df = new SimpleDateFormat(ISO_DATETIME_FORMAT);
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    String timeString = df.format(date);
    return timeString;
  }

  public static Date addMonthAndDays(Date date, int months, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, months);
    cal.add(Calendar.DATE, days); //minus number would decrement the days
    return cal.getTime();
  }

  public static Timestamp longToTimeDate(Long timestamp) {
    SimpleDateFormat localDateTimeIsoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    localDateTimeIsoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return convertStringToTimestamp(localDateTimeIsoFormat.format(timestamp), "yyyy-MM-dd HH:mm:ss.S");
  }

  public static String longToTimeFormat(long timestamp) {
    SimpleDateFormat localDateTimeIsoFormat = new SimpleDateFormat("yyyy-MM-dd");
    localDateTimeIsoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return localDateTimeIsoFormat.format(timestamp);
  }

  public static int getDateField(Date date, int field) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(field);
  }

  public static Date setDateField(Date date, int field, int value) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(field, value);
    return calendar.getTime();
  }

  public static Date getNextDay(Integer addDay) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, addDay);
    return calendar.getTime();
  }
}
